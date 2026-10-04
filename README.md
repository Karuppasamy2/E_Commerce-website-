# E-Commerce Backend

A backend REST API for managing products in an e-commerce system, built with **Java 17 and Spring Boot 4**.

Supports full product CRUD, image upload and retrieval, keyword-based search, user registration, and role-based access control using Spring Security.

---

## Tech Stack

| Technology          | Usage                        |
| ------------------- | ---------------------------- |
| Java 17             | Application language         |
| Spring Boot 4.1.0   | Backend framework            |
| Spring Web          | REST API                     |
| Spring Data JPA     | Database interaction         |
| Spring Security     | Authentication & authorization |
| Hibernate           | ORM                          |
| H2 Database         | In-memory database           |
| Lombok              | Boilerplate reduction        |
| Maven               | Dependency management        |

---

## Project Structure

```
e_commerce/
│
├── config/
│   ├── SecurityConfig.java
│   └── PasswordConfig.java
│
├── controller/
│   ├── ProductController.java
│   └── UserController.java
│
├── model/
│   ├── Product.java
│   └── User.java
│
├── repo/
│   ├── ProductRepository.java
│   └── UserRepository.java
│
├── service/
│   ├── ProductService.java
│   └── UserService.java
│
└── ECommerceApplication.java
```

---

## Architecture

```
HTTP Request
     │
     ▼
Controller  (ProductController / UserController)
     │
     ▼
Service     (ProductService / UserService)
     │
     ▼
Repository  (ProductRepository / UserRepository)
     │
     ▼
H2 Database
```

---

## Models

### Product

| Field              | Type         | Description                        |
| ------------------ | ------------ | ---------------------------------- |
| `id`               | Integer      | Auto-generated primary key         |
| `name`             | String       | Product name                       |
| `description`      | String       | Product description                |
| `brand`            | String       | Product brand                      |
| `price`            | BigDecimal   | Product price                      |
| `category`         | String       | Product category                   |
| `releaseDate`      | LocalDate    | Release date (`yyyy-MM-dd`)        |
| `productAvailable` | boolean      | Availability status                |
| `stockQuantity`    | Integer      | Stock count                        |
| `imageName`        | String       | Original image filename            |
| `imageType`        | String       | Image MIME type                    |
| `imageDate`        | byte[]       | Image binary data (`@Lob`)         |

### User

| Field      | Type    | Description              |
| ---------- | ------- | ------------------------ |
| `id`       | Integer | Auto-generated primary key |
| `username` | String  | Unique username          |
| `password` | String  | BCrypt hashed password   |
| `role`     | String  | `USER` or `ADMIN`        |

---

## Security

The application uses **Spring Security with DAO authentication** (database-backed).

Passwords are hashed using **BCrypt**.

Authentication is done via **HTTP Basic Auth** on every request.

### Access Rules

| Endpoint                        | USER | ADMIN |
| ------------------------------- | ---- | ----- |
| `POST /api/register`            | ✅ public | ✅ public |
| `GET /api/products`             | ✅   | ✅    |
| `GET /api/product/{id}`         | ✅   | ✅    |
| `GET /api/product/{id}/image`   | ✅   | ✅    |
| `GET /api/products/search`      | ✅   | ✅    |
| `POST /api/product`             | ❌   | ✅    |
| `PUT /api/product/{id}`         | ❌   | ✅    |
| `DELETE /api/product/{id}`      | ❌   | ✅    |
| `/h2-console/**`                | ✅ public | ✅ public |

---

## REST API

Base URL: `http://localhost:8080/api`

---

### Register User

```http
POST /api/register
Content-Type: application/json
```

```json
{
  "username": "admin",
  "password": "admin123",
  "role": "ADMIN"
}
```

```json
{
  "username": "user",
  "password": "user123",
  "role": "USER"
}
```

> Register users before calling any protected endpoint.

---

### Get All Products

```http
GET /api/products
Authorization: Basic <credentials>
```

---

### Get Product by ID

```http
GET /api/product/{id}
Authorization: Basic <credentials>
```

---

### Add Product

```http
POST /api/product
Content-Type: multipart/form-data
Authorization: Basic admin:admin123
```

Form parts:

| Part        | Type              | Description              |
| ----------- | ----------------- | ------------------------ |
| `product`   | application/json  | Product JSON data        |
| `imageFile` | file              | Product image            |

Example product JSON:

```json
{
  "name": "Laptop",
  "description": "Everyday use laptop",
  "brand": "Example",
  "price": 55000,
  "category": "Laptop",
  "releaseDate": "2024-01-15",
  "productAvailable": true,
  "stockQuantity": 10
}
```

---

### Get Product Image

```http
GET /api/product/{id}/image
Authorization: Basic <credentials>
```

---

### Update Product

```http
PUT /api/product/{id}
Content-Type: multipart/form-data
Authorization: Basic admin:admin123
```

Same form parts as Add Product.

---

### Delete Product

```http
DELETE /api/product/{id}
Authorization: Basic admin:admin123
```

---

### Search Products

```http
GET /api/products/search?keyword={keyword}
Authorization: Basic <credentials>
```

Searches across `name`, `description`, `brand`, and `category` fields (case-insensitive).

Example:

```http
GET /api/products/search?keyword=laptop
```

---

## H2 Console

```
URL:      http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:e-commerce
Username: sa
Password: (leave blank)
```

---

## Frontend

A React + Vite frontend is included under `ecom-frontend-3-main/`.

To run it:

```bash
cd ecom-frontend-3-main/ecom-frontend-3-main
npm install
npm run dev
```

Runs at: `http://localhost:5173`

When calling protected endpoints from the frontend, pass Basic Auth credentials:

```js
axios.post("http://localhost:8080/api/product", formData, {
    auth: { username: "admin", password: "admin123" }
})
```

---

## Getting Started

### Prerequisites

- Java 17
- Maven
- Node.js (for frontend)

### Run the Backend

```bash
mvn spring-boot:run
```

Backend runs at: `http://localhost:8080`

### First Steps After Starting

1. Register an admin user:

```bash
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123","role":"ADMIN"}'
```

2. Register a regular user:

```bash
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user123","role":"USER"}'
```

3. Start adding and viewing products.

---

## Configuration

`src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:e-commerce
spring.datasource.driverClassName=org.h2.Driver
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

> The H2 database is in-memory. All data is lost when the application stops. To persist data, switch to a file-based or external database.

---

## Author

**Karuppasamy V**

GitHub: [Karuppasamy2](https://github.com/Karuppasamy2)

---

## License

This project was created for learning and development purposes.
