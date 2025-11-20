# Warehouse Management System

A RESTful API for managing a shop's warehouse inventory, built with Spring Boot 3.x and Java 17.

## 📋 Features

- **Item Management**: Complete CRUD operations for items
- **Variant Support**: Each item can have multiple variants (sizes, colors, etc.)
- **Stock Control**: Track inventory levels and prevent overselling
- **Price Management**: Individual pricing for items and variants
- **Search & Filter**: Find items by name or availability
- **Stock Operations**: Increase and reduce stock with validation

## 🛠️ Technologies Used

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- MySQL 8.0
- Lombok
- Maven
- Docker & Docker Compose

## 📐 Architecture & Design Decisions

### Database Schema
The system uses a relational database with two main entities:

1. **Items Table**
   - Core product information
   - Base price and stock level
   - One-to-many relationship with variants

2. **Variants Table**
   - Product variations (e.g., "Red - Large", "Blue - Small")
   - Individual pricing and stock tracking
   - Many-to-one relationship with items

### Design Patterns
- **Repository Pattern**: Data access abstraction
- **Service Layer**: Business logic separation
- **DTO Pattern**: Through entity validation
- **RESTful API**: Standard HTTP methods and status codes

### Key Features Implementation

#### Stock Prevention
The system validates stock before any sale/reduction:
```java
if (item.getStock() < quantity) {
    throw new RuntimeException("Insufficient stock...");
}
```

#### Cascade Operations
When an item is deleted, all its variants are automatically removed using `CascadeType.ALL` and `orphanRemoval = true`.

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Docker & Docker Compose (recommended)
- MySQL 8.0 (if not using Docker)

### Installation & Running

#### Option 1: Using Docker (Recommended)

1. **Clone the repository**
```bash
git clone <your-repo-url>
cd warehouse-management
```

2. **Start MySQL and phpMyAdmin**
```bash
docker-compose up -d
```

This will start:
- MySQL on `localhost:3306`
- phpMyAdmin on `http://localhost:8081`

3. **Run the application**
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

#### Option 2: Using Local MySQL

1. **Install MySQL and create database**
```sql
CREATE DATABASE warehouse_db;
```

2. **Update application.properties**
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. **Run the application**
```bash
mvn spring-boot:run
```

## 📚 API Endpoints

### Items

#### Get all items
```http
GET /api/items
```

#### Get item by ID
```http
GET /api/items/{id}
```

#### Search items by name
```http
GET /api/items/search?name=laptop
```

#### Get available items (stock > 0)
```http
GET /api/items/available
```

#### Create new item
```http
POST /api/items
Content-Type: application/json

{
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 15000000,
  "stock": 10
}
```

#### Update item
```http
PUT /api/items/{id}
Content-Type: application/json

{
  "name": "Laptop Pro",
  "description": "Updated description",
  "price": 18000000,
  "stock": 15
}
```

#### Delete item
```http
DELETE /api/items/{id}
```

#### Reduce stock
```http
PATCH /api/items/{id}/reduce-stock
Content-Type: application/json

{
  "quantity": 2
}
```

#### Increase stock
```http
PATCH /api/items/{id}/increase-stock
Content-Type: application/json

{
  "quantity": 5
}
```

### Variants

#### Get all variants
```http
GET /api/variants
```

#### Get variant by ID
```http
GET /api/variants/{id}
```

#### Get variants by item ID
```http
GET /api/variants/item/{itemId}
```

#### Get available variants (stock > 0)
```http
GET /api/variants/available
```

#### Create new variant for an item
```http
POST /api/variants/item/{itemId}
Content-Type: application/json

{
  "name": "Red - Large",
  "description": "Large size in red color",
  "price": 15500000,
  "stock": 5
}
```

#### Update variant
```http
PUT /api/variants/{id}
Content-Type: application/json

{
  "name": "Red - XL",
  "description": "Extra large size in red",
  "price": 16000000,
  "stock": 8
}
```

#### Delete variant
```http
DELETE /api/variants/{id}
```

#### Reduce variant stock
```http
PATCH /api/variants/{id}/reduce-stock
Content-Type: application/json

{
  "quantity": 1
}
```

