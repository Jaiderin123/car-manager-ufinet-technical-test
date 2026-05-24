# ✦ Car Manager — Frontend

Interfaz web para la gestión de vehículos por usuario, construida con **React 18**, **TypeScript** y **Vite**. Incluye autenticación JWT, rutas protegidas y un CRUD completo de autos.

---

## 📋 Tabla de Contenidos

- [Requisitos previos](#requisitos-previos)
- [Dependencias del proyecto](#dependencias-del-proyecto)
- [Levantar servicios externos](#levantar-servicios-externos)
- [Instalación y ejecución](#instalación-y-ejecución)
- [Estructura de carpetas](#estructura-de-carpetas)
- [Módulos](#módulos)
- [Variables de configuración](#variables-de-configuración)
- [Funcionalidades](#funcionalidades)

---

## Requisitos previos

- **Node.js 18+**
- **npm 9+**
- La **base de datos** y el **backend** deben estar activos antes de iniciar el frontend. Levántalos desde la raíz del repositorio con:

```bash
docker-compose up -d
```

> Esto iniciará SQL Server y el backend de Spring Boot automáticamente.

---

## Dependencias del proyecto

| Librería            | Uso                                         |
|---------------------|---------------------------------------------|
| React 18            | Librería principal de UI                    |
| TypeScript          | Tipado estático                             |
| Vite                | Bundler y servidor de desarrollo            |
| React Router DOM v6 | Enrutamiento y rutas protegidas             |
| Axios               | Cliente HTTP con interceptores              |
| React Hook Form     | Manejo y validación de formularios          |

---

## Levantar servicios externos

Antes de correr el frontend, asegúrate de que los servicios estén activos:

```bash
# Desde la raíz del repositorio
docker-compose up -d
```

Verifica que el backend responda en: `http://localhost:8080/api/v1`

---

## Instalación y ejecución

```bash
# 1. Entrar a la carpeta del frontend
cd car-manager-frontend

# 2. Instalar dependencias
npm install

# 3. Iniciar el servidor de desarrollo
npm run dev
```

La aplicación estará disponible en: `http://localhost:5173`

---

## Estructura de carpetas

```
src/
├── modules/
│   ├── auth/
│   │   ├── pages/          # LoginPage
│   │   ├── services/       # auth.service.ts (login, logout, sesión)
│   │   └── types/          # auth.types.ts
│   └── cars/
│       ├── components/     # CarModal (crear / editar)
│       ├── pages/          # CarsPage (tabla CRUD)
│       ├── services/       # car.service.ts
│       └── types/          # car.types.ts
├── router/
│   ├── AppRouter.tsx       # Definición de rutas
│   └── ProtectedRoute.tsx  # Guarda de rutas privadas
├── shared/
│   └── services/
│       └── http.ts         # Instancia de Axios con interceptores
└── styles/                 # (reservado para estilos globales adicionales)
```

---

## Módulos

### Auth
- **LoginPage** — formulario de inicio de sesión con validación.
- **auth.service.ts** — maneja login, guardado de token en `localStorage` y logout.
- El token JWT se adjunta automáticamente a cada petición mediante un interceptor de Axios.
- Si el backend retorna `401`, la sesión se limpia y se redirige al login.

### Cars
- **CarsPage** — tabla con todos los autos del usuario autenticado.
- **CarModal** — modal reutilizable para crear y editar autos.
- **car.service.ts** — peticiones al backend: listar, crear, editar, eliminar y cambiar estado.
- Búsqueda en tiempo real por marca, modelo o placa.
- Toggle de estado activo / inactivo por auto.

---

## Variables de configuración

La URL base del backend se configura en:

```
src/shared/services/http.ts
```

```ts
const http = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
})
```

Cambia `baseURL` si el backend corre en un puerto o host diferente.

---

## Funcionalidades

- ✅ Login con JWT y redirección automática
- ✅ Rutas protegidas (redirige al login si no hay sesión)
- ✅ Cierre de sesión con limpieza de `localStorage`
- ✅ Listado de autos del usuario autenticado
- ✅ Crear auto con validaciones de formulario
- ✅ Editar auto desde modal reutilizable
- ✅ Eliminar auto con confirmación
- ✅ Toggle activo / inactivo por auto
- ✅ Búsqueda por marca, modelo o placa
- ✅ Selector de color con indicador visual en tabla
- ✅ Campo simulado de foto del auto (no funcional)
- ✅ Diseño dark, moderno y minimalista

---

## Autor

**Jaider Betancur**
📧 [torrezjaider10@gmail.com](mailto:torrezjaider10@gmail.com)

© 2026 Jaider Betancur. Prueba Técnica — Ufinet.