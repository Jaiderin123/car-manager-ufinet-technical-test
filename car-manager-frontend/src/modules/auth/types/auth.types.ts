// Request
export interface LoginRequest {
  email: string
  password: string
}

// Backend response
export interface AuthResponse {
  data: string // JWT token
  status: number
  message: string
  timestamp: string
}

// User stored in localStorage
export interface User {
  email: string
}