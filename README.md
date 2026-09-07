# Cirquo Server

Backend API for **Cirquo**, an Apple e-commerce platform. It provides public storefront catalog endpoints and authenticated customer/admin APIs with JWT-based authorization.

## Features

- REST API using a Controller → Service → Repository architecture.
- JWT access/refresh tokens with Spring Security.
- Role-based authorization for `CUSTOMER`, `STAFF`, and `ADMIN`.
- Customer profile, password, and delivery-address management.
- Public APIs for active Categories and Products.
- Admin APIs for Category, Product, and User management.
- Cursor pagination for public Products by Category; page pagination for Admin User/Product tables.
- PostgreSQL migrations managed by Flyway.
- Shared API response envelope and centralized exception handling.

## Tech stack

| Area | Technology |
| --- | --- |
| Language & runtime | Java 25 |
| Framework | Spring Boot 4 |
| Data access | Spring Data JPA, Hibernate, MapStruct |
| Database | PostgreSQL |
| Schema migration | Flyway |
| Security | Spring Security, JJWT |
| API documentation | springdoc OpenAPI / Swagger UI |
| Configuration | java-dotenv |

## Prerequisites

- JDK 25
- PostgreSQL 18 or a compatible PostgreSQL version
- A database named `cirquo_db`

## Local setup

### 1. Create the database

```sql
CREATE DATABASE cirquo_db;
```

### 2. Configure environment variables

Create `.env` inside `cirquo-server/`:

```env
DB_USERNAME=postgres
DB_PASSWORD=your_postgres_password
JWT_SECRET=replace_with_a_long_random_secret_at_least_32_characters
```

Never commit `.env`.

### 3. Start the API

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

macOS/Linux:

```bash
./mvnw spring-boot:run
```

The API starts at `http://localhost:8080`. Flyway validates/applies pending migrations at startup. Hibernate uses `ddl-auto: validate`, so entities must match the migration schema.

## Useful commands

| Command | Purpose |
| --- | --- |
| `./mvnw spring-boot:run` | Run the API. |
| `./mvnw clean compile` | Compile production sources. |
| `./mvnw test` | Run tests. |
| `./mvnw clean package` | Build the application JAR. |

On Windows, use `.\mvnw.cmd` instead of `./mvnw`.

## API documentation

With the server running, open Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

All responses use a common envelope:

```json
{
  "code": 1000,
  "message": "Request completed successfully!",
  "result": {}
}
```

## Authentication and roles

Public endpoints:

```text
POST /api/v1/auth/register
POST /api/v1/auth/login
POST /api/v1/auth/refresh

GET  /api/v1/categories
GET  /api/v1/categories/{slug}
GET  /api/v1/categories/{categorySlug}/products
GET  /api/v1/products/{slug}
```

All other endpoints require:

```http
Authorization: Bearer <access-token>
```

| Role | Current access |
| --- | --- |
| `CUSTOMER` | Own profile, password, and delivery addresses. |
| `STAFF` | Read Admin users; manage Admin Categories and Products. |
| `ADMIN` | All current Admin operations, including User updates, roles, and statuses. |

Security is enforced by Spring Security and method-level `@PreAuthorize` annotations.

## API overview

### Customer account

```text
GET   /api/v1/profile
PUT   /api/v1/profile
PATCH /api/v1/auth/password

GET    /api/v1/addresses
POST   /api/v1/addresses
PUT    /api/v1/addresses/{addressId}
DELETE /api/v1/addresses/{addressId}
```

### Public catalog

```text
GET /api/v1/categories
GET /api/v1/categories/{slug}
GET /api/v1/categories/{categorySlug}/products?cursor=&size=
GET /api/v1/products/{slug}
```

Public product lists use cursor pagination. The first request has no cursor; each response can return `nextCursor` and `hasNext`.

### Admin catalog

```text
GET   /api/v1/admin/categories?status=&keyword=
GET   /api/v1/admin/categories/{categoryId}
POST  /api/v1/admin/categories
PUT   /api/v1/admin/categories/{categoryId}
PATCH /api/v1/admin/categories/{categoryId}/status

GET   /api/v1/admin/products?categoryId=&status=&keyword=&page=&size=
GET   /api/v1/admin/products/{productId}
POST  /api/v1/admin/products
PUT   /api/v1/admin/products/{productId}
PATCH /api/v1/admin/products/{productId}/status
```

Admin Product pagination is zero-based: `page=0` is the first page; `size` defaults to `20` and cannot exceed `100`.

### Admin users

```text
GET   /api/v1/admin/users?status=&roleName=&keyword=&page=&size=
GET   /api/v1/admin/users/{userId}
PUT   /api/v1/admin/users/{userId}
PATCH /api/v1/admin/users/{userId}/status
PATCH /api/v1/admin/users/{userId}/role
```

## Project structure

```text
src/main/java/com/huysg136/cirquo_server/
├── auth/       # Registration, login, refresh token, JWT security
├── catalog/    # Categories, Products, variants/images in progress
├── user/       # Profile, addresses, roles, permissions, admin users
├── common/     # API envelope, base controller, page response
├── config/     # Spring Security configuration
├── exception/  # Error codes and global exception handling
└── annotation/ # Reusable validation annotations

src/main/resources/
├── application.yaml
└── db/migration/  # Flyway SQL migrations
```

## Database and migrations

Flyway is the source of truth for the schema:

```text
V1  User access: roles, permissions, users, delivery addresses
V2  Catalog: categories, products, product_variants, product_images
V3  Product category cursor-pagination index
```

Never edit a migration that already appears in `flyway_schema_history`. Create a new migration instead, for example `V4__add_product_image_updated_at.sql`.

## Catalog model

```text
Category
  └── Product
        ├── ProductVariant  # SKU, price, compare-at price, attributes, stock
        └── ProductImage    # Product image or optional Variant-specific image
```

The Variant/Image tables exist in the schema; their services and APIs are the next implementation step.

## Error handling

| Code | Meaning |
| --- | --- |
| `1001` | Validation error |
| `1106` | Authentication required |
| `1107` | Access denied |
| `1201` | Email already exists |
| `1402` | Category slug already exists |
| `1404` | Product slug already exists |

Unexpected errors return `1099`; the server logs a stack trace without exposing internal details to clients.

## Roadmap

- Complete Product Variant CRUD and stock rules.
- Complete Product Image management, primary-image selection, ordering, and external media storage.
- Replace storefront mock catalog data with public API data.
- Implement cart, checkout, orders, payment, shipping, reviews, loyalty, and recommendations.
- Add automated integration tests and CI.

## Related project

See [Cirquo Client](../cirquo-client) for the React application and frontend setup.
