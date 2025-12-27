# Vue Store

Full-stack e-commerce demo with Vue 3 (frontend) and Spring Boot (backend).

## Stack
- Frontend: Vue 3, Vite, TypeScript, Pinia, Vue Router
- Backend: Java 17, Spring Boot 3, Spring Security, JPA, JWT
- DB: MySQL (Docker optional)

## Project Structure
- `frontend/` — SPA client
- `backend/` — REST API

## Requirements
- Node.js 18+
- Java 17 (JDK)
- Maven (or use `mvnw` if you add it)
- MySQL (local or Docker)

## Quick Start

### 1) Start MySQL (Docker)
```powershell
cd backend
docker compose up -d
```

### 2) Run Backend
```powershell
cd backend
mvn spring-boot:run
```

Backend runs at `http://localhost:8080`.

### 3) Run Frontend
```powershell
cd frontend
npm install
npm run dev
```

Frontend runs at `http://localhost:3000`.

## Environment
Backend (`backend/src/main/resources/application.yml`):
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- `JWT_SECRET`
- `CORS_ORIGINS`
- `UPLOAD_DIR`
- `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`
- `SUPPORT_EMAIL_TO`

Frontend (`frontend/.env`):
- `VITE_API_BASE_URL` (default `http://localhost:8080/api`)

## Admin
Default admin user:
- email: `admin@vue-store.local`
- password: `admin123`

Admin panel: `http://localhost:3000/admin`

## Support Email
Support mail is sent to:
- `dr.xlson@yandex.ru`

Configure SMTP via env vars:
```powershell
$env:MAIL_HOST="smtp.yandex.ru"
$env:MAIL_PORT="587"
$env:MAIL_USERNAME="dr.xlson@yandex.ru"
$env:MAIL_PASSWORD="<app_password>"
```

## Images
Images are stored locally in:
- `backend/src/main/resources/static/uploads/`

They are served from:
- `http://localhost:8080/uploads/<filename>`

## API (Main)
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/users/me`
- `GET /api/products`
- `GET /api/products/{id}`
- `GET /api/cart`
- `POST /api/cart/items`
- `PATCH /api/cart/items/{productId}`
- `DELETE /api/cart/items/{productId}`
- `POST /api/orders`
- `GET /api/orders`
- `GET /api/orders/{id}`
- `PATCH /api/orders/{id}/cancel`
- `POST /api/support/contact`

Admin API:
- `GET /api/admin/products`
- `POST /api/admin/products`
- `PUT /api/admin/products/{id}`
- `DELETE /api/admin/products/{id}`
- `POST /api/admin/products/upload`
- `GET /api/admin/orders`
- `PATCH /api/admin/orders/{id}/status`

## GitHub Actions
This repo includes CI workflows for frontend and backend builds.
