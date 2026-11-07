export const ServiceErrorCode = {
	// 미정의 오류
	//정의되지않은 오류.
	UNDEFINED_ERROR: "E000000",

	// 공통 오류
	//DTO Validate 실패 오류
	DTO_VALIDATE_FAILED: "E000001",
	//이미 삭제된 데이터입니다.
	ALREADY_DELETED: "E000002",
	//삭제되지 않은 데이터입니다.
	NOT_DELETED: "E000003",

	// Member 관련 오류
	//중복된 이메일입니다.
	DUPLICATE_EMAIL: "E100001",
	//중복된 닉네임입니다.
	DUPLICATE_NICKNAME: "E100002",
	//이메일 또는 비밀번호가 일치하지 않습니다.
	INVALID_CREDENTIALS: "E100003",
	//잘못된 RefreshToken 입니다.
	INVALID_REFRESH_TOKEN: "E100004",
	//이메일은 대문자 불가입니다.
	UPPERCASE_EMAIL_NOT_ALLOWED: "E100005",
	//잘못된 타임존입니다.
	INVALID_TIMEZONE: "E100006",
	//비밀번호와 비밀번호확인이 일치하지 않습니다.
	PASSWORD_CONFIRM_MISMATCH: "E100007"
} as const

// eslint-disable-next-line @typescript-eslint/no-redeclare
export type ServiceErrorCode = (typeof ServiceErrorCode)[keyof typeof ServiceErrorCode]
