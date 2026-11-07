import useSignupViewModel from "@/viewmodel/account/useSignupViewModel"
import { Picker } from "@react-native-picker/picker"
import { useEffect } from "react"
import {
	Button,
	KeyboardAvoidingView,
	ScrollView,
	StyleSheet,
	Text,
	TextInput,
	View
} from "react-native"

export default function Signup() {
	const {
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
		timezoneLocation,
		setTimezoneLocation,
		timezoneAreaList,
		timezoneLocationList,

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
	} = useSignupViewModel()

	useEffect(() => {
		changeTimezoneToDeviceDefault()
	}, [changeTimezoneToDeviceDefault])

	const styles = StyleSheet.create({
		textInput: {
			borderWidth: 1
		},
		errorText: {
			color: "red"
		},
		errorTextInput: {
			borderWidth: 1,
			borderColor: "red"
		},
		picker: {
			borderWidth: 1
		},
		pickerError: {
			borderWidth: 1,
			borderColor: "red"
		}
	})

	return (
		<ScrollView>
			<KeyboardAvoidingView>
				<Text>가입</Text>

				<Text>이메일</Text>
				<TextInput
					value={email}
					onChangeText={setEmail}
					style={
						emailTextError || duplicateEmailError
							? styles.errorTextInput
							: styles.textInput
					}
					maxLength={254}
				/>
				{emailTextError && (
					<Text style={styles.errorText}>
						이메일은 이메일 형식, 5자 이상이여야 합니다.
					</Text>
				)}
				{duplicateEmailError && (
					<Text style={styles.errorText}>해당 이메일은 사용이 불가합니다.</Text>
				)}

				<Text>비밀번호</Text>
				<TextInput
					secureTextEntry={true}
					value={password}
					onChangeText={setPassword}
					style={
						passwordTextError || passwordConfirmError
							? styles.errorTextInput
							: styles.textInput
					}
					maxLength={64}
				/>
				{passwordTextError && (
					<Text style={styles.errorText}>비밀번호는 8자 이상이여야합니다.</Text>
				)}
				<Text>비밀번호 확인</Text>
				<TextInput
					secureTextEntry={true}
					value={passwordConfirm}
					onChangeText={setPasswordConfirm}
					style={
						passwordConfirmTextError || passwordConfirmError
							? styles.errorTextInput
							: styles.textInput
					}
					maxLength={64}
				/>
				{passwordConfirmTextError && (
					<Text style={styles.errorText}>비밀번호는 8자 이상이여야합니다.</Text>
				)}
				{passwordConfirmError && (
					<Text style={styles.errorText}>비밀번호가 일치하지 않습니다.</Text>
				)}

				<Text>닉네임</Text>
				<TextInput
					value={nickname}
					onChangeText={setNickname}
					style={
						nicknameTextError || duplicateNicknameError
							? styles.errorTextInput
							: styles.textInput
					}
					maxLength={30}
				/>
				{nicknameTextError && (
					<Text style={styles.errorText}>닉네임은 2자 이상이여야합니다.</Text>
				)}
				{duplicateNicknameError && (
					<Text style={styles.errorText}>해당 닉네임은 사용이 불가합니다.</Text>
				)}

				<Text>타임존</Text>
				<Text>Area</Text>
				<View style={timezoneSelectError ? styles.pickerError : styles.picker}>
					<Picker
						selectedValue={timezoneArea}
						onValueChange={(area) => changeTimezoneArea(area)}
					>
						<Picker.Item label={""} value={""} key={"timezone_area_null"} />
						{timezoneAreaList.map((area: string) => (
							<Picker.Item label={area} value={area} key={"timezone_area" + area} />
						))}
					</Picker>
				</View>
				<Text>Location</Text>
				<View style={timezoneSelectError ? styles.pickerError : styles.picker}>
					<Picker
						selectedValue={timezoneLocation}
						onValueChange={(location) => setTimezoneLocation(location)}
						enabled={timezoneArea === "" ? false : true}
					>
						<Picker.Item label={""} value={""} key={"timezone_location_null"} />
						{timezoneLocationList.map((area: string) => (
							<Picker.Item label={area} value={area} key={"timezone_area" + area} />
						))}
					</Picker>
				</View>
				{timezoneSelectError && (
					<Text style={styles.errorText}>타임존을 선택해야합니다.</Text>
				)}

				<Button title="가입" onPress={doSignUp} disabled={isProceedSignup} />
			</KeyboardAvoidingView>
		</ScrollView>
	)
}
