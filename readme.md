# ✦ Car Manager — Prueba Técnica Ufinet

Aplicación full-stack para registrar y gestionar vehículos por usuario. Incluye autenticación JWT, CRUD completo de autos y una interfaz moderna dark.

---

## 🗂️ Estructura del repositorio

```
car-manager-ufinet-technical-test/
├── docker-compose.yaml           # Orquestación de todos los servicios
├── Michael Page - Ufinet.postman_collection.json
├── car-manager-backend/          # API REST — Spring Boot + WebFlux + SQL Server
│   └── README.md                 # Documentación detallada del backend
└── car-manager-frontend/         # SPA — React 18 + TypeScript + Vite
    └── README.md                 # Documentación detallada del frontend
```

---

## ⚡ Inicio rápido

### Requisitos

- **Docker** y **Docker Compose** instalados

### 1. Levantar base de datos y backend

Desde la raíz del repositorio:

```bash
sudo docker compose up --build
```

Esto levanta automáticamente:

| Servicio | Descripción | Puerto |
|---|---|---|
| `unitet-cars-db` | SQL Server 2022 | `1433` |
| `unitet-cars-db-init` | Crea tablas y datos de prueba | — |
| `unitet-cars-backend` | API REST Spring Boot | `8080` |
| `unitet-cars-frontend` | SPA React (Compilada y servida con Nginx) | `3000` |


> El backend espera a que la base de datos esté lista antes de arrancar y el frontend espera a que el backend haya iniciado para poder arrancar. El script de inicialización corre automáticamente.

---

## 🔑 Credenciales de prueba

| Campo | Valor |
|---|---|
| Email | `demo@unitet.com` |
| Contraseña | `Admin123!` |

---

## 🛠️ Stack tecnológico

| Capa | Tecnología |
|---|---|
| Frontend | React 19, TypeScript, Vite, React Router, Axios |
| Backend | Java 21, Spring Boot 3.5, WebFlux, Spring Security |
| Auth | JWT (Bearer Token) |
| Base de datos | SQL Server 2022, R2DBC reactivo |
| Infraestructura | Docker, Docker Compose |

---

## 📡 API

Base URL: `http://localhost:8080/api/v1`

| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| `POST` | `/auth/login` | Iniciar sesión | ❌ |
| `GET` | `/car/` | Listar autos del usuario | ✅ |
| `POST` | `/car/` | Crear auto | ✅ |
| `PUT` | `/car/{id}` | Actualizar auto | ✅ |
| `PATCH` | `/car/{id}` | Activar / desactivar auto | ✅ |
| `DELETE` | `/car/{id}` | Eliminar auto | ✅ |

La colección de Postman `Michael Page - Ufinet.postman_collection.json` incluye todos los endpoints listos para usar. El token JWT se guarda automáticamente tras el login.

---

## 🧹 Detener los servicios

```bash
# Detener sin borrar datos
sudo docker compose down

# Detener y resetear la base de datos
sudo docker compose down -v
```

---

> Para más detalles de cada capa, consulta el `README.md` dentro de cada carpeta.

---

## Autor

**Jaider Betancur**
📧 [torrezjaider10@gmail.com](mailto:torrezjaider10@gmail.com)

© 2026 Jaider Betancur. Prueba Técnica — Ufinet.