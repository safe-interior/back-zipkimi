package com.zipkimi.user.controller;

import com.zipkimi.user.dto.request.FindPwCheckSmsGetRequest;
import com.zipkimi.user.dto.request.PassResetSmsAuthNumberPostRequest;
import com.zipkimi.user.dto.request.FindIdCheckSmsGetRequest;
import com.zipkimi.user.dto.request.SmsAuthNumberPostRequest;
import com.zipkimi.user.dto.response.FindSmsAuthNumberGetResponse;
import com.zipkimi.user.dto.response.FindSmsAuthNumberPostResponse;
import com.zipkimi.user.service.UserLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@Api(value = "로그인")
public class UserLoginController {

    private UserLoginService loginService;

    // ************* 아이디 찾기 *************

    @ApiOperation(value = "아이디 찾기 - SMS 인증번호 전송")
    @PostMapping(value = "/api/v1/users/find-id/sms")
    public ResponseEntity<FindSmsAuthNumberPostResponse> sendFindIdSmsAuthNumber(
            @RequestBody @Validated SmsAuthNumberPostRequest requestDto) {
        FindSmsAuthNumberPostResponse response = loginService.sendFindIdSmsAuthNumber(
                requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiOperation(value = "아이디 찾기 - SMS 인증번호 확인 및 아이디 찾기")
    @GetMapping(value = "/api/v1/users/find-id/sms/{smsAuthId}")
    public ResponseEntity<FindSmsAuthNumberGetResponse> checkFindIdSmsAuth(
            @ModelAttribute FindIdCheckSmsGetRequest requestDto) {

        // #1. SMS 인증번호 DB 데이터 검증 후
        // #2. 휴대폰번호로 아이디 찾기
        return ResponseEntity.status(HttpStatus.OK)
                .body(loginService.checkFindIdSmsAuth(requestDto));

    }

    // ************* 비밀번호 찾기 *************

    @ApiOperation(value = "비밀번호 찾기 - SMS 인증번호 전송")
    @PostMapping(value = "/api/v1/users/find-pw/sms")
    public ResponseEntity<FindSmsAuthNumberPostResponse> sendFindPwSmsAuthNumber(
            @RequestBody @Validated PassResetSmsAuthNumberPostRequest requestDto) {
        FindSmsAuthNumberPostResponse response = loginService.sendFindPwSmsAuthNumber(
                requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiOperation(value = "비밀번호 찾기 - SMS 인증번호 확인 및 비밀번호 초기화")
    @GetMapping(value = "/api/v1/users/find-pw/sms/{smsAuthId}")
    public ResponseEntity<FindSmsAuthNumberGetResponse> verifySmsAuthAndResetPw(
            @ModelAttribute FindPwCheckSmsGetRequest requestDto) {

        // #1. SMS 인증번호 DB 데이터 검증 후
        // #2. 이메일과 휴대폰 번호로 아이디 찾기
        return ResponseEntity.status(HttpStatus.OK)
                .body(loginService.checkFindPwSmsAuth(requestDto));

    }
}
