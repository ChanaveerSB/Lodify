# LOADIFY

**Official Project Title:** Goods Transport Backhaul Optimization System  
**Tagline:** Optimize Every Return Trip

LOADIFY is a full-stack logistics web application that reduces empty return truck trips by connecting transport providers with customers who need goods delivered on return routes.

Example: when a provider posts `Bangalore -> Chennai` with a return date, the system stores the searchable booking route as `Chennai -> Bangalore`.

## Tech Stack

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
http://localhost:8080/index.html
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
- Public navbar search shows available truck cards and redirects unauthenticated users to login when they click Book.
