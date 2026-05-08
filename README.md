# 📦 Inventory Management System

REST API for inventory management built with **Spring Boot 4**, following best practices in layered architecture, data validation, and automated documentation.

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 25 | Main language |
| Spring Boot | 4.0.6 | Backend framework |
| Spring Data JPA | — | Persistence and pagination |
| Hibernate | — | ORM |
| Bean Validation | — | Input validation |
| H2 Database | — | In-memory database (`test` profile) |
| PostgreSQL | — | Relational database (`dev` profile) |
| Springdoc OpenAPI | 3.0.3 | Swagger UI documentation |
| JUnit 5 + Mockito | — | Unit testing |
| Maven | — | Dependency management |

## 📐 Architecture

The project follows the layered architecture pattern:

```
controllers/         → Handles HTTP requests, returns DTOs
  exceptions/        → Global error handling (@ControllerAdvice)
dto/                 → Java Records for data transfer
services/            → Business logic and transactions
repositories/        → Database access (Spring Data JPA)
entities/            → JPA entities (table mappings)
  enums/             → Enumerations (MovementType)
config/              → Configuration (CORS, data seeding)
```

## 📊 Domain Model

```
┌──────────┐     ┌───────────┐     ┌──────────────────┐
│   User   │     │ Category  │     │    Supplier       │
│──────────│     │───────────│     │──────────────────│
│ id       │     │ id        │     │ id               │
│ name     │     │ name      │     │ name             │
│ email    │     │           │     │ cnpj             │
│ phone    │     └─────┬─────┘     │ email            │
│ password │           │ N:N       │ phone            │
└────┬─────┘     ┌─────┴─────┐     └────────┬─────────┘
     │           │  Product  │              │ 1:N
     │           │───────────│──────────────┘
     │           │ id        │
     │           │ name      │
     │           │ price     │
     │           │ stockQty  │
     │           │ minStock  │
     │           └─────┬─────┘
     │     1:N         │ 1:N
     │    ┌────────────┴──────────────┐
     └────┤    StockMovement          │
          │───────────────────────────│
          │ id, quantity, reason      │
          │ movementType (ENTRY/EXIT/ │
          │   RETURN/ADJUSTMENT)      │
          │ moment                    │
          └───────────────────────────┘
```

## 🚀 API Endpoints

### Products (`/products`)
| Method | Route | Description |
|---|---|---|
| `GET` | `/products?page=0&size=10` | Paginated product list |
| `GET` | `/products/{id}` | Find product by ID |
| `GET` | `/products/low-stock` | Products below minimum stock |
| `POST` | `/products` | Create new product |
| `PUT` | `/products/{id}` | Update product |
| `DELETE` | `/products/{id}` | Delete product |

### Stock Movements (`/stock-movements`)
| Method | Route | Description |
|---|---|---|
| `GET` | `/stock-movements` | Paginated movement list |
| `GET` | `/stock-movements/{id}` | Find movement by ID |
| `GET` | `/stock-movements/product/{productId}` | Movement history for a product |
| `POST` | `/stock-movements` | Register movement (auto-updates stock) |

### Users (`/users`), Categories (`/categories`), Suppliers (`/suppliers`)
All follow the standard CRUD pattern: `GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}`.

## ✅ Features

- **Pagination** — All list endpoints return `Page<DTO>` with support for `page`, `size`, and `sort` query params.
- **Bean Validation** — Automatic field validation (`@NotBlank`, `@Email`, `@Positive`, etc.) with a standardized `422 Unprocessable Entity` response.
- **DTOs with Java Records** — Separation between JPA entities and API responses. Sensitive data (e.g., passwords) is never exposed.
- **Transaction management** — `@Transactional` on all Service methods. Compound operations (e.g., register movement + update stock) are atomic.
- **Stock control rule** — Exit movements (`EXIT`) are blocked when the requested quantity exceeds the available stock.
- **Interactive documentation** — Swagger UI available at `/swagger-ui/index.html`.
- **Global exception handling** — `ResourceNotFoundException` (404), `DataBaseException` (400), and `MethodArgumentNotValidException` (422).

## ⚙️ Getting Started

### Prerequisites
- Java 25+
- Maven (or use the included `mvnw` wrapper)

### `test` profile (H2 — no database installation required)
```bash
# In application.properties, set:
# spring.profiles.active=test

./mvnw spring-boot:run
```
The H2 database is created in-memory and automatically seeded with sample data.
H2 Console available at: `http://localhost:8080/h2-console`

### `dev` profile (PostgreSQL)
```bash
# 1. Create the database in PostgreSQL:
# CREATE DATABASE inventory_db;

# 2. Update credentials in application-dev.properties

# 3. In application.properties, set:
# spring.profiles.active=dev

./mvnw spring-boot:run
```

### Running tests
```bash
./mvnw test "-Dspring.profiles.active=test"
```

## 🧪 Tests

The project includes unit tests with **JUnit 5 + Mockito**:

- **`StockMovementServiceTest`** (6 tests) — Covers all movement types (`ENTRY`, `EXIT`, `RETURN`, `ADJUSTMENT`) and the insufficient stock scenario.
- **`ProductServiceTest`** (5 tests) — Covers CRUD operations, DTO conversion, and resource-not-found handling.

## 📁 Project Structure

```
src/
├── main/java/com/thaleskirchner/inventory/
│   ├── config/
│   │   ├── TestConfig.java          # Data seeding (test profile)
│   │   └── WebConfig.java           # CORS configuration
│   ├── controllers/
│   │   ├── CategoryController.java
│   │   ├── ProductController.java
│   │   ├── SupplierController.java
│   │   ├── StockMovementController.java
│   │   ├── UserController.java
│   │   └── exceptions/
│   │       ├── ResourceExceptionHandler.java
│   │       ├── StandardError.java
│   │       ├── ValidationError.java
│   │       └── FieldMessage.java
│   ├── dto/
│   │   ├── CategoryDTO.java
│   │   ├── ProductDTO.java
│   │   ├── SupplierDTO.java
│   │   ├── StockMovementDTO.java
│   │   └── UserDTO.java
│   ├── entities/
│   │   ├── Category.java
│   │   ├── Product.java
│   │   ├── Supplier.java
│   │   ├── StockMovement.java
│   │   ├── User.java
│   │   └── enums/
│   │       └── MovementType.java
│   ├── repositories/
│   │   ├── CategoryRepository.java
│   │   ├── ProductRepository.java
│   │   ├── SupplierRepository.java
│   │   ├── StockMovementRepository.java
│   │   └── UserRepository.java
│   └── services/
│       ├── CategoryService.java
│       ├── ProductService.java
│       ├── SupplierService.java
│       ├── StockMovementService.java
│       ├── UserService.java
│       └── exceptions/
│           ├── DataBaseException.java
│           └── ResourceNotFoundException.java
├── main/resources/
│   ├── application.properties
│   ├── application-test.properties
│   └── application-dev.properties
└── test/java/com/thaleskirchner/inventory/services/
    ├── StockMovementServiceTest.java
    └── ProductServiceTest.java
```