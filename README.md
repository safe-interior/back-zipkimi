
# 인테리어 사기 제로 프로젝트 

## 목적
인테리어 사기꾼으로부터 이용자의 피해를 방지한다.

## 방법
   1. *사기 예방법 공유*            -> 기본 게시판 (등록 수정 삭제 조회 상세조회), 이미지 사이즈 제한
   2. 피해자 커뮤니티               -> 기본 게시판 (등록 수정 삭제 조회 상세조회)

   3. *좋은 업체 연결 플랫폼*
         - 업체 목록 (업체명, 평점, 리뷰, 시공사례, 업체 주소, 기본 업체정보 : 전화번호, 대표자, 사업자번호, 자격증명)
         - 안전 업체 선정 기준 
   4. 업체 평점 및 리뷰 관리

   5. *계약 프로세스 관리* : 돈!, 실제 공사 과정 확인
         견적요청, 견적금액 합리성, 

### 개발 차순위 
   6. 셀프 인테리어 기술 공유         -> 기본 게시판 (등록 수정 삭제 조회 상세조회)
     -> 대신 인테리어 팁 ? - 업체를 연결하는 플랫폼과 셀프인테리어 항목과는 맞지 않는 거같음
   7. 사기 업체 리스트 + 증거 (고발로 인한 법의 위험을 피해서 진행)
   8. 사기꾼에게 유리한 법 정리 -> 수정 요청

## 수익구조
1. 중개료
2. 계약 프로세스 관리료
3. 광고

## 구현 순서
### 1차 구현 순서
1. 사용자 정보 관리 DB 설계 - 권한 설계
2. 게시글 정보 관리 DB 설계
3. 게시판 구현 (사기 방지 방법 공유, 피해자 커뮤니티, 셀프 인테리어 기술 공유)

### 2차 구현 순서
4,5
- 권한별 로그인/ 로그아웃  -> JWT, OAuth ?
- 권한별 메뉴 설정, 관리
- 플랫폼 (인테리어 업체와 사용자를 연결)
- 좋은 업체 선정 기준 설정
- 결제 시스템 구축

### 3차 구현 순서
6 -> **중요 사항 : 시공 프로세스 관리 구현
7,8


## 사용 기술 스택
Java - 17
, Spring
, Gradle
, JPA
, AWS
, JUnit5
, JWT
, OAuth
, Swaager
