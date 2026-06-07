# Pottery Workshop Web Reservation System

🌐 [日本語](README.md) | **English**

A reservation system for pottery-experience workshops, built with HTML / CSS / Servlet / JSP.
It follows the **MVC model with DAO / DTO / Service / Servlet / JSP** layers.

- Runtime: **Tomcat 11** (Jakarta EE 11 / `jakarta.servlet`), **Java 17+**
- Database: **PostgreSQL 14+**
- Build: **Maven**

---

## Features

### Members (general users)
- Sign-up (name, email, phone, address, password) with **input validation**
- Login (email + password) / logout
- Login state kept in the session (until logout)
- Main page
  - Workshop detail list
  - Event calendar (workshops shown on their event dates)
  - Notices from the administrator
  - **Reservations can be made from both the list and the calendar**
- **Course (plan) selection** at reservation time
- "My reservations" list + **cancellation**

### Administrator (accessed via a separate URL `/admin/login`)
- Dashboard
- **Notice management**: add / edit / delete
- **Workshop management**: add / edit / delete
- **Reservation management**: edit / delete
- **Member management**: delete only

---

## Architecture (MVC)

```
Browser
  │  HTTP (Filters handle auth & character encoding)
  ▼
[ Servlet ]  ── receives input, controls navigation (Controller)
  │
  ▼
[ Service ]  ── business logic & validation (ValidationUtil)
  │
  ▼
[ DAO ]      ── SQL execution & DB access (DBConnection)
  │
  ▼
[ Database ] PostgreSQL
  ▲
  │ data is passed between layers via [ DTO ]
  ▼
[ JSP ]      ── view rendering (View)
```

- **ER diagram**: [`docs/ER-diagram.md`](docs/ER-diagram.md)
- **Class & sequence diagrams**: [`docs/class-diagram.md`](docs/class-diagram.md)
- **Per-process class diagrams** (login/register/reserve/cancel/admin): [`docs/process-class-diagrams.md`](docs/process-class-diagrams.md)
- **User-side / admin-side class diagrams & filter behavior**: [`docs/class-diagram-by-role.md`](docs/class-diagram-by-role.md)
- **Per-function class diagrams (incl. JSPs, full members)**: [`docs/class-diagram-by-function.md`](docs/class-diagram-by-function.md)

### Two architectures (bundled for study)
Two implementations with different designs are bundled **side by side** in the same app (DAO/DTO/CSS are shared).

| Architecture | Entry URL | Description |
|--------------|-----------|-------------|
| **Layered MVC** (standard) | `/` | Servlet → **Service** → DAO with separated layers. Packages `servlet` / `service` / `dao` / `dto` |
| **Service-merged** | `/combined/` | No Service layer; business logic written **directly in the Servlets**. Packages `combined.servlet` / `combined.filter` |

- The two are fully independent via different URLs and session attributes (`loginMember` ↔ `cbLoginMember`, etc.). Logging into one does not affect the other.
- Features (register / login / reserve / course selection / cancel / admin CRUD / IP restriction) are equivalent.

### Package structure
```
com.pottery.reservation
├── dto/        MemberDTO, WorkshopDTO, ReservationDTO, NoticeDTO, CourseDTO
├── dao/        MemberDAO, WorkshopDAO, ReservationDAO, NoticeDAO, CourseDAO
├── service/    MemberService, WorkshopService, ReservationService, NoticeService, CourseService
├── servlet/    Register/Login/Logout/Main/Reservation servlets
│   └── admin/  AdminLogin/Logout/Dashboard + Notice/Workshop/Reservation/Member admin
├── combined/   Service-merged variant (combined.servlet / combined.filter)
├── filter/     EncodingFilter, AuthFilter, AdminAuthFilter
└── util/       DBConnection, ValidationUtil
```

---

## Database design (PostgreSQL)

| Table | Overview | Key columns |
|-------|----------|-------------|
| `members`      | members (general & admin) | name, email(UNIQUE), phone, address, password (plaintext / for the exercise), role |
| `workshops`    | workshops | title, description, instructor, event_date, start_time, capacity, price |
| `courses`      | courses (master of plans selected at reservation) | name, description, price |
| `reservations` | reservations | member_id(FK), workshop_id(FK), course_id(FK), number_of_people, status |
| `notices`      | notices | title, content, created_at, updated_at |

- Primary keys use `GENERATED ALWAYS AS IDENTITY` (PostgreSQL auto-numbering).
- `reservations` uses a **partial unique index** so a member×workshop pair is unique only while CONFIRMED
  → re-reservation after cancellation is allowed.
- Foreign keys are `ON DELETE CASCADE`; `notices.updated_at` is auto-updated by a trigger.
- See [`database/schema.sql`](database/schema.sql) for details.

