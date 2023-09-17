package com.zipkimi.builder.service;

import static com.zipkimi.global.utils.CommonUtils.generateNumber;
import static com.zipkimi.global.utils.RegexUtils.isValidPhoneNumber;

import com.zipkimi.entity.BuilderUserEntity;
import com.zipkimi.entity.SmsAuthEntity;
import com.zipkimi.entity.UserEntity;
import com.zipkimi.global.exception.BadRequestException;
import com.zipkimi.global.service.SmsService;
import com.zipkimi.global.utils.CodeConstant.SMS_AUTH_CODE;
import com.zipkimi.repository.BuilderRepository;
import com.zipkimi.repository.BuilderUserRepository;
import com.zipkimi.repository.SmsAuthRepository;
import com.zipkimi.repository.UserRepository;
import com.zipkimi.user.dto.request.SmsAuthNumberPostRequest;
import com.zipkimi.user.dto.response.SmsAuthNumberPostResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class BuilderManagementService {

    private final BuilderRepository builderRepository;
    private final BuilderUserRepository builderUserRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final SmsAuthRepository smsAuthRepository;
    private final SmsService smsService;

    public SmsAuthNumberPostResponse sendBuilderUserJoinSmsAuthNumber(
            SmsAuthNumberPostRequest requestDto) {

        // 휴대폰 번호 유효성 검사 - 타입, 글자수, 이미 등록된 번호인지 체크
        String phoneNumber = requestDto.getPhoneNumber().replaceAll("\\D", ""); // "\\D" 정규식을 사용하여 숫자이외의 문자는 모두 ""으로 변경
        if (phoneNumber == null || !isValidPhoneNumber(phoneNumber)) {
            return SmsAuthNumberPostResponse.builder()
                    .message("휴대폰 번호를 정확히 입력해주세요.")
                    .build();
        }

        // 시공사 회원 중복 확인
        Optional<BuilderUserEntity> builderUser = builderUserRepository.findByPhoneNumberAndIsUseIsTrue(phoneNumber);
        // 일반 회원 중복 확인
        Optional<UserEntity> user = userRepository.findByPhoneNumberAndIsUseIsTrue(phoneNumber);

        // 일반 회원으로 가입된 휴대폰 번호거나, 시공사 회원으로 가입된 휴대폰 번호일 경우
        if (builderUser.isPresent() || user.isPresent()) {
            return SmsAuthNumberPostResponse.builder()
                    .message("이미 등록된 휴대폰 번호입니다.")
                    .build();
        }

        // 휴대폰번호/인증완료여부/만료시간만료여부/인증타입으로 만료되지 않은 인증번호 있는지 조회
        SmsAuthEntity existingSmsAuth =
                smsAuthRepository.findValidSmsAuthByPhoneNumberAndType(phoneNumber,
                        LocalDateTime.now(), SMS_AUTH_CODE.BUILDER_JOIN.getValue());

        String randomNumber;
        SmsAuthEntity smsAuthEntitySaved = null;

        if (existingSmsAuth != null) {
            // SMS 인증번호가 만료되지 않았을 경우
            existingSmsAuth.setExpirationTime(LocalDateTime.now().plusMinutes(5L));
            smsAuthRepository.save(existingSmsAuth);
            return SmsAuthNumberPostResponse.builder()
                    .message("유효시간이 만료되지 않은 인증번호가 존재합니다. \n인증번호를 확인하거나, 유효시간이 지난 후 다시 시도해주세요.")
                    .build();
        } else {
            // 인증번호 생성 : 4자리(중복 x)
            randomNumber = generateNumber(4, 2);

            SmsAuthEntity smsAuth = new SmsAuthEntity();
            smsAuth.setPhoneNumber(phoneNumber);
            smsAuth.setSmsAuthNumber(randomNumber);
            smsAuth.setIsAuthenticate(false);
            smsAuth.setExpirationTime(LocalDateTime.now().plusMinutes(5L));
            smsAuth.setSmsAuthType(SMS_AUTH_CODE.BUILDER_JOIN.getValue());

            // SMS 내용 설정
            smsAuth.setContent("[집킴이] 본인확인 인증번호는 [" + smsAuth.getSmsAuthNumber() + "] 입니다. 인증번호를 정확히 입력해주세요.");

            try {
                // DB 테이블에 insert
                smsAuthEntitySaved = smsAuthRepository.save(smsAuth);

                // SMS 전송 로직
                smsService.pushSMSMessage(smsAuthEntitySaved);

            } catch (Exception e) {
                throw new BadRequestException("SMS 인증번호를 생성하던 중 오류가 발생하였습니다.");
            }
        }

        return SmsAuthNumberPostResponse.builder()
                .smsAuthId(smsAuthEntitySaved.getSmsAuthId())
                .message("인증번호를 전송하였습니다.")
                .build();
    }

}