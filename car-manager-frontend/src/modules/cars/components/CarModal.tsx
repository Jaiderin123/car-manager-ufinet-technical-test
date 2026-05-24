import { useEffect } from "react";
import { useForm } from "react-hook-form";
import type { Car, CarRequest } from "../types/car.types";
import "./CarModal.css";

interface Props {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: CarRequest) => void;
  car?: Car | null;
  loading: boolean;
}

const CarModal = ({ isOpen, onClose, onSubmit, car, loading }: Props) => {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<CarRequest>();

  // Populate form when editing
  useEffect(() => {
    if (car) {
      reset({
        brand: car.brand,
        model: car.model,
        year: car.year,
        plate: car.plate,
        color: car.color,
        photoUrl: car.photoUrl,
      });
    } else {
      reset({
        brand: "",
        model: "",
        year: new Date().getFullYear(),
        plate: "",
        color: "",
        photoUrl: null,
      });
    }
  }, [car, isOpen, reset]);

  if (!isOpen) return null;

  const currentYear = new Date().getFullYear();

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-card" onClick={(e) => e.stopPropagation()}>
        {/* Header */}
        <div className="modal-header">
          <h2 className="modal-title">{car ? "Edit Car" : "Add New Car"}</h2>
          <button className="modal-close" onClick={onClose}>
            ✕
          </button>
        </div>

        {/* Form */}
        <form className="modal-form" onSubmit={handleSubmit(onSubmit)}>
          {/* Brand & Model */}
          <div className="modal-row">
            <div className="form-group">
              <label className="form-label">Brand</label>
              <input
                className={`form-input ${errors.brand ? "form-input--error" : ""}`}
                placeholder="Toyota"
                {...register("brand", { required: "Brand is required" })}
              />
              {errors.brand && (
                <span className="form-error">{errors.brand.message}</span>
              )}
            </div>

            <div className="form-group">
              <label className="form-label">Model</label>
              <input
                className={`form-input ${errors.model ? "form-input--error" : ""}`}
                placeholder="Corolla"
                {...register("model", { required: "Model is required" })}
              />
              {errors.model && (
                <span className="form-error">{errors.model.message}</span>
              )}
            </div>
          </div>

          {/* Year & Color */}
          <div className="modal-row">
            <div className="form-group">
              <label className="form-label">Year</label>
              <input
                className={`form-input ${errors.year ? "form-input--error" : ""}`}
                type="number"
                placeholder="2024"
                {...register("year", {
                  required: "Year is required",
                  min: { value: 1900, message: "Year must be after 1900" },
                  max: {
                    value: currentYear,
                    message: `Year cannot exceed ${currentYear}`,
                  },
                  valueAsNumber: true,
                })}
              />
              {errors.year && (
                <span className="form-error">{errors.year.message}</span>
              )}
            </div>

            <div className="form-group">
              <label className="form-label">Color</label>
              <select
                className={`form-input form-select ${errors.color ? "form-input--error" : ""}`}
                {...register("color", { required: "Color is required" })}
              >
                <option value="">Select a color</option>
                <option value="Black">Black</option>
                <option value="White">White</option>
                <option value="Gray">Gray</option>
                <option value="Silver">Silver</option>
                <option value="Red">Red</option>
                <option value="Blue">Blue</option>
                <option value="Navy">Navy</option>
                <option value="Green">Green</option>
                <option value="Yellow">Yellow</option>
                <option value="Orange">Orange</option>
                <option value="Purple">Purple</option>
                <option value="Brown">Brown</option>
                <option value="Beige">Beige</option>
                <option value="Gold">Gold</option>
                <option value="Pink">Pink</option>
              </select>
              {errors.color && (
                <span className="form-error">{errors.color.message}</span>
              )}
            </div>
          </div>

          {/* Plate */}
          <div className="form-group">
            <label className="form-label">Plate</label>
            <input
              className={`form-input ${errors.plate ? "form-input--error" : ""}`}
              placeholder="ABC-123"
              {...register("plate", {
                required: "Plate is required",
                pattern: {
                  value: /^[A-Z]{2,3}-\d{2,3}$/,
                  message: "Format must be ABC-123 or AB-12",
                },
              })}
            />
            {errors.plate && (
              <span className="form-error">{errors.plate.message}</span>
            )}
          </div>

          {/* Photo URL (simulated) */}
          <div className="form-group">
            <label className="form-label">
              Photo URL <span className="form-label-optional">(optional)</span>
            </label>
            <input
              className="form-input"
              placeholder="https://example.com/photo.jpg"
              {...register("photoUrl")}
            />
          </div>

          {/* Actions */}
          <div className="modal-actions">
            <button
              type="button"
              className="btn-secondary"
              onClick={onClose}
              disabled={loading}
            >
              Cancel
            </button>
            <button
              type="submit"
              className={`btn-primary ${loading ? "btn--loading" : ""}`}
              disabled={loading}
            >
              {loading ? "Saving..." : car ? "Save Changes" : "Add Car"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CarModal;
