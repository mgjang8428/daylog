// SecureStore keys
export const ACCESS_TOKEN_STORED_KEY = "accessToken"
export const REFRESH_TOKEN_STORED_KEY = "refreshToken"

// API
export const API_TIMEOUT: number = Number(process.env.EXPO_PUBLIC_API_TIMEOUT)
export const API_BASE_URL = process.env.EXPO_PUBLIC_API_BASE_URL

// TOKEN
export const TOKEN_TYPE = "Bearer"
export const TOKEN_ISSUER = process.env.EXPO_PUBLIC_TOKEN_ISSUER
