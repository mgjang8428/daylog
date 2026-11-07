import { useSigninStore } from "@/model/store/signinStore"
import { Stack } from "expo-router"
import { useEffect } from "react"
import { SafeAreaView } from "react-native-safe-area-context"

export default function RootLayout() {
	const { storeRestore, storeIsSignin, storeIsLoading } = useSigninStore()

	useEffect(() => {
		storeRestore()
	}, [storeRestore])

	if (storeIsLoading) return null

	return (
		<SafeAreaView style={{ flex: 1 }} edges={["bottom", "left", "right"]}>
			<Stack
				screenOptions={{
					headerTitle: "",
					headerBackButtonDisplayMode: "minimal",
					headerShadowVisible: false
				}}
			>
				<Stack.Protected guard={!storeIsSignin}>
					<Stack.Screen name="index" />
					<Stack.Screen name="signin" />
					<Stack.Screen name="signup" />
				</Stack.Protected>
				<Stack.Protected guard={storeIsSignin} redirectTo={"/"}>
					<Stack.Screen name="(tabs)" />
				</Stack.Protected>
			</Stack>
		</SafeAreaView>
	)
}