---

## Setup

### 1. Requirements
| Software | Version | Check command |
|----------|---------|---------------|
| JDK | 17+ | `java -version` |
| Apache Tomcat | 11.x | — |
| PostgreSQL | 14+ | `psql --version` |
| Maven | 3.9+ | `mvn -v` |

> Example on macOS: `brew install openjdk@17 maven postgresql@16 tomcat`

### 2. Create the database
```bash
# Create the DB
psql -U postgres -c "CREATE DATABASE pottery_reservation ENCODING 'UTF8';"
# Load tables + seed data
psql -U postgres -d pottery_reservation -f database/schema.sql
```

### 3. Configure DB connection
Edit `URL` / `USER` / `PASSWORD` in
`src/main/java/com/pottery/reservation/util/DBConnection.java` for your environment.

```java
private static final String URL = "jdbc:postgresql://localhost:5432/pottery_reservation";
private static final String USER = "postgres";
private static final String PASSWORD = "password";
```

---

## Compile & build

```bash
# Syntax / dependency check only (produces class files)
mvn clean compile

# Package including tests (produces target/reservation.war)
mvn clean package
```

- If `mvn compile` reports **BUILD SUCCESS**, compilation passed.
- The artifact `target/reservation.war` bundles the PostgreSQL driver and JSTL implementation
  under `WEB-INF/lib` (the Servlet/JSP API is excluded since Tomcat provides it).

---

## Run (deploy to Tomcat 11)

### Option A: Drop the WAR in (simplest)
```bash
mvn clean package
cp target/reservation.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/startup.sh      # startup.bat on Windows
```
After startup, Tomcat auto-expands the WAR into `webapps/reservation/`.

### Option B: Tomcat Maven plugin
You can add a plugin to `pom.xml` for a `mvn tomcat:run`-style launch, but official
Tomcat 11 plugin support is limited, so **Option A (WAR deploy) is recommended**.

### URLs
- User entry: `http://localhost:8080/reservation/`
- Admin entry: `http://localhost:8080/reservation/admin/login`

### Stop
```bash
$CATALINA_HOME/bin/shutdown.sh
```

---

## Default accounts

| Role | Email | Password |
|------|-------|----------|
| Admin | `admin@pottery.com` | `admin123` |
| Member | `taro@example.com` | `user123` |

---

## Validation

| Field | Client side (HTML5) | Server side (ValidationUtil / Service) |
|-------|---------------------|----------------------------------------|
| Name | required, maxlength=100 | required, ≤100 chars |
| Email | type=email, required | required, regex format check |
| Phone | pattern, required | digits + hyphens, 10–13 chars |
| Address | required, maxlength=255 | required, ≤255 chars |
| Password | minlength=6 + confirm-match check | ≥6 chars |
| Number of people | select (up to remaining seats) | ≥1, plus over-capacity / duplicate / full checks |

> The client side is a first-pass check for convenience; the final decision is always made on the server (defense in depth).

---

## Admin access restriction (store terminal only)

The admin screens `/admin/*` are not linked from any user-facing page and are reachable
**only from allowed IP addresses (the store terminal)**. Non-allowed IPs receive a **404**,
hiding the very existence of the admin area (`AdminAuthFilter`).

Allowed IPs are configured without recompilation via the system property / environment variable
`ADMIN_ALLOWED_IPS` (comma-separated; a trailing `*` means prefix match). On Tomcat, set it in
`$CATALINA_HOME/bin/setenv.sh`:

```sh
# Allow only the store terminal's real IP (do not include loopback)
export CATALINA_OPTS="$CATALINA_OPTS -DADMIN_ALLOWED_IPS=192.168.11.50"
# e.g. allow a whole subnet: -DADMIN_ALLOWED_IPS=192.168.11.*
```

When unset, the default allows loopback only (`127.0.0.1` / `::1`).

> ⚠️ **Caution when combined with a tunnel**: traffic through a tunnel such as cloudflared
> appears as `127.0.0.1` to Tomcat. If you publish a tunnel while the default (loopback allowed)
> is in effect, the **admin screens become reachable from the internet**. To truly limit to the
> store terminal, set `ADMIN_ALLOWED_IPS` to the store terminal's real IP and do not include loopback.

---

## Notes
- Passwords are **stored in plaintext as specified for this exercise** (`MemberService`).
  In production, always hash them with bcrypt or similar.
- Character encoding is unified to UTF-8 for every request (`EncodingFilter`).
- Authentication is handled by `AuthFilter` (`/user/*`) and `AdminAuthFilter` (`/admin/*`).
- JSPs live under `WEB-INF` and are always rendered via a Servlet (no direct access).
