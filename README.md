# 📘 MCP Server — Spring Boot + GraphQL + PostgreSQL + Docker

**MCP Server** is the backend component implementing the **Model Context Protocol (MCP)** on top of **Spring Boot**, with **GraphQL** and **PostgreSQL** support.
It provides a set of **mutation tools** for managing books and authors, along with a **GraphQL endpoint** that allows dynamic field selection in queries.

---

## 🧠 Architecture

* **LLM** (DeepSeek) communicates via the MCP Client.
* **MCP Client** determines which tool should be executed.
* **MCP Server** receives the request, performs mutations and executes GraphQL queries.
* **GraphQL** enables fetching only the required fields.
* **PostgreSQL** stores entities `Book` and `Author`.

---

## ⚙️ Technology Stack

| Component        | Technology                   |
| ---------------- | ---------------------------- |
| Language         | Java 21                      |
| Framework        | Spring Boot 3.5.x            |
| Protocol         | MCP (Model Context Protocol) |
| API              | GraphQL                      |
| Reactive Stack   | Spring WebFlux               |
| ORM              | Spring Data JPA              |
| Database         | PostgreSQL 15                |
| AI Integration   | Spring AI                    |
| Build Tool       | Maven                        |
| Containerization | Docker + Docker Compose      |

---

## 📦 Clone the Project

```bash
git clone https://github.com/iivvakii/McpServer
cd mcp-server
```

---

## 🚀 Run Options

### 🔹 Option 1 — Local Run (without Docker)

#### 1️⃣ Requirements

* **Java 21+**
* **Maven 3.9+**
* **PostgreSQL** installed locally

#### 2️⃣ Create a Database

Create a PostgreSQL database manually or using a tool like pgAdmin.

#### 3️⃣ Configure `application.properties`

`src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

#### 4️⃣ Build and Run the Application

```bash
mvn clean package
java -jar target/McpServer-0.0.1-SNAPSHOT.jar
```

---

### 🔹 Option 2 — Run with Docker Compose

#### 1️⃣ Build the jar file

```bash
mvn clean package -DskipTests
```

#### 2️⃣ Start Docker Compose

```bash
docker compose up --build
```

#### 3️⃣ Check container status

```bash
docker ps
```

Expected services:

```
postgres      → port 5432
mcp_server    → port 8080
```

---

## 📘 Dockerfile

```dockerfile
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app
COPY target/McpServer-0.0.1-SNAPSHOT.jar /app/mcp-server.jar
EXPOSE 8080
CMD ["java", "-jar", "mcp-server.jar"]
```

---

## 🧱 docker-compose.yml

```yaml
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
      POSTGRES_DB: postgres
    ports:
      - "5432:5432"
    networks:
      - mcp_network

  mcp_server:
    build:
      context: .
      dockerfile: dockerfile
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/postgres
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: password
    ports:
      - "8080:8080"
    networks:
      - mcp_network

networks:
  mcp_network:
    driver: bridge
    attachable: true
```

---

## 🧩 Core Components

| Class                                | Description                                  |
| ------------------------------------ | -------------------------------------------- |
| `BookTools`                          | Tools for creating, updating, deleting books |
| `AuthorTools`                        | Tools for managing authors                   |
| `GraphQLTools`                       | Generic tool for executing GraphQL queries   |
| `BookService`, `AuthorService`       | Business logic                               |
| `BookController`, `AuthorController` | GraphQL controllers                          |
| `McpServerApplication`               | Application entry point                      |

---

## 🧠 MCP Tools

| Tool                                    | Description                         |
| --------------------------------------- | ----------------------------------- |
| `createBook(title, authorIds)`          | Creates a new book                  |
| `updateBook(bookId, newTitle)`          | Updates a book title                |
| `addAuthorsToBook(bookId, authorIds)`   | Adds authors to a book              |
| `deleteBook(bookId)`                    | Deletes a book                      |
| `createAuthor(name, bookIds)`           | Creates a new author                |
| `updateAuthor(authorId, newName)`       | Updates an author's name            |
| `addBooksToAuthor(authorId, bookIds)`   | Links books to an author            |
| `deleteAuthor(authorId)`                | Deletes an author                   |
| `executeGraphQLQuery(query, variables)` | Executes an arbitrary GraphQL query |

---

## 🧾 License

MIT License © 2025