#### Increase variant stock
```http
PATCH /api/variants/{id}/increase-stock
Content-Type: application/json

{
  "quantity": 3
}
```

## 🧪 Example Usage

### Creating a Complete Product with Variants

1. **Create base item**
```bash
curl -X POST http://localhost:8080/api/items \
  -H "Content-Type: application/json" \
  -d '{
    "name": "T-Shirt",
    "description": "Cotton T-Shirt",
    "price": 150000,
    "stock": 50
  }'
```

Response:
```json
{
  "id": 1,
  "name": "T-Shirt",
  "description": "Cotton T-Shirt",
  "price": 150000,
  "stock": 50,
  "variants": []
}
```

2. **Add variants**
```bash
curl -X POST http://localhost:8080/api/variants/item/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Red - Small",
    "description": "Small size, red color",
    "price": 150000,
    "stock": 10
  }'

curl -X POST http://localhost:8080/api/variants/item/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Blue - Large",
    "description": "Large size, blue color",
    "price": 160000,
    "stock": 15
  }'
```

3. **Sell a variant (reduce stock)**
```bash
curl -X PATCH http://localhost:8080/api/variants/1/reduce-stock \
  -H "Content-Type: application/json" \
  -d '{"quantity": 2}'
```

## 🔒 Validation Rules

- **Name**: Required, cannot be blank
- **Price**: Required, must be zero or positive
- **Stock**: Required, must be zero or positive
- **Stock Reduction**: Cannot reduce more than available stock

## 🎯 Assumptions

1. **Stock Management**: Both items and variants track their own stock independently
2. **Pricing Flexibility**: Variants can have different prices from their parent items
3. **No Authentication**: This is a backend-only assessment without authentication
4. **Single Currency**: All prices are in the same currency (e.g., IDR)
5. **No Order System**: Direct stock manipulation instead of order processing
6. **Cascade Delete**: Deleting an item removes all its variants

## 📝 Design Choices Explained

### Why Both Item and Variant Have Stock?
- **Flexibility**: Some shops sell base items without variants
- **Independent Tracking**: Variants can have different stock levels
- **Real-world Mapping**: Matches common e-commerce patterns

### Why BigDecimal for Price?
- **Precision**: Accurate decimal calculations for money
- **No Rounding Errors**: Critical for financial calculations

### Why Validation Annotations?
- **Declarative**: Clear constraints at the model level
- **Automatic**: Spring Boot validates before processing
- **Maintainable**: Easy to update validation rules

### Why Separate Controllers?
- **Separation of Concerns**: Clear responsibility boundaries
- **Scalability**: Easy to add features per entity
- **RESTful Design**: Resource-based URL structure

## 🐛 Error Handling

The API returns appropriate HTTP status codes:
- `200 OK`: Successful GET, PUT, PATCH
- `201 Created`: Successful POST
- `400 Bad Request`: Validation errors, insufficient stock
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server errors

## 🔧 Configuration

### Database Configuration
Edit `src/main/resources/application.properties`:

```properties
# Change database credentials
spring.datasource.username=your_username
spring.datasource.password=your_password

# Change port
server.port=8080

# Change database name
spring.datasource.url=jdbc:mysql://localhost:3306/your_db_name
```

## 📦 Project Structure

```
warehouse-management/
├── src/
│   ├── main/
│   │   ├── java/com/geli/warehouse/
│   │   │   ├── controller/
│   │   │   │   ├── ItemController.java
│   │   │   │   └── VariantController.java
│   │   │   ├── model/
│   │   │   │   ├── Item.java
│   │   │   │   └── Variant.java
│   │   │   ├── repository/
│   │   │   │   ├── ItemRepository.java
│   │   │   │   └── VariantRepository.java
│   │   │   ├── service/
│   │   │   │   ├── ItemService.java
│   │   │   │   └── VariantService.java
│   │   │   └── WarehouseManagementApplication.java
│   │   └── resources/
│   │       └── application.properties
├── docker-compose.yml
├── pom.xml
└── README.md
```
