# 🚗 Car Manager — Backend

API REST para la gestión de vehículos por usuario, construida con **Spring Boot 3.5**, arquitectura **hexagonal (puertos y adaptadores)**, autenticación **JWT** y base de datos **SQL Server** con **R2DBC reactivo**.

---

## 📐 Arquitectura

El proyecto implementa **Arquitectura Hexagonal (Ports & Adapters)** con separación estricta de responsabilidades:

```
com.ufinet.carmanager/
├── application/               # Casos de uso — orquesta el dominio
│   └── usecase/
│       ├── auth/              # LoginAppUserService
│       └── cars/              # CreateCarByUserService, UpdateCarByUserService...
├── domain/                    # Núcleo del negocio — sin dependencias externas
│   ├── auth.ports/            # Puertos de autenticación
│   ├── car/
│   │   ├── model/             # Car (record de dominio)
│   │   └── ports/
│   │       ├── in/            # Casos de uso (interfaces)
│   │       └── out/           # Repositorios (interfaces)
│   ├── shared/
│   │   ├── exceptions/        # BusinessException, InvalidCredentialsException...
│   │   └── utils/             # ValidateDate, DateUtils
│   └── user/
│       ├── model/             # AppUser
│       └── ports/
├── infrastructure/            # Adaptadores — implementaciones concretas
│   ├── adapters.persistence/
│   │   ├── car/               # CarEntity, CarMapper, CarR2dbcRepository, CarR2dbcAdapter
│   │   └── user/              # UserEntity, UserMapper, UserR2dbcAdapter
│   ├── config/
│   │   └── security/          # SecurityConfig, JwtUtil, JwtAuthenticationFilter...
│   └── entrypoints/
│       ├── auth/              # AuthHandler, AuthRouterConfig
│       ├── car/               # CarHandler, CarRouterConfig
│       ├── config/            # ApiResponse, validators
│       └── exceptions/        # GlobalExceptionHandler
```

### Principios aplicados
- **Hexagonal Architecture** — el dominio no conoce Spring, R2DBC ni ningún framework
- **SOLID** — Single Responsibility, Open/Closed, Dependency Inversion
- **KISS** — servicios simples con una sola responsabilidad
- **Reactive Programming** — WebFlux + R2DBC para I/O no bloqueante

---

## 🛠️ Tecnologías

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 21 | Lenguaje principal |
| Spring Boot | 3.5.14 | Framework base |
| Spring WebFlux | 3.5.14 | API REST reactiva |
| Spring Security | 3.5.14 | Autenticación y autorización |
| Spring Data R2DBC | 3.5.14 | Persistencia reactiva |
| SQL Server | 2022 | Base de datos |
| JWT (jjwt) | — | Tokens de autenticación |
| MapStruct | — | Mapeo entre capas |
| Lombok | — | Reducción de boilerplate |
| Docker | — | Contenedores |
| Gradle | — | Gestión de dependencias |

---

## 📋 Requisitos previos

- **Docker** y **Docker Compose** instalados
- **Java 21** (solo para desarrollo local sin Docker)
- **Gradle** (incluido via wrapper `./gradlew`)

---

## 🚀 Cómo correr el proyecto

### Opción 1 — Docker Compose (recomendado)

Desde la **raíz del repositorio** (donde está el `docker-compose.yaml`):

```bash
# 1. Levantar todos los servicios (SQL Server + inicialización BD + Backend)
sudo docker compose up --build

# 2. Verificar que los contenedores estén corriendo
docker ps
```

Los servicios que se levantan:
- `unitet-cars-db` — SQL Server 2022 en el puerto `1433`
- `unitet-cars-db-init` — ejecuta el script `init.sql` una sola vez
- `unitet-cars-backend` — API REST en el puerto `8080`

> ⚠️ El backend espera a que la BD esté saludable y el script de init haya terminado antes de arrancar.

### Opción 2 — Desarrollo local (IntelliJ / VS Code)

```bash
# 1. Levantar solo la base de datos
sudo docker compose up sqlserver-db db-init

# 2. Configurar variables de entorno locales
# Editar car-manager-backend/src/main/resources/application.yml
# Cambiar las variables ${...} por valores directos:
#   DATABASE_ENDPOINT: localhost:1433
#   DATABASE_NAME: car_manager_db
#   DATABASE_USERNAME: sa
#   DATABASE_PASSWORD: Password123!
#   JWT_SECRET: CR0zMxXVfqMzeCsQJcEnJjhkphcScyueBh3b8Eq6l6S
#   APP_BASE_PATH: /api/v1

# 3. Correr el backend
cd car-manager-backend
./gradlew bootRun
```

### Detener los servicios

```bash
# Detener sin eliminar datos
sudo docker compose down

# Detener y eliminar volúmenes (resetea la BD)
sudo docker compose down -v
```

---

## 🗄️ Base de datos

### Credenciales

| Campo | Valor |
|---|---|
| Host | `localhost:1433` |
| Base de datos | `car_manager_db` |
| Usuario | `sa` |
| Contraseña | `Password123!` |

### Script de inicialización

El archivo `car-manager-backend/local-env/sqlserver/init.sql` crea automáticamente:
- Tabla `users` con constraints de unicidad en email
- Tabla `cars` con FK hacia `users`, constraint de formato de placa colombiana (`ABC-123`) y validación de año
- Índices optimizados para búsqueda por usuario, placa y marca/año
- Datos de prueba: 3 usuarios y 8 vehículos

