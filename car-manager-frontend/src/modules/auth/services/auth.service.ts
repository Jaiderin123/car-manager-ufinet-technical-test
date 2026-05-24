import http from '../../../shared/services/http'
import type { LoginRequest, AuthResponse } from '../types/auth.types'

// Login user and store token
export const login = async (credentials: LoginRequest): Promise<AuthResponse> => {
  const response = await http.post<AuthResponse>('/auth/login', credentials)
  return response.data
}

// Save token and user to localStorage
export const saveSession = (token: string, email: string): void => {
  localStorage.setItem('token', token)
  localStorage.setItem('user', JSON.stringify({ email }))
}

// Clear session from localStorage
export const logout = (): void => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
}

// Check if user is authenticated
export const isAuthenticated = (): boolean => {
  return !!localStorage.getItem('token')
}