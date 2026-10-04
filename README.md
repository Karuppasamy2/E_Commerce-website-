# E-Commerce Website

A backend application for managing products in an e-commerce system, built using **Java and Spring Boot**.

The project provides REST APIs for creating, viewing, updating, deleting, and searching products. It also supports uploading and retrieving product images through the API.

## Features

* Add new products
* View all products
* View a single product by ID
* Update existing products
* Delete products
* Search products using keywords
* Search by product name, description, brand, or category
* Upload product images
* Retrieve product images
* Store product information using JPA/Hibernate
* REST API based backend
* Layered application structure

---

## Tech Stack

| Technology      | Usage                       |
| --------------- | --------------------------- |
| Java            | Application development     |
| Spring Boot     | Backend framework           |
| Spring Web      | REST API development        |
| Spring Data JPA | Database interaction        |
| Hibernate       | ORM                         |
| Lombok          | Reducing boilerplate code   |
| Maven           | Dependency management       |
| JSON            | API request/response format |

---

## Architecture

The application follows a simple layered architecture.

```text
                         E-COMMERCE BACKEND
                                  │
                                  │ HTTP Request
                                  ▼
                    ┌─────────────────────────┐
                    │    ProductController    │
                    │                         │
                    │  REST API Endpoints     │
                    │                         │
                    │  • Get products         │
                    │  • Get product by ID    │
                    │  • Add product          │
                    │  • Update product       │
                    │  • Delete product       │
                    │  • Search products      │
                    │  • Get product image    │
                    └────────────┬────────────┘
                                 │
                                 │ Method calls
                                 ▼
                    ┌─────────────────────────┐
                    │      ProductService     │
                    │                         │
                    │  • Product operations   │
                    │  • Business logic       │
                    │  • Image processing     │
                    │  • Search handling      │
                    └────────────┬────────────┘
                                 │
                                 │ Repository calls
                                 ▼
                    ┌─────────────────────────┐
                    │    ProductRepository    │
                    │                         │
                    │   Spring Data JPA       │
                    │                         │
                    │   • CRUD operations     │
                    │   • Custom search query │
                    └────────────┬────────────┘
                                 │
                                 │ JPA / Hibernate
                                 ▼
                    ┌─────────────────────────┐
                    │        DATABASE         │
                    │                         │
                    │        Product          │
                    │                         │
                    │  Product details        │
                    │  Stock information      │
                    │  Image information      │
                    │  Image binary data      │
                    └─────────────────────────┘
```

### How a request moves through the application

For example, when a client requests a product:

```text
GET /api/product/1
       │
       ▼
ProductController
       │
       ▼
ProductService
       │
       ▼
ProductRepository
       │
       ▼
Database
       │
       ▼
Product
       │
       ▼
HTTP Response
```

The controller handles the HTTP request, the service performs the required operation, and the repository communicates with the database.

---

## Project Structure

```text
e_commerce/
│
├── config/
│
├── controller/
│   └── ProductController.java
│
├── model/
│   └── Product.java
│
├── repo/
│   └── ProductRepository.java
│
├── service/
│   └── ProductService.java
│
└── ECommerceApplication.java
```

### `controller`

Contains the REST controller responsible for receiving requests and returning responses.

**`ProductController.java`**

Handles the product-related API endpoints.

### `service`

Contains the application logic for product operations.

**`ProductService.java`**

Acts as the middle layer between the controller and repository.

### `repo`

Contains the repository used to communicate with the database.

**`ProductRepository.java`**

Uses Spring Data JPA and also contains the custom product search query.

### `model`

Contains the entity classes used by the application.

**`Product.java`**

Represents a product stored in the database.

### `config`

Contains configuration-related classes for the application.

---

# Product Model

The `Product` entity contains the following information:

| Field              | Description                                |
| ------------------ | ------------------------------------------ |
| `id`               | Unique identifier for the product          |
| `name`             | Name of the product                        |
| `description`      | Description of the product                 |
| `brand`            | Product brand                              |
| `price`            | Price of the product                       |
| `category`         | Product category                           |
| `releaseDate`      | Product release date                       |
| `productAvailable` | Indicates whether the product is available |
| `stockQuantity`    | Number of products currently in stock      |
| `imageName`        | Name of the uploaded image                 |
| `imageType`        | Image content type                         |
| `imageDate`        | Image data stored as binary data           |

The ID is automatically generated by the database using JPA.

---

# REST API

The application exposes its product APIs under:

```text
/api
```

## Get All Products

```http
GET /api/products
```

Returns all products available in the database.

### Example

```http
GET http://localhost:8080/api/products
```

---

## Get Product by ID

```http
GET /api/product/{id}
```

Returns a product using its ID.

### Example

```http
GET http://localhost:8080/api/product/1
```

---

## Add Product

```http
POST /api/product
```

Creates a new product.

This endpoint accepts `multipart/form-data` so that product information and an image can be sent together.

### Request parts

```text
product
imageFile
```

Example product data:

```json
{
  "name": "Laptop",
  "description": "Laptop for everyday use",
  "brand": "Example",
  "price": 55000,
  "category": "Electronics",
  "releaseDate": "2026-01-15",
  "productAvailable": true,
  "stockQuantity": 10
}
```

---

## Get Product Image

```http
GET /api/product/{id}/image
```

Returns the image associated with a product.

### Example

```http
GET http://localhost:8080/api/product/1/image
```

---

## Update Product

