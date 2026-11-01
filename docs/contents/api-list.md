# API 목록

[문서 Home](../index.md)

| 번호 | API명     |    분류     | 영문명  | 주소                          | 구현여부 |
| :--: | :-------- | :---------: | :------ | :---------------------------- | :------: |
|  1   | 회원가입  | 사용자 계정 | SignUp  | POST /api/v1/accounts/signup  |   구현   |
|  2   | 로그인    | 사용자 계정 | SignIn  | POST /api/v1/accounts/signin  |   구현   |
|  3   | 로그아웃  | 사용자 계정 | SignOut | POST /api/v1/accounts/signout |   구현   |
|  4   | 토큰 갱신 | 사용자 계정 | Reissue | POST /api/v1/accounts/reissue |   구현   |

## API 응답 기본 구조

```json
// 성공시
{
  "isSuccess": true,
  "timestamp": "2000-00-00T00:00:00",
  "result": DTO,
  "errorCode": null,
  "errorMessage": null
}

// 실패시
{
  "isSuccess": false,
  "timestamp": "2000-00-00T00:00:00",
  "result": null,
  "errorCode": "E100001",
  "errorMessage": "에러 내용 메시지"
}
```

- 분류
  - 사용자 계정
  - 집중시간
- 구현여부
  - 미구현
  - 구현
  - 진행중
