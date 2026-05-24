// Single car entity
export interface Car {
  carId: number
  brand: string
  model: string
  year: number
  plate: string
  color: string
  photoUrl: string | null
  isActive: boolean
  createdAt: string
}

// Request to create or update a car
export interface CarRequest {
  brand: string
  model: string
  year: number
  plate: string
  color: string
  photoUrl: string | null
}

// Generic backend response wrapper
export interface ApiResponse<T> {
  data: T
  status: number
  message: string
  timestamp: string
}