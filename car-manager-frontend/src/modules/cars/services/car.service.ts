import http from '../../../shared/services/http'
import type { Car, CarRequest, ApiResponse } from '../types/car.types'

// Get all cars for authenticated user
export const getAllCars = async (): Promise<Car[]> => {
  const response = await http.get<ApiResponse<Car[]>>('/car/')
  return response.data.data
}

// Create a new car
export const createCar = async (data: CarRequest): Promise<Car> => {
  const response = await http.post<ApiResponse<Car>>('/car/', data)
  return response.data.data
}

// Update an existing car
export const updateCar = async (carId: number, data: CarRequest): Promise<Car> => {
  const response = await http.put<ApiResponse<Car>>(`/car/${carId}`, data)
  return response.data.data
}

// Toggle car active status
export const toggleCarStatus = async (carId: number): Promise<void> => {
  await http.patch(`/car/${carId}`)
}

// Delete a car
export const deleteCar = async (carId: number): Promise<void> => {
  await http.delete(`/car/${carId}`)
}