# Wallet MySQL Auth Valid

A simple Spring Boot backend wallet application with authentication, validation, and MySQL integration.

## Features
- RESTful API for wallet CRUD operations
- Basic authentication (HTTP Basic Auth)
- Input validation
- MySQL database integration

## Requirements
- Java 17+
- Maven
- MySQL

## Getting Started

### 1. Clone the repository
## Entity Validation

The `Wallet` entity uses validation annotations to ensure data integrity:

- `@NotBlank` on `ownerName` (requires `import jakarta.validation.constraints.NotBlank;` in your entity)
- `@Min(0)` and `@NotNull` on `balance`
- `@NotNull` and `@Size(min = 3, max = 3)` (if used) on `currency`

If you send invalid data, you will receive a 400 Bad Request with field-level error messages, e.g.:

```json
{
  "ownerName": "Owner name must not be blank",
  "balance": "Balance must be at least 0",
  "currency": "Currency must be exactly 3 characters long"
}
```
<img width="829" height="453" alt="image" src="https://github.com/user-attachments/assets/2949244f-7afb-4516-a905-e8aab8eae78b" />

```
git clone <your-repo-url>
cd wallet_mysql_auth_valid
```

### 2. Configure the Database
- Create a MySQL database named `walletdb`:
  ```sql
  CREATE DATABASE walletdb;
  ```
- Update `src/main/resources/application.properties` with your MySQL username and password if needed.

### 3. Build and Run the Application
```
mvn spring-boot:run
```

The app will start on [http://localhost:8080](http://localhost:8080) by default.

### 4. API Authentication
- Default user (from code):
  - Username: `user`
  - Password: `password`
- Or, if you configure users in `application.properties`, use those credentials.

- <img width="802" height="600" alt="image" src="https://github.com/user-attachments/assets/70b77e41-24f8-4328-a2f7-04631e88eec1" />



### 5. API Endpoints


#### Create Wallet
- **POST** `/api/wallets`
- Body (JSON):
  ```json
  {
    "ownerName": "Alice",
    "balance": 100.0,
    "currency": "USD"
  }
  ```

#### Bulk Create Wallets
- **POST** `/api/wallets/bulk`
- Body (JSON array):
  ```json
  [
    {"ownerName": "Alice", "balance": 100.0, "currency": "USD"},
    {"ownerName": "Bob", "balance": 200.0, "currency": "EUR"},
    {"ownerName": "Charlie", "balance": 150.5, "currency": "GBP"}
    // ... more records ...
  ]
  ```
  Returns a list of created wallet objects. Use this to quickly add multiple wallets for testing or batch operations.

#### Get All Wallets
- **GET** `/api/wallets`

#### Get Wallet by ID
- **GET** `/api/wallets/{id}`

#### Update Wallet
- **PUT** `/api/wallets/{id}`
- Body (JSON):
  ```json
  {
    "ownerName": "Alice",
    "balance": 200.0,
    "currency": "USD"
  }
  ```

#### Delete Wallet
- **DELETE** `/api/wallets/{id}`


### 6. Testing with Postman
- Set Authorization type to **Basic Auth** with the correct username and password.
- Set `Content-Type: application/json` for POST/PUT requests.

### 7. Pagination Test Example

To test pagination, first bulk insert at least 15 wallet records using the `/api/wallets/bulk` endpoint:

**POST** `/api/wallets/bulk`
Body (JSON):
```
[
  {"ownerName": "Alice", "balance": 100.0, "currency": "USD"},
  {"ownerName": "Bob", "balance": 200.0, "currency": "EUR"},
  {"ownerName": "Charlie", "balance": 150.5, "currency": "GBP"},
  {"ownerName": "Diana", "balance": 300.0, "currency": "USD"},
  {"ownerName": "Eve", "balance": 50.0, "currency": "EUR"},
  {"ownerName": "Frank", "balance": 75.25, "currency": "USD"},
  {"ownerName": "Grace", "balance": 120.0, "currency": "GBP"},
  {"ownerName": "Heidi", "balance": 500.0, "currency": "USD"},
  {"ownerName": "Ivan", "balance": 60.0, "currency": "EUR"},
  {"ownerName": "Judy", "balance": 80.0, "currency": "USD"},
  {"ownerName": "Karl", "balance": 90.0, "currency": "GBP"},
  {"ownerName": "Laura", "balance": 110.0, "currency": "USD"},
  {"ownerName": "Mallory", "balance": 130.0, "currency": "EUR"},
  {"ownerName": "Niaj", "balance": 140.0, "currency": "USD"},
  {"ownerName": "Olivia", "balance": 160.0, "currency": "GBP"}
]
```

Then, to retrieve 12 records per page, use:

**GET** `/api/wallets?page=0&size=12`

The response will include fields like `content` (the records), `totalPages`, `totalElements`, `number` (current page), and `size` (page size). For the next page, use `page=1`.

**Example response:**
```json
{
  "content": [ ... 12 wallet records ... ],
  "totalPages": 2,
  "totalElements": 15,
  "number": 0,
  "size": 12
  // ... other fields ...
}
```
<img width="857" height="681" alt="image" src="https://github.com/user-attachments/assets/0dcc31cd-7133-4722-bfa3-84433ad6215c" />





## Exception Handling

This project includes a global exception handler (`GlobalExceptionHandler`) that provides user-friendly error responses for API clients. Here’s how each handler works:

### 1. MethodArgumentNotValidException Handler
- Handles validation errors for request bodies (e.g., invalid JSON fields in a POST/PUT).
- Triggered when you use `@Valid` or `@Validated` on a controller method parameter and the request body fails validation (e.g., missing or invalid fields).
- Example: Sending a POST with `{ "ownerName": "", "balance": -10, "currency": "" }`.
- Response: Returns a 400 Bad Request with a map of field names to error messages, in the order: ownerName, balance, currency.

### 2. ConstraintViolationException Handler
- Handles validation errors for request parameters, path variables, or programmatic validation (not request body).
- Triggered when you use validation annotations on method parameters (e.g., `@RequestParam`, `@PathVariable`, or service layer validation).
- Example: If you had a controller method like `getWallet(@RequestParam @Min(1) int id)` and called it with `id=0`.
- Response: Returns a 400 Bad Request with a map of parameter names to error messages, in the order: ownerName, balance, currency.

### 3. Generic Exception Handler
- Handles all other unhandled exceptions (fallback).
- Triggered when any other exception occurs that isn’t caught by the above handlers.
- Example: Database connection errors, null pointer exceptions, etc.
- Response: Returns a 500 Internal Server Error with a generic error message.

### How to Test the Exception Handler

#### Validation Error Example

Send a POST request to `/api/wallets` with invalid data, for example:

```json
{
  "ownerName": "",
  "balance": -10,
  "currency": ""
}
```

**Expected response (HTTP 400):**
```json
{
  "ownerName": "Owner name must not be blank",
  "balance": "Balance must be at least 0",
  "currency": "Currency must be exactly 3 characters long"
}
```
<img width="829" height="453" alt="image" src="https://github.com/user-attachments/assets/2949244f-7afb-4516-a905-e8aab8eae78b" />



#### Generic Error Example

If an unexpected error occurs (e.g., a database error), the API responds with HTTP 500 and a message:

```json
{
  "error": "Internal server error message here"
}
```

## License
MIT
