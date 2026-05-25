import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'
import { login, saveSession } from '../services/auth.service'
import type { LoginRequest } from '../types/auth.types'
import './LoginPage.css'

const LoginPage = () => {
  const [errorMsg, setErrorMsg] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginRequest>()

  const onSubmit = async (data: LoginRequest) => {
    try {
      setLoading(true)
      setErrorMsg(null)
      const response = await login(data)
      saveSession(response.data, data.email)
      navigate('/cars')
    } catch (error: unknown) {
      const err = error as { response?: { data?: { message?: string } } }
      setErrorMsg(err.response?.data?.message || 'Invalid credentials. Please try again.')
      setTimeout(() => setErrorMsg(null), 5000)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="login-wrapper">
      <div className="login-card">

        {/* Header */}
        <div className="login-header">
          <div className="login-logo">✦</div>
          <h1 className="login-title">Car Manager</h1>
          <p className="login-subtitle">Sign in to your account</p>
        </div>

        {/* Form */}
        <form className="login-form" onSubmit={handleSubmit(onSubmit)}>

          {/* Email */}
          <div className="form-group">
            <label className="form-label">Email</label>
            <input
              className={`form-input ${errors.email ? 'form-input--error' : ''}`}
              type="email"
              placeholder="you@example.com"
              {...register('email', {
                required: 'Email is required',
                pattern: {
                  value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
                  message: 'Enter a valid email',
                },
              })}
            />
            {errors.email && (
              <span className="form-error">{errors.email.message}</span>
            )}
          </div>

          {/* Password */}
          <div className="form-group">
            <label className="form-label">Password</label>
            <input
              className={`form-input ${errors.password ? 'form-input--error' : ''}`}
              type="password"
              placeholder="••••••••"
              {...register('password', {
                required: 'Password is required',
                minLength: {
                  value: 6,
                  message: 'Password must be at least 6 characters',
                },
              })}
            />
            {errors.password && (
              <span className="form-error">{errors.password.message}</span>
            )}
          </div>

          {/* Error message */}
          {errorMsg && (
            <div className="form-alert">
              {errorMsg}
            </div>
          )}

          {/* Submit */}
          <button
            className={`login-btn ${loading ? 'login-btn--loading' : ''}`}
            type="submit"
            disabled={loading}
          >
            {loading ? 'Signing in...' : 'Sign in'}
          </button>

        </form>
      </div>
    </div>
  )
}

export default LoginPage