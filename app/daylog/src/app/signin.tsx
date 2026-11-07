import useSigninViewModel from "@/viewmodel/account/useSigninViewModel"
import { Button, Image, StyleSheet, Text, TextInput, View } from "react-native"

export default function Signin() {
	const { isProceedSignin, email, setEmail, password, setPassword, doSignin, signinError } =
		useSigninViewModel()

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
		<View>
			<Image
				source={require("@assets/images/daylog_logo.png")}
				style={{ width: 300, height: 300 }}
			/>
			<Text>이메일</Text>
			<TextInput
				value={email}
				onChangeText={setEmail}
				style={signinError ? styles.errorTextInput : styles.textInput}
			/>
			<Text>비밀번호</Text>
			<TextInput
				secureTextEntry={true}
				value={password}
				onChangeText={setPassword}
				style={signinError ? styles.errorTextInput : styles.textInput}
			/>
			{signinError && <Text style={styles.errorText}>이메일 또는 비밀번호가 틀립니다.</Text>}
			<Button title="로그인" onPress={doSignin} disabled={isProceedSignin} />
		</View>
	)
}
