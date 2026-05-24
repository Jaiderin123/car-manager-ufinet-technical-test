import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getAllCars, createCar, updateCar, deleteCar, toggleCarStatus } from '../services/car.service'
import { logout } from '../../auth/services/auth.service'
import CarModal from '../components/CarModal'
import type { Car, CarRequest } from '../types/car.types'
import './CarsPage.css'

const CarsPage = () => {
  const [cars, setCars] = useState<Car[]>([])
  const [loading, setLoading] = useState(true)
  const [modalOpen, setModalOpen] = useState(false)
  const [selectedCar, setSelectedCar] = useState<Car | null>(null)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [search, setSearch] = useState('')
  const navigate = useNavigate()

  // Fetch cars on mount
  useEffect(() => {
    const loadCars = async () => {
      try {
        setLoading(true)
        const data = await getAllCars()
        setCars(data)
      } catch {
        setError('Failed to load cars. Please try again.')
      } finally {
        setLoading(false)
      }
    }

    loadCars()
  }, [])

  // Open modal for create
  const handleCreate = () => {
    setSelectedCar(null)
    setModalOpen(true)
  }

  // Open modal for edit
  const handleEdit = (car: Car) => {
    setSelectedCar(car)
    setModalOpen(true)
  }

  const handleCloseModal = () => {
    setModalOpen(false)
    setSelectedCar(null)
  }

  // Create or update car
  const handleSubmit = async (data: CarRequest) => {
    try {
      setSaving(true)
      if (selectedCar) {
        const updated = await updateCar(selectedCar.carId, data)
        setCars((prev) => prev.map((c) => (c.carId === updated.carId ? updated : c)))
      } else {
        const created = await createCar(data)
        setCars((prev) => [...prev, created])
      }
      handleCloseModal()
    } catch {
      setError('Failed to save car. Please try again.')
    } finally {
      setSaving(false)
    }
  }

  // Toggle active status
  const handleToggleStatus = async (car: Car) => {
    try {
      await toggleCarStatus(car.carId)
      setCars((prev) =>
        prev.map((c) => (c.carId === car.carId ? { ...c, isActive: !c.isActive } : c))
      )
    } catch {
      setError('Failed to update status.')
    }
  }

  // Delete car
  const handleDelete = async (carId: number) => {
    if (!confirm('Are you sure you want to delete this car?')) return
    try {
      await deleteCar(carId)
      setCars((prev) => prev.filter((c) => c.carId !== carId))
    } catch {
      setError('Failed to delete car.')
    }
  }

  // Logout
  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  // Filter cars by search
  const filtered = cars.filter((car) => {
    const q = search.toLowerCase()
    return (
      car.brand.toLowerCase().includes(q) ||
      car.model.toLowerCase().includes(q) ||
      car.plate.toLowerCase().includes(q)
    )
  })

  return (
    <div className="cars-wrapper">

      {/* Navbar */}
      <header className="cars-navbar">
        <div className="navbar-brand">
          <span className="navbar-logo">✦</span>
          <span className="navbar-title">Car Manager</span>
        </div>
        <button className="btn-logout" onClick={handleLogout}>
          Sign out
        </button>
      </header>

      {/* Main content */}
      <main className="cars-main">

        {/* Page header */}
        <div className="cars-header">
          <div>
            <h1 className="cars-title">My Cars</h1>
            <p className="cars-subtitle">{cars.length} vehicle{cars.length !== 1 ? 's' : ''} registered</p>
          </div>
          <button className="btn-add" onClick={handleCreate}>
            + Add Car
          </button>
        </div>

        {/* Search */}
        <div className="cars-search">
          <input
            className="form-input search-input"
            type="text"
            placeholder="Search by brand, model or plate..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>

        {/* Error */}
        {error && (
          <div className="cars-alert">
            {error}
            <button onClick={() => setError(null)}>✕</button>
          </div>
        )}

        {/* Table */}
        <div className="cars-table-wrapper">
          {loading ? (
            <div className="cars-loading">Loading vehicles...</div>
          ) : filtered.length === 0 ? (
            <div className="cars-empty">
              {search ? 'No results found.' : 'No cars registered yet.'}
            </div>
          ) : (
            <table className="cars-table">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Brand</th>
                  <th>Model</th>
                  <th>Year</th>
                  <th>Plate</th>
                  <th>Color</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((car, index) => (
                  <tr key={car.carId}>
                    <td className="td-index">{index + 1}</td>
                    <td className="td-brand">{car.brand}</td>
                    <td>{car.model}</td>
                    <td>{car.year}</td>
                    <td><span className="badge-plate">{car.plate}</span></td>
                    <td>
                      <span className="color-dot" style={{ backgroundColor: car.color.toLowerCase() }} />
                      {car.color}
                    </td>
                    <td>
                      <button
                        className={`btn-status ${car.isActive ? 'btn-status--active' : 'btn-status--inactive'}`}
                        onClick={() => handleToggleStatus(car)}
                      >
                        {car.isActive ? 'Active' : 'Inactive'}
                      </button>
                    </td>
                    <td>
                      <div className="td-actions">
                        <button className="btn-edit" onClick={() => handleEdit(car)}>
                          Edit
                        </button>
                        <button className="btn-delete" onClick={() => handleDelete(car.carId)}>
                          Delete
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </main>

      {/* Modal */}
      <CarModal
        isOpen={modalOpen}
        onClose={handleCloseModal}
        onSubmit={handleSubmit}
        car={selectedCar}
        loading={saving}
      />
    </div>
  )
}

export default CarsPage