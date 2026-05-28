# 🚛 LOADIFY — Goods Transport Backhaul Optimization System

> **Optimize Every Return Trip** — A smart platform that connects transport providers with customers to monetize empty return-load capacity and reduce fuel waste.

---

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [System Architecture](#system-architecture)
- [Project Structure](#project-structure)
- [Database Schema](#database-schema)
- [API Reference](#api-reference)
- [Application Flow](#application-flow)
- [User Roles](#user-roles)
- [Data Models & Enums](#data-models--enums)
- [Security](#security)
- [Frontend Pages](#frontend-pages)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Logging](#logging)
- [Error Handling](#error-handling)
- [Known Limitations & Future Work](#known-limitations--future-work)

---

## Overview

Loadify is a **Goods Transport Backhaul Optimization System** built with Spring Boot. It solves a real logistics problem: trucks that travel from Point A to Point B often return empty. Loadify allows transport providers to list their return trips and lets customers book available capacity on those return routes — reducing operational costs for providers and offering affordable transport for customers.

The core business logic is a **route inversion system**: when a provider registers a trip from `Bangalore → Chennai`, Loadify internally stores and makes discoverable the return trip `Chennai → Bangalore` on the return date, enabling a true backhaul marketplace.

---

## Key Features

- **Return-Load Marketplace** — Providers post forward trips; the system automatically indexes the return route for customer discovery.
- **Real-Time Capacity Management** — Booking a truck deducts weight from available capacity instantly. Cancellations restore it. Truck status auto-updates between `AVAILABLE`, `PARTIALLY_BOOKED`, and `FULLY_BOOKED`.
- **Advanced Truck Search** — Filter by source, destination, date, capacity, truck type, min rating, max price, and status. Supports pagination and sorting.
- **Role-Based User System** — Three roles: `ADMIN`, `TRANSPORT_PROVIDER`, and `CUSTOMER`.
- **Admin Dashboard** — Aggregated stats (revenue, bookings, trucks) filterable by day, month, or year.
- **Feedback & Complaints** — Users can submit categorized feedback visible to admins.
- **BCrypt Password Hashing** — All passwords are securely hashed at rest.
- **Standardized API Responses** — All endpoints return a unified `ResponseStructure<T>` envelope with status code, message, data, and timestamp.
- **Global Exception Handling** — Centralized error responses for all custom and validation exceptions.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.3.5 |
| Web | Spring MVC (REST) |
| Persistence | Spring Data JPA + Hibernate |
| Database | PostgreSQL |
| Security | Spring Security + BCrypt |
| Validation | Jakarta Bean Validation |
| Build Tool | Maven |
| Frontend | Vanilla HTML, CSS, JavaScript |
| Logging | Spring Boot Logging (file-based) |
| Dev Tools | Spring Boot DevTools |

---

## System Architecture

```
┌──────────────────────────────────────────────┐
│              Static Frontend                  │
│   HTML + CSS + Vanilla JS (served by Spring) │
└───────────────────┬──────────────────────────┘
                    │ HTTP / REST
┌───────────────────▼──────────────────────────┐
│              Controller Layer                 │
│  AuthController  TruckController             │
│  BookingController  DashboardController      │
│  AdminController  FeedbackController         │
└───────────────────┬──────────────────────────┘
                    │
┌───────────────────▼──────────────────────────┐
│               Service Layer                   │
│  AuthService  TruckService  BookingService   │
│  AdminService  DashboardService  UserService  │
└──────────┬────────────────────────┬──────────┘
           │ DAO Pattern            │ JPA Repository
┌──────────▼────────┐   ┌──────────▼──────────┐
│    DAO Layer      │   │  Repository Layer    │
│  BookingDao       │   │  BookingRepository   │
│  TruckDao         │   │  TruckRepository     │
│  UserDao          │   │  FeedbackRepository  │
└──────────┬────────┘   └──────────┬──────────┘
           └──────────┬────────────┘
              ┌───────▼────────┐
              │   PostgreSQL   │
              │   LodifyDB     │
              └────────────────┘
```

---

## Project Structure

```
Loadify/
├── src/
│   └── main/
│       ├── java/com/loadify/
│       │   ├── LoadifyApplication.java          # Entry point
│       │   ├── config/
│       │   │   └── AppConfig.java               # BCryptPasswordEncoder bean
│       │   ├── controller/
│       │   │   ├── AuthController.java          # POST /auth/signup, /auth/login
│       │   │   ├── TruckController.java         # CRUD + search + provider trips
│       │   │   ├── BookingController.java       # CRUD + customer/provider views
│       │   │   ├── DashboardController.java     # Role-based dashboard summaries
│       │   │   ├── AdminController.java         # Admin stats + feedback list
│       │   │   └── FeedbackController.java      # POST /api/user/feedback
│       │   ├── service/
│       │   │   ├── AuthService.java             # Signup + login with BCrypt
│       │   │   ├── TruckService.java            # Route inversion + JPA Specification search
│       │   │   ├── BookingService.java          # Booking lifecycle + capacity management
│       │   │   ├── AdminService.java            # Revenue stats + feedback
│       │   │   ├── DashboardService.java        # Summary stats
│       │   │   └── UserService.java             # User lookup
│       │   ├── dao/
│       │   │   ├── BookingDao.java              # Booking data access wrapper
│       │   │   ├── TruckDao.java                # Truck data access wrapper
│       │   │   └── UserDao.java                 # User data access wrapper
│       │   ├── repository/
│       │   │   ├── BookingRepository.java       # JPA + custom revenue query
│       │   │   ├── TruckRepository.java         # JPA repository
│       │   │   ├── FeedbackRepository.java      # JPA repository
│       │   │   └── UserRepository.java          # JPA repository
│       │   ├── entity/
│       │   │   ├── User.java                    # users table
│       │   │   ├── Truck.java                   # trucks table
│       │   │   ├── Booking.java                 # bookings table
│       │   │   └── Feedback.java                # feedbacks table
│       │   ├── dto/
│       │   │   ├── SignupRequest.java
│       │   │   ├── LoginRequest.java
│       │   │   ├── UserResponse.java
│       │   │   ├── TruckRequest.java
│       │   │   ├── TruckResponse.java
│       │   │   ├── BookingRequest.java
│       │   │   ├── BookingResponse.java
│       │   │   └── DashboardResponse.java
│       │   ├── enums/
│       │   │   ├── UserRole.java                # ADMIN, TRANSPORT_PROVIDER, CUSTOMER
│       │   │   ├── TripStatus.java              # AVAILABLE, PARTIALLY_BOOKED, FULLY_BOOKED, COMPLETED
│       │   │   ├── BookingStatus.java           # PENDING, CONFIRMED, CANCELLED, DELIVERED
│       │   │   └── RouteType.java               # ONE_WAY, RETURN
│       │   ├── exception/
│       │   │   ├── GlobalExceptionHandler.java  # @RestControllerAdvice
│       │   │   ├── BookingNotFoundException.java
│       │   │   ├── TruckNotFoundException.java
│       │   │   ├── UserNotFoundException.java
│       │   │   ├── DuplicateEmailException.java
│       │   │   ├── InvalidCapacityException.java
│       │   │   └── UnauthorizedAccessException.java
│       │   ├── security/
│       │   │   ├── SecurityConfig.java          # Spring Security + CORS config
│       │   │   └── SecurityNotes.java           # Notes on JWT roadmap
│       │   └── util/
│       │       └── ResponseStructure.java       # Generic API response wrapper
│       └── resources/
│           ├── application.properties           # App config (port, DB, JPA, logging)
│           ├── db/
│           │   ├── schema.sql                   # DDL: tables + indexes
│           │   └── sample-data.sql              # Seed data for testing
│           └── static/                          # Frontend (served by Spring)
│               ├── index.html                   # Landing page
│               ├── css/styles.css
│               ├── js/app.js                    # Shared frontend JS utilities
│               └── pages/
│                   ├── login.html
│                   ├── signup.html
│                   ├── dashboard.html
│                   ├── post-trip.html
│                   ├── book-truck.html
│                   ├── booking.html
│                   ├── my-bookings.html
│                   ├── my-trips.html
│                   ├── feedback.html
│                   └── admin-dashboard.html
├── docs/
│   ├── API_DOCUMENTATION.md
│   └── SAMPLE_RESPONSES.md
├── logs/
│   └── loadify.log
└── pom.xml
```

---

## Database Schema

Three core tables with referential integrity and performance indexes.

**`users`**

| Column | Type | Notes |
|---|---|---|
| user_id | BIGSERIAL PK | Auto-increment |
| full_name | VARCHAR(100) | Not null |
| email | VARCHAR(120) | Unique, not null |
| phone | VARCHAR(15) | Not null |
| password | VARCHAR(255) | BCrypt hash |
| role | VARCHAR(30) | ADMIN / TRANSPORT_PROVIDER / CUSTOMER |
| is_active | BOOLEAN | Default true |
| created_at | TIMESTAMP | Auto-set on insert |

**`trucks`**

| Column | Type | Notes |
|---|---|---|
| truck_id | BIGSERIAL PK | |
| uploaded_by | BIGINT FK | References users(user_id) |
| truck_number | VARCHAR(20) | |
| truck_type | VARCHAR(30) | |
| driver_name | VARCHAR(50) | |
| driver_phone | VARCHAR(15) | |
| capacity | DOUBLE | Total capacity in tons |
| available_capacity | DOUBLE | Decremented on booking |
| source | VARCHAR(50) | Stored as return-trip source |
| destination | VARCHAR(50) | Stored as return-trip destination |
| departure_date | DATE | Return trip departure date |
| return_date | DATE | Original forward trip date |
| price_per_ton | DOUBLE | Pricing basis for cost calculation |
| route_type | VARCHAR(30) | ONE_WAY / RETURN |
| status | VARCHAR(20) | AVAILABLE / PARTIALLY_BOOKED / FULLY_BOOKED / COMPLETED |
| rating | DOUBLE | Default 4.5 |
| insurance_available | BOOLEAN | Default true |

**`bookings`**

| Column | Type | Notes |
|---|---|---|
| booking_id | BIGSERIAL PK | |
| truck_id | BIGINT FK | References trucks(truck_id) |
| customer_id | BIGINT FK | References users(user_id) |
| goods_type | VARCHAR(50) | |
| weight | DOUBLE | Booked tonnage |
| booking_status | VARCHAR(20) | PENDING / CONFIRMED / CANCELLED / DELIVERED |
| total_price | DOUBLE | weight × price_per_ton |
| created_at | TIMESTAMP | Auto-set |

**`feedbacks`**

| Column | Type | Notes |
|---|---|---|
| id | BIGSERIAL PK | |
| category | VARCHAR | Feedback category |
| message | TEXT | |
| user_id | BIGINT FK | References users(user_id) |
| created_at | TIMESTAMP | |

**Indexes**

```sql
CREATE INDEX idx_trucks_search   ON trucks(source, destination, departure_date, available_capacity, status);
CREATE INDEX idx_bookings_customer ON bookings(customer_id);
CREATE INDEX idx_bookings_truck    ON bookings(truck_id);
```

---

## API Reference

Base URL: `http://localhost:9090`

All responses follow the standard envelope:

```json
{
  "statusCode": 200,
  "message": "Operation message",
  "data": {},
  "timestamp": "2026-05-10T10:30:00"
}
```

### Authentication — `/auth`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/signup` | Register a new user |
| POST | `/auth/login` | Authenticate and get user details |

**Signup Request**
```json
{
  "fullName": "Arjun Transport Co",
  "email": "provider@loadify.com",
  "phone": "9876543210",
  "password": "secret123",
  "confirmPassword": "secret123",
  "role": "TRANSPORT_PROVIDER"
}
```

**Login Request**
```json
{
  "email": "provider@loadify.com",
  "password": "secret123"
}
```

---

### Trucks — `/trucks`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/trucks` | Create / upload a return trip |
| GET | `/trucks` | Get all trucks |
| GET | `/trucks/{id}` | Get truck by ID |
| PUT | `/trucks/{id}` | Update truck details |
| DELETE | `/trucks/{id}` | Delete a truck |
| GET | `/trucks/search` | Search with filters and pagination |
| GET | `/trucks/provider/{providerId}` | Get all trips by a provider |

**Search Query Parameters**

| Param | Type | Description |
|---|---|---|
| source | String | Origin city |
| destination | String | Destination city |
| date | LocalDate | Departure date (ISO format) |
| capacity | Double | Minimum required capacity (tons) |
| truckType | String | Type of truck |
| minRating | Double | Minimum rating filter |
| maxPrice | Double | Maximum price per ton |
| status | TripStatus | Filter by truck status |
| page | int | Page number (default: 0) |
| size | int | Page size (default: 9) |
| sortBy | String | Sort field (default: departureDate) |
| direction | String | `asc` or `desc` |

**Create Truck Request**
```json
{
  "uploadedBy": 1,
  "truckNumber": "KA01AB4587",
  "truckType": "Container",
  "driverName": "Ravi Kumar",
  "driverPhone": "9988776655",
  "capacity": 18.0,
  "source": "Bangalore",
  "destination": "Chennai",
  "departureDate": "2026-05-10",
  "returnDate": "2026-05-12",
  "estimatedArrivalTime": "2026-05-12T10:30:00",
  "pricePerTon": 4200
}
```

> **Note:** The service automatically inverts the route. The truck above is stored and discoverable as `Chennai → Bangalore` on `2026-05-12`.

---

### Bookings — `/bookings`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/bookings` | Create a new booking |
| GET | `/bookings` | Get all bookings |
| GET | `/bookings/{id}` | Get booking by ID |
| PUT | `/bookings/{id}` | Update booking details |
| DELETE | `/bookings/{id}` | Delete a booking (restores capacity) |
| GET | `/bookings/customer/{customerId}` | All bookings by a customer |
| GET | `/bookings/provider/{providerId}` | All booking requests for a provider |

**Create Booking Request**
```json
{
  "truckId": 1,
  "customerId": 2,
  "customerName": "Meera Foods",
  "customerPhone": "9123456780",
  "goodsType": "Packaged snacks",
  "weight": 4.5,
  "pickupLocation": "Chennai Warehouse",
  "dropLocation": "Bangalore Distribution Hub",
  "bookingDate": "2026-05-12",
  "requiredTrucks": 1,
  "bookingNotes": "Pickup after 10 AM"
}
```

> `totalPrice` is auto-calculated as `weight × truck.pricePerTon`. Available capacity is decremented immediately.

---

### Dashboard — `/dashboard`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/dashboard/admin` | Admin summary stats |
| GET | `/dashboard/provider` | Provider summary stats |
| GET | `/dashboard/customer` | Customer summary stats |

---

### Admin — `/api/admin`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/admin/stats?filter=day\|month\|year` | Revenue + trucks + bookings + feedback stats |
| GET | `/api/admin/feedbacks` | All feedbacks ordered by date desc |

---

### Feedback — `/api/user`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/user/feedback` | Submit user feedback or complaint |

---

## Application Flow

### Transport Provider Flow

```
Register (role: TRANSPORT_PROVIDER)
    ↓
Login
    ↓
Post Truck Trip (POST /trucks)
   • Provide forward trip details: source, destination, departure date, return date
   • System stores the RETURN route with return date as the new departure
    ↓
View My Trips (GET /trucks/provider/{id})
    ↓
Receive Booking Requests (GET /bookings/provider/{id})
    ↓
Manage Trip Status
```

### Customer Flow

```
Register (role: CUSTOMER)
    ↓
Login
    ↓
Search Available Trucks (GET /trucks/search)
   • Filter by source, destination, date, capacity, price
    ↓
View Truck Details (GET /trucks/{id})
    ↓
Create Booking (POST /bookings)
   • Capacity validated in real-time
   • Total price auto-calculated
    ↓
Track My Bookings (GET /bookings/customer/{id})
    ↓
Cancel Booking if needed (capacity automatically restored)
```

### Admin Flow

```
Login (role: ADMIN)
    ↓
Admin Dashboard (GET /api/admin/stats?filter=month)
   • Total revenue
   • Recent trucks listed
   • Recent bookings
   • Recent feedbacks
    ↓
Review Feedbacks (GET /api/admin/feedbacks)
```

---

## User Roles

| Role | Capabilities |
|---|---|
| `ADMIN` | View platform-wide stats, all feedbacks, access admin dashboard |
| `TRANSPORT_PROVIDER` | Post trucks/trips, view own trips, see booking requests on their trucks |
| `CUSTOMER` | Search trucks, create bookings, manage own bookings, submit feedback |

---

## Data Models & Enums

**UserRole**
```
ADMIN | TRANSPORT_PROVIDER | CUSTOMER
```

**TripStatus** (auto-managed by booking logic)
```
AVAILABLE → PARTIALLY_BOOKED → FULLY_BOOKED → COMPLETED
```

**BookingStatus**
```
PENDING → CONFIRMED → DELIVERED
         ↘ CANCELLED (capacity restored)
```

**RouteType**
```
ONE_WAY | RETURN
```

> All current trips default to `RouteType.RETURN` — the core purpose of the platform.

---

## Security

- Spring Security is configured with CSRF disabled (stateless REST API).
- **BCrypt** is used for password hashing via `BCryptPasswordEncoder`.
- All endpoints are currently set to `.permitAll()` — JWT and Role-Based Access Control (RBAC) infrastructure is scaffolded and ready to enable.
- CORS is configured to accept all origins with credentials for development.

> **Roadmap Note:** The `SecurityConfig.java` contains explicit comments marking where `.permitAll()` should be swapped for `.hasAuthority("ROLE_ADMIN")` etc. once JWT filter is added.

---

## Frontend Pages

All frontend files are served as static resources by Spring Boot from `src/main/resources/static/`.

| Page | Path | Description |
|---|---|---|
| Landing | `/index.html` | Hero, truck preview, how-it-works, testimonials |
| Signup | `/pages/signup.html` | User registration form |
| Login | `/pages/login.html` | Login form |
| Dashboard | `/pages/dashboard.html` | Post-login home with stats |
| Post Trip | `/pages/post-trip.html` | Transport provider trip upload form |
| Book Truck | `/pages/book-truck.html` | Search and browse available trucks |
| Booking | `/pages/booking.html` | Booking detail/confirmation |
| My Bookings | `/pages/my-bookings.html` | Customer's booking history |
| My Trips | `/pages/my-trips.html` | Provider's uploaded trips |
| Feedback | `/pages/feedback.html` | Submit feedback/complaints |
| Admin Dashboard | `/pages/admin-dashboard.html` | Admin stats panel |

Frontend state is managed via `localStorage` (`loadifyUser` key). The shared `app.js` provides utilities for API calls, toast notifications, user session, and admin navigation injection.

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 14+

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/Loadify.git
cd Loadify
```

### 2. Create the Database

```sql
CREATE DATABASE "LodifyDB";
```

Then run the schema script:

```bash
psql -U postgres -d LodifyDB -f src/main/resources/db/schema.sql
```

Optionally load sample data:

```bash
psql -U postgres -d LodifyDB -f src/main/resources/db/sample-data.sql
```

### 3. Configure the Application

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/LodifyDB
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### 4. Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

### 5. Access the Application

- **Frontend:** `http://localhost:9090`
- **API Base:** `http://localhost:9090`

---

## Configuration

All configuration is in `src/main/resources/application.properties`:

```properties
# Server
server.port=9090

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/LodifyDB
spring.datasource.username=postgres
spring.datasource.password=root

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.org.springframework=INFO
logging.level.com.loadify=DEBUG
logging.file.name=logs/loadify.log
```

---

## Logging

Logs are written to `logs/loadify.log` with daily rolling. Application-level logs are at `DEBUG`, Spring framework logs at `INFO`.

Log files are kept in the `logs/` directory with rotation:
```
logs/
├── loadify.log              # Current log
└── loadify.log.2026-05-19.0.gz  # Archived log
```

---

## Error Handling

All errors are handled by `GlobalExceptionHandler` (`@RestControllerAdvice`) and return the same `ResponseStructure` envelope.

| Exception | HTTP Status |
|---|---|
| `UserNotFoundException` | 404 |
| `TruckNotFoundException` | 404 |
| `BookingNotFoundException` | 404 |
| `InvalidCapacityException` | 400 |
| `DuplicateEmailException` | 400 |
| `IllegalArgumentException` | 400 |
| `UnauthorizedAccessException` | 403 |
| `MethodArgumentNotValidException` | 400 (field-level map) |

**Validation Error Response Example:**
```json
{
  "statusCode": 400,
  "message": "Validation failed",
  "data": {
    "email": "must be a valid email address",
    "weight": "must be greater than 0"
  },
  "timestamp": "2026-05-28T10:00:00"
}
```

---

## Known Limitations & Future Work

- **No JWT Authentication** — All routes are currently open. JWT token-based auth and RBAC are scaffolded but not yet activated. The `SecurityConfig` is structured for easy enablement.
- **No Image Upload** — Truck `imageUrl` stores a URL string; actual file upload (multipart) is commented out in properties.
- **Session via localStorage** — The frontend uses browser localStorage for session; no server-side session management yet.
- **Booking Confirmation Flow** — Confirm/Cancel booking endpoints exist in the service layer but are not yet exposed via dedicated controller routes.
- **Rating System** — Truck ratings are stored but the review/rating submission flow is not yet implemented.
- **Route Matching Intelligence** — The current search is filter-based. A future enhancement could include fuzzy location matching or map-based routing.
- **Payment Integration** — Pricing is calculated but no payment gateway is integrated.

---

## Documentation

Additional documentation is available in the `docs/` folder:

- [`docs/API_DOCUMENTATION.md`](docs/API_DOCUMENTATION.md) — Full request/response examples for all endpoints
- [`docs/SAMPLE_RESPONSES.md`](docs/SAMPLE_RESPONSES.md) — Sample API response payloads

---

*Built with Spring Boot 3.3.5 · Java 17 · PostgreSQL · Version 1.0.0*



<!-- ## Tech Stack

- Frontend: HTML, CSS, JavaScript
- Backend: Java, Spring Boot, Maven
- Database: PostgreSQL
- Security: BCrypt password hashing
- Final-note scope: JWT and RBAC are intentionally not implemented.

## Project Structure

```text
src/main/java/com/loadify
  config
  controller
  dao
  dto
  entity
  enums
  exception
  repository
  security
  service
  util
src/main/resources
  static
    css
    js
    pages
  db
docs
```

## Run Steps

1. Create PostgreSQL database:

```sql
CREATE DATABASE loadify;
```

2. Update credentials in `src/main/resources/application.properties` if needed.

3. Run schema manually, or let Hibernate create tables:

```bash
psql -U postgres -d loadify -f src/main/resources/db/schema.sql
```

4. Start the app:

```bash
mvn spring-boot:run
```

5. Open:

```text
http://localhost:9090/index.html
```

## Main Pages

- Public home: `/index.html`
- Signup: `/pages/signup.html`
- Login: `/pages/login.html`
- Dashboard: `/pages/dashboard.html`
- Post trip: `/pages/post-trip.html`
- Book truck: `/pages/book-truck.html`
- Booking form: `/pages/booking.html`
- My trips: `/pages/my-trips.html`
- My bookings: `/pages/my-bookings.html`

## Notes For Evaluation

- Controllers only handle request/response flow.
- Services contain business rules.
- DAOs wrap repository access.
- DTOs are used for request and response transfer.
- Global exception handling returns consistent `ResponseStructure`.
- Passwords are stored using BCrypt.
- Public navbar search shows available truck cards and redirects unauthenticated users to login when they click Book. -->