```http
PUT /api/product/{id}
```

Updates an existing product.

The endpoint can also receive an updated image.

### Example

```http
PUT /api/product/1
```

---

## Delete Product

```http
DELETE /api/product/{id}
```

Deletes a product from the database.

### Example

```http
DELETE http://localhost:8080/api/product/1
```

---

# Product Search

The project includes a keyword-based product search API.

```http
GET /api/products/search?keyword={keyword}
```

### Example

```http
GET http://localhost:8080/api/products/search?keyword=laptop
```

The search checks the keyword against multiple product fields:

```text
                  Search Keyword
                        │
          ┌─────────────┼─────────────┐
          ▼             ▼             ▼
        Name      Description       Brand
          │             │             │
          └─────────────┼─────────────┘
                        │
                        ▼
                     Category
```

The search is implemented using a custom query in `ProductRepository`.

This makes it possible to search for a product without knowing its exact name.

---

# Image Upload

Product images are handled using Spring's `MultipartFile`.

When a product is added, the application extracts information from the uploaded image:

```text
Uploaded Image
      │
      ├── Original filename
      │
      ├── Content type
      │
      └── Image bytes
```

The image information is stored along with the product.

The image itself is stored as binary data using a JPA `@Lob` field.

Images can then be retrieved through:

```http
GET /api/product/{id}/image
```

---

# Database Interaction

The application uses **Spring Data JPA** for database operations.

The repository extends Spring Data's CRUD repository, which provides commonly required operations such as:

```text
Create
Read
Update
Delete
```

The application does not need to manually write SQL for these basic operations.

For product searching, a custom JPQL query is used to check multiple product fields.

---

# API Flow

### Adding a product

```text
Client
  │
  │ POST /api/product
  │
  │ Product + Image
  ▼
ProductController
  │
  ▼
ProductService
  │
  │ Process product
  │ Process image
  ▼
ProductRepository
  │
  ▼
Database
```

### Searching for products

```text
Client
  │
  │ GET /api/products/search?keyword=phone
  ▼
ProductController
  │
  ▼
ProductService
  │
  ▼
ProductRepository
  │
  │ Custom JPQL Query
  ▼
Database
  │
  ▼
Matching Products
  │
  ▼
Client
```

---

# Getting Started

## Prerequisites

Before running the project, make sure you have:

* Java installed
* Maven installed
* A relational database
* An IDE such as IntelliJ IDEA, Eclipse, or VS Code
* Postman or another API testing tool

You can check Java with:

```bash
java -version
```

And Maven with:

```bash
mvn -version
```

---

# Installation

Clone the repository:

```bash
git clone https://github.com/Karuppasamy2/E_Commerce-website-.git
```

Move into the project directory:

```bash
cd E_Commerce-website-
```

Configure the database connection in the Spring Boot application configuration.

Example:

```properties
spring.datasource.url=your_database_url
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Use your own database URL, username, and password.

---

# Running the Application

The application can be started using Maven:

```bash
mvn spring-boot:run
```

Or run the main class from your IDE:

```text
ECommerceApplication.java
```

After the application starts, the API can be accessed through the configured Spring Boot port.

For the default port:

```text
http://localhost:8080
```

---

# Testing with Postman

The APIs can be tested using Postman.

Some basic requests to try:

```text
GET     /api/products
GET     /api/product/1
POST    /api/product
PUT     /api/product/1
DELETE  /api/product/1
GET     /api/products/search?keyword=laptop
GET     /api/product/1/image
```

For the product creation and update APIs, use `form-data` to send the product information and image.

---

# Example Workflow

A basic product management workflow looks like this:

```text
1. Add Product
      │
      ▼
2. Product stored in database
      │
      ▼
3. Retrieve products
      │
      ▼
4. Search for products
      │
      ▼
5. Update product
      │
      ▼
6. Delete product when no longer needed
```

---

# What I Worked On

This project helped me get hands-on experience with building a backend using Spring Boot.

Some of the main areas covered were:

* Designing REST APIs
* Working with HTTP methods
* Creating Spring Boot controllers
* Separating application logic into service and repository layers
* Using Spring Data JPA
* Mapping Java classes to database tables
* Performing CRUD operations
* Writing custom database queries
* Handling multipart file uploads
* Working with product images
* Returning data through REST APIs

---

# Future Improvements

The current project focuses mainly on product management. Some features that can be added to make it a more complete e-commerce application are:

* User registration and login
* Spring Security
* JWT-based authentication
* Role-based access for users and admins
* Shopping cart
* Wishlist
* Order management
* Payment integration
* Product reviews and ratings
* Product pagination
* Filtering and sorting
* Admin dashboard
* Better validation
* Global exception handling
* API documentation using Swagger/OpenAPI
* External image storage

---

# Screenshots

Screenshots can be added here to show the API responses and application interface.

Example:

```text
docs/
├── products.png
├── add-product.png
├── search.png
└── product-image.png
```

Then they can be included in the README using:

```markdown
![Products API](docs/products.png)
```

---

# Future Project Direction

The current backend provides the foundation for a larger e-commerce application.

The next step would be to build the user-facing frontend and extend the backend with authentication, cart management, orders, and payments.

The architecture is already separated into controller, service, repository, and model layers, which makes it easier to add these features without putting all the application logic into a single class.

---

# Author

**Karuppasamy V**

GitHub: [Karuppasamy2](https://github.com/Karuppasamy2)

---

## License

This project was created for learning and development purposes.
