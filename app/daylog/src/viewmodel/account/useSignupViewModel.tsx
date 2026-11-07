import { ServiceErrorCode } from "@/model/common/serviceErrorCode"
import {
	getDeviceTimezone,
	getTimezoneAreas,
	getTimezoneLocations,
	TimezoneSelection
} from "@/model/util/timezone"
import SignupRequest from "@/model/v1/account/dto/request/signupRequest"
import AccountService from "@/model/v1/account/service/accountService"
import ResponseV1 from "@/model/v1/dto/responseV1"
import { AxiosError } from "axios"
import { router } from "expo-router"
import { useState } from "react"

const accountService = new AccountService()

export default function useSignupViewModel() {
	const [isProceedSignup, setIsProceedSignup] = useState(false)

	const [email, setEmail] = useState("")
	const [password, setPassword] = useState("")
	const [passwordConfirm, setPasswordConfirm] = useState("")
	const [nickname, setNickname] = useState("")

	const [timezoneArea, setTimezoneArea] = useState("")
	const [timezoneLocation, setTimezoneLocation] = useState("")
	const [timezoneAreaList, setTimezoneAreaList] = useState(getTimezoneAreas())
	const [timezoneLocationList, setTimezoneLocationList] = useState([""])

	const [emailTextError, setEmailTextError] = useState(false)
	const [passwordTextError, setPasswordTextError] = useState(false)
	const [passwordConfirmTextError, setPasswordConfirmTextError] = useState(false)
	const [passwordConfirmError, setPasswordConfirmError] = useState(false)
	const [nicknameTextError, setNicknameTextError] = useState(false)
	const [timezoneSelectError, setTimezoneSelectError] = useState(false)

	const [duplicateEmailError, setDuplicateEmailError] = useState(false)
	const [duplicateNicknameError, setDuplicateNicknameError] = useState(false)

	function changeTimezoneArea(area: string) {
		setTimezoneArea(area)
		setTimezoneLocation("")
		setTimezoneLocationList(getTimezoneLocations(area))
	}

	function changeTimezoneToDeviceDefault() {
		const deviceTimezone: TimezoneSelection | null = getDeviceTimezone()
		if (deviceTimezone === null) return
		changeTimezoneArea(deviceTimezone.area)
		setTimezoneLocation(deviceTimezone.location)
	}

	function clientInputTextErrorValidate(): boolean {
		// error 상태 초기화
		initErrorState()

		let validateResult = true

		// 이메일 입력 확인
		if (email === "" || email.length < 5) {
			setEmailTextError(true)
			validateResult = false
		}

		// 이메일 형식 확인
		const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/
		if (!emailRegex.test(email.trim())) {
			setEmailTextError(true)
			validateResult = false
		}

		// 비밀번호 입력 확인
		if (password === "" || password.length < 8) {
			setPasswordTextError(true)
			validateResult = false
		}

		// 비밀번호 확인 입력 확인
		if (passwordConfirm === "" || passwordConfirm.length < 8) {
			setPasswordConfirmTextError(true)
			validateResult = false
		}

		// password 일치 여부 확인
		if (password !== passwordConfirm) {
			setPasswordConfirmError(true)
			validateResult = false
		}

		// 닉네임 입력 확인
		if (nickname === "" || nickname.length < 2) {
			setNicknameTextError(true)
			validateResult = false
		}

		// 타임존 선택 확인
		if (timezoneArea === "" || timezoneLocation === "") {
			setTimezoneSelectError(true)
			validateResult = false
		}

		return validateResult
	}

	function serverInputTextErrorValidate(errorCode: string) {
		initErrorState()

		// 이메일 중복 가입 불가
		if (errorCode === ServiceErrorCode.DUPLICATE_EMAIL) {
			setDuplicateEmailError(true)
		}

		// 닉네임 중복 가입 불가
		if (errorCode === ServiceErrorCode.DUPLICATE_NICKNAME) {
			setDuplicateNicknameError(true)
		}
	}

	function initErrorState() {
		setEmailTextError(false)
		setPasswordTextError(false)
		setPasswordConfirmTextError(false)
		setPasswordConfirmError(false)
		setNicknameTextError(false)
		setTimezoneSelectError(false)

		setDuplicateEmailError(false)
		setDuplicateNicknameError(false)
	}

	async function doSignUp() {
		if (isProceedSignup) return
		if (!clientInputTextErrorValidate()) return
		setIsProceedSignup(true)

		// request 객체 생성
		const request: SignupRequest = {
			email: email,
			password: password,
			passwordConfirm: passwordConfirm,
			nickname: nickname,
			timezone: `${timezoneArea}/${timezoneLocation}`
		}
		await accountService
			.signup(request)
			.then(() => {
				alert("가입 성공")
				router.replace("/")
			})
			.catch((error: AxiosError<ResponseV1<void>>) => {
				const errorCode = error.response?.data.errorCode
				if (errorCode != null) {
					serverInputTextErrorValidate(errorCode)
				} else {
					alert("서버 에러")
				}
			})
			.finally(() => {
				setIsProceedSignup(false)
			})
	}

	return {
		isProceedSignup,

		email,
		setEmail,
		password,
		setPassword,
		passwordConfirm,
		setPasswordConfirm,
		nickname,
		setNickname,

		timezoneArea,
		setTimezoneArea,
		timezoneLocation,
		setTimezoneLocation,
		timezoneAreaList,
		setTimezoneAreaList,
		timezoneLocationList,
		setTimezoneLocationList,

		emailTextError,
		passwordTextError,
		passwordConfirmTextError,
		passwordConfirmError,
		nicknameTextError,
		timezoneSelectError,

		duplicateEmailError,
		duplicateNicknameError,

		doSignUp,
		changeTimezoneArea,
		changeTimezoneToDeviceDefault
	}
}