### Usuarios de prueba

| Email | Contraseña | Descripción |
|---|---|---|
| `demo@unitet.com` | `Admin123!` | Usuario demo con 3 carros |
| `john.doe@unitet.com` | `Admin123!` | Usuario con 2 carros |
| `maria.lopez@unitet.com` | `Admin123!` | Usuario con 3 carros |

---

## 🔐 Autenticación

El sistema usa **JWT Bearer Token**:

1. Hacer login con email y contraseña
2. Guardar el token recibido en `data`
3. Incluir el token en cada petición protegida:
   ```
   Authorization: Bearer <token>
   ```

El token contiene el `userId` y expira en 1 hora.

---

## 📡 Endpoints

### Base URL
```
http://localhost:8080/api/v1
```

### Auth

| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| `POST` | `/auth/login` | Iniciar sesión | ❌ |

**Login — Request:**
```json
{
    "email": "demo@unitet.com",
    "password": "Admin123!"
}
```

**Login — Response:**
```json
{
    "data": "eyJhbGciOiJIUzI1NiJ9...",
    "status": 200,
    "message": "User logged successfully",
    "timestamp": "2026-05-24T10:00:00"
}
```

---

### Cars

Todos los endpoints requieren `Authorization: Bearer <token>`

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/car/` | Listar todos los carros del usuario autenticado |
| `POST` | `/car/` | Crear un nuevo carro |
| `PUT` | `/car/{carId}` | Actualizar un carro existente |
| `PATCH` | `/car/{carId}` | Alternar estado activo/inactivo del carro |
| `DELETE` | `/car/{carId}` | Eliminar un carro |

**Crear / Actualizar carro — Request:**
```json
{
    "brand": "Toyota",
    "model": "Corolla",
    "year": 2021,
    "plate": "TYT-999",
    "color": "Black",
    "photoUrl": null
}
```

**Validaciones del backend:**
- `brand`, `model`, `color` — requeridos, máximo 50 caracteres
- `plate` — requerido, formato colombiano `AAA-999` (3 letras + guion + 3 números)
- `year` — mínimo 1900, máximo año actual + 1
- `photoUrl` — opcional, máximo 500 caracteres

**Formato de respuesta estándar:**
```json
{
    "data": { ... },
    "status": 200,
    "message": "...",
    "timestamp": "2026-05-24T10:00:00"
}
```

---

## 🔒 Seguridad

- Contraseñas hasheadas con **BCrypt** (cost factor 10)
- Tokens **JWT** firmados con clave secreta configurable
- Mensajes de error de autenticación genéricos (no revela si el email existe)
- Validación de pertenencia: cada usuario solo puede ver/editar/eliminar sus propios carros
- Endpoints públicos: solo `/auth/login`
- El backend corre como usuario **no-root** en Docker

---

## ⚙️ Variables de entorno

| Variable | Descripción | Ejemplo |
|---|---|---|
| `DATABASE_ENDPOINT` | Host y puerto de SQL Server | `sqlserver-db:1433` |
| `DATABASE_NAME` | Nombre de la base de datos | `car_manager_db` |
| `DATABASE_USERNAME` | Usuario de la BD | `sa` |
| `DATABASE_PASSWORD` | Contraseña de la BD | `Password123!` |
| `JWT_SECRET` | Clave secreta para firmar JWT | `CR0zMxXVfq...` |
| `APP_BASE_PATH` | Prefijo base de la API | `/api/v1` |

---

## 🧪 Colección Postman

Se incluye la colección `Michael Page - Ufinet.postman_collection.json` en la raíz del repositorio.

El script de test del endpoint de Login guarda automáticamente el token JWT en la variable de colección `jwt_auth_token`, que es usada por todos los demás endpoints automáticamente.

**Para importar:**
1. Abrir Postman
2. `Import` → seleccionar el archivo `.json`
3. Hacer Login primero
4. El token se guarda automáticamente ✅

---

## 📁 Estructura del repositorio

```
car-manager-ufinet-technical-test/
├── docker-compose.yaml
├── car-manager-backend/
│   ├── Dockerfile
│   ├── build.gradle
│   ├── local-env/
│   │   └── sqlserver/
│   │       └── init.sql          # Script de creación e inicialización de BD
│   └── src/
│       └── main/
│           ├── java/             # Código fuente (arquitectura hexagonal)
│           └── resources/
│               └── application.yml
└── car-manager-frontend/         # Frontend React (ver README del frontend)
```

---

## 🐛 Solución de problemas comunes

**El backend no conecta a la BD:**
```bash
# Verificar que SQL Server esté saludable
docker logs unitet-cars-db | tail -20

# Verificar que el init script corrió
docker logs unitet-cars-db-init
```

**Error de contraseña SQL Server:**
> La contraseña debe tener mayúscula, minúscula, número y símbolo.
> `Password123!` cumple todos los requisitos.

**Limpiar todo y empezar de cero:**
```bash
sudo docker system prune -a --volumes -f
sudo docker compose up --build
```

**Puerto 8080 ocupado:**
```bash
# Ver qué proceso usa el puerto
sudo lsof -i :8080
# Matar el proceso
sudo kill -9 <PID>
```

---

## Autor

**Jaider Betancur**
📧 [torrezjaider10@gmail.com](mailto:torrezjaider10@gmail.com)

© 2026 Jaider Betancur. Prueba Técnica — Ufinet.