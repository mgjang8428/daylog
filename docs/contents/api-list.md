# API 목록

[문서 Home](../index.md)

| 번호 | API명 | 분류 | 영문명 | 주소 | 구현여부 |
| :--: | :---- | :--: | :----- | :--- | :------: |

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
  "errorCode": "E10001",
  "errorMessage": "에러 내용 메시지"
}
```

- 분류
- 구현여부
  - 미구현
  - 구현
  - 진행중
