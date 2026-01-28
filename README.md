# Pastebin-Lite (Spring Boot + React)

## 📌 Project Overview
Pastebin-Lite is a full-stack application that allows users to create, share, and view text snippets ("pastes").  
It is built with **Spring Boot (backend)** and **React (frontend)**, deployed on **Render** and **Vercel** respectively.

---

## ✨ Features
- Create a paste with configurable **expiry time (TTL)** and **view limits**.
- Retrieve pastes via:
  - **API endpoint** (`/api/pastes/:id`) → returns JSON.
  - **HTML endpoint** (`/p/:id`) → returns safe HTML for direct browser visits.
- Deterministic expiry support using:
  - `TEST_MODE=1` environment variable.
  - `x-test-now-ms` header for simulated current time.
- Health check endpoint: `/api/healthz`.
- Deployed frontend and backend with proper CORS integration.

---

## 🛠️ Tech Stack
- **Backend**: Java, Spring Boot, JPA/Hibernate, MySQL
- **Frontend**: React (Vite), React Router
- **Deployment**: Render (backend), Vercel (frontend), Railway (database)
- **Build Tools**: Maven, Docker (for containerization)

---

## 📋 Prerequisites

### Backend (Spring Boot)
- Java 17 or higher installed  
- Maven (comes bundled with Spring Boot wrapper `./mvnw`)  
- MySQL running locally or accessible via cloud (update `application.properties` with DB credentials)  
- Docker (optional, if you want to run backend in a container)  

### Frontend (React + Vite)
- Node.js (v18 or higher )  
- npm package manager  
---

## ⚙️ API Endpoints
### Create Paste
POST /api/pastes
Body: {
  "content": "Hello World",
  "ttlSeconds": 3600,
  "maxViews": 5
}
Response: {
  "id": "uuid",
  "link": "/p/uuid"
}

### Get Paste (JSON)
GET /api/pastes/{id}
Response: {
  "content": "Hello World",
  "expires_at": "...",
  "remaining_views": 3
}

### Get Paste (HTML)

GET /p/{id}
Response: HTML page with paste content
### Health Check

GET /api/healthz
Response: 200 OK
## 🧪 Deterministic Expiry
When **TEST_MODE=1** is set:
- Backend reads x-test-now-ms header (milliseconds since epoch).
- Uses that as the current time for expiry checks.

## 🚀 Deployment URLs
### - Frontend (Vercel):
https://pastebin-springboot-app.vercel.app (pastebin-springboot-app.vercel.app)
### - Backend (Render):
https://pastebin-springboot-app.onrender.com (pastebin-springboot-app.onrender.com)

## 🖥️ Running Locally
### Backend
cd backend<br>
./mvnw spring-boot:run<br>


 **Backend runs at http://localhost:8080.**
 
### Frontend
cd frontend<br/>
npm install<br/>
npm run dev<br/>


**Frontend runs at http://localhost:5173.**

## 📖 Design Decisions
- Separation of concerns: /api/pastes/:id for JSON, /p/:id for HTML.
- Safe rendering: Paste content is escaped to prevent script injection.
- Cloud deployment: Backend on Render, frontend on Vercel, integrated via environment variables.
- Deterministic expiry: Ensures automated tests can simulate time-based expiry.

