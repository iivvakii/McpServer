# 📘 MCP Server — Spring Boot + GraphQL + PostgreSQL + Docker

**MCP Server** — це серверна частина, що реалізує **Model Context Protocol (MCP)** поверх **Spring Boot**, з підтримкою **GraphQL** і **PostgreSQL**.
Він надає набір **mutation tools** для керування книгами та авторами, а також **GraphQL endpoint** для динамічного вибору полів при запитах.

---

## 🧠 Архітектура



* **LLM** (DeepSeek) взаємодіє через MCP Client.
* **MCP Client** вирішує, який тул викликати.
* **MCP Server** приймає запити, виконує операції (mutation) і GraphQL-запити.
* **GraphQL** дозволяє отримувати лише потрібні поля.
* **PostgreSQL** зберігає сутності `Book` та `Author`.

---

## ⚙️ Технологічний стек

| Компонент       | Технологія                   |
| --------------- | ---------------------------- |
| Мова            | Java 21                      |
| Фреймворк       | Spring Boot 3.5.x            |
| Протокол        | MCP (Model Context Protocol) |
| API             | GraphQL                      |
| Реактивність    | Spring WebFlux               |
| ORM             | Spring Data JPA              |
| База даних      | PostgreSQL 15                |
| AI інтеграція   | Spring AI                    |
| Збірка          | Maven                        |
| Контейнеризація | Docker + Docker Compose      |


---

## 📦 Як клонувати проєкт

```bash
git clone https://github.com/your-username/mcp-server.git
cd mcp-server
```

---

## 🚀 Способи запуску

### 🔹 Варіант 1 — Локальний запуск (без Docker)

#### 1️⃣ Вимоги

* **Java 21+**
* **Maven 3.9+**
* **PostgreSQL** встановлений локально

#### 2️⃣ Налаштуй базу даних

Створи БД PostgreSQL


#### 3️⃣ Налаштуй `application.properties`

`src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

#### 4️⃣ Збери та запусти застосунок

```bash
mvn clean package
java -jar target/McpServer-0.0.1-SNAPSHOT.jar
```

---

### 🔹 Варіант 2 — Запуск через Docker Compose

#### 1️⃣ Збери jar-файл

```bash
mvn clean package -DskipTests
```

#### 2️⃣ Запусти docker-compose

```bash
docker compose up --build
```

#### 3️⃣ Перевір статус контейнерів

```bash
docker ps
```

Очікувані сервіси:

```
postgres      → порт 5432
mcp_server    → порт 8080
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

## 🧩 Основні компоненти

| Клас                                 | Призначення                                   |
| ------------------------------------ | --------------------------------------------- |
| `BookTools`                          | Тули для створення, оновлення, видалення книг |
| `AuthorTools`                        | Тули для роботи з авторами                    |
| `GraphQLTools`                       | Універсальний тул для GraphQL-запитів         |
| `BookService`, `AuthorService`       | Бізнес-логіка                                 |
| `BookController`, `AuthorController` | GraphQL контролери                            |
| `McpServerApplication`               | Точка входу в застосунок                      |

---

## 🧠 MCP Tools

| Tool                                    | Опис                            |
| --------------------------------------- | ------------------------------- |
| `createBook(title, authorIds)`          | Створює нову книгу              |
| `updateBook(bookId, newTitle)`          | Оновлює назву книги             |
| `addAuthorsToBook(bookId, authorIds)`   | Додає авторів до книги          |
| `deleteBook(bookId)`                    | Видаляє книгу                   |
| `createAuthor(name, bookIds)`           | Створює автора                  |
| `updateAuthor(authorId, newName)`       | Оновлює ім’я автора             |
| `addBooksToAuthor(authorId, bookIds)`   | Прив’язує книги до автора       |
| `deleteAuthor(authorId)`                | Видаляє автора                  |
| `executeGraphQLQuery(query, variables)` | Виконує довільний GraphQL-запит |

