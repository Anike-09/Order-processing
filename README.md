# 🧾 Order Processing System (Spring Boot + Apache Camel + ActiveMQ)

## 📌 Overview

This project is a **Mini Order Processing System** built using **Java, Spring Boot, Apache Camel, and ActiveMQ**. It is designed as per the *Cashinvoice Java Developer Coding Assignment* and demonstrates REST API design, in-memory storage, file-based integration, messaging, and JWT-based security.

The application supports:

* Creating and retrieving orders via REST APIs
* In-memory order storage
* Writing order data to files
* Asynchronous processing using Apache Camel and ActiveMQ
* JWT-based Authentication & Role-based Authorization

---

## 🏗 High-Level Architecture

```
Client (REST API)
     |
     v
Spring Boot Application
     |
     |-- In-memory Order Store (ConcurrentHashMap)
     |
     |-- Write Order JSON
     v
File System (input/orders)
     |
     |-- Apache Camel Route
     v
ActiveMQ Queue (ORDER.CREATED.QUEUE)
     |
     |-- Apache Camel Consumer
     v
Application Logs
```

---

## ⚙️ Tech Stack

* **Java**: 8+
* **Framework**: Spring Boot
* **Security**: Spring Security + JWT
* **Integration**: Apache Camel
* **Messaging**: ActiveMQ
* **Storage**: In-memory (ConcurrentHashMap)
* **Build Tool**: Maven
* **Logging**: SLF4J + Logback

---

## 🚀 How to Run the Application

### 1️⃣ Prerequisites

* Java 8 or higher
* Maven
* ActiveMQ (running locally)

Default ActiveMQ URL:

```
tcp://localhost:61616
```

### 2️⃣ Start ActiveMQ

```
activemq start
```

### 3️⃣ Run Spring Boot App

```
mvn spring-boot:run
```

Application will start on:

```
http://localhost:8080
```

---

## 🔐 Authentication & Authorization

### Roles

* **ADMIN** → Can create and view all orders
* **USER** → Can create orders and view only their own orders

### In-Memory Users

| Username | Password | Role  |
| -------- | -------- | ----- |
| admin    | admin123 | ADMIN |
| user1    | user123  | USER  |
| CUST1001 | cust123  | USER  |
| CUST1002 | cust123  | USER  |

---

## 🔑 Login API (JWT)

**Endpoint**

```
POST /api/auth/login
```

**Request Body**

```json
{
  "userName": "CUST1001",
  "password": "cust123"
}
```

**Response**

```json
{
  "jwtToken": "<JWT_TOKEN>",
  "userName": "CUST1001",
  "role": "USER"
}
```

Use the token in headers:

```
Authorization: Bearer <JWT_TOKEN>
```

---

## 📦 Order APIs

### ✅ Create Order

**Endpoint**

```
POST /api/orders
```

**Request Body**

```json
{
  "customerId": "CUST1001",
  "product": "Laptop",
  "amount": 75000
}
```

**Response (201 CREATED)**

```json
{
  "orderId": "<uuid>",
  "status": "CREATED"
}
```

---

### 🔍 Get Order by ID

**Endpoint**

```
GET /api/orders/{orderId}
```

* Returns order details if found
* Returns **404 NOT FOUND** if order does not exist

---

### 📄 List Orders

**By Customer**

```
GET /api/orders?customerId=CUST1001
```

**Admin (All Orders)**

```
GET /api/orders
```

---

## 📁 File Output (Bonus)

After successful order creation:

* Order is converted to JSON
* Written to directory:

```
input/orders/
```

**File Name Format**

```
order-<orderId>.json
```

---

## 🔁 Apache Camel Routes (Bonus)

### 🟢 File → ActiveMQ Route

* Polls files from `input/orders`
* Reads JSON payload
* Converts to `Order` object
* Validates:

  * orderId not null
  * customerId not null
  * amount > 0
* Sends message to ActiveMQ queue:

```
ORDER.CREATED.QUEUE
```

### 🔴 Failure Flow

* On exception, file is moved to:

```
error/orders
```

* Error details are logged

---

### 🟣 ActiveMQ Consumer Route

* Consumes messages from `ORDER.CREATED.QUEUE`
* Deserializes JSON to Order
* Logs:

```
Order processed | OrderId=xxx | CustomerId=CUST1001 | Amount=75000
```


