import { Navigate } from 'react-router-dom'
import { isAuthenticated } from '../modules/auth/services/auth.service'
import type { ReactNode } from 'react'

interface Props {
  children: ReactNode
}

const ProtectedRoute = ({ children }: Props) => {
  if (!isAuthenticated()) {
    return <Navigate to="/login" replace />
  }

  return <>{children}</>
}

export default ProtectedRoute