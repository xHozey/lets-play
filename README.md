# LetsPlay API

LetsPlay is a Spring Boot REST API for user authentication, user management, and product management. It uses MongoDB for persistence and JWT bearer tokens for authentication.

## Requirements

- Java 17 or newer
- Docker and Docker Compose
- Maven is not required because the project includes the Maven wrapper

## Configuration

The default application configuration uses:

| Setting | Value |
| --- | --- |
| API URL | `https://localhost:8443` |
| MongoDB database | `myapp` |
| MongoDB URI | `mongodb://admin:password@localhost:27017/?authSource=admin` |

The provided `docker-compose.yaml` starts MongoDB without authentication. Before starting the API, either change `spring.data.mongodb.uri` in `src/main/resources/application.properties` to:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/myapp
```

or configure the MongoDB container with matching `admin` and `password` credentials.

The application uses the bundled PKCS12 keystore for local HTTPS. The certificate is self-signed, so clients such as `curl` need the `-k` option during local development.

## Run locally

Start MongoDB:

```bash
docker compose up -d
```

Start the API:

```bash
mvn spring-boot:run
```

Run tests:

```bash
mvn test
```

The API is available at `https://localhost:8443`.

## Authentication

Register a user, then log in to receive a JWT:

```bash
curl -k -X POST https://localhost:8443/api/v1/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"email":"alex@example.com","name":"alex","password":"secret123"}'

curl -k -X POST https://localhost:8443/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"alex@example.com","password":"secret123"}'
```

The login response has this shape:

```json
{
  "token": "<jwt>"
}
```

Send the token with protected requests:

```bash
curl -k https://localhost:8443/api/v1/users/<user-id> \
  -H 'Authorization: Bearer <jwt>'
```

Newly registered users have the `USER` role. The API does not expose an endpoint for promoting users; an administrator must be assigned directly in MongoDB by setting the user's `role` field to `ADMIN`.

## API endpoints

All request and response bodies are JSON.

### Authentication

| Method | Endpoint | Auth | Description |
| --- | --- | --- | --- |
| `POST` | `/api/v1/auth/register` | Public | Create a user |
| `POST` | `/api/v1/auth/login` | Public | Validate credentials and return a JWT |

Registration fields:

```json
{
  "email": "alex@example.com",
  "name": "alex",
  "password": "secret123"
}
```

Email must be valid, `name` must be 3-120 characters, and `password` must be 6-260 characters. Registration returns the public user fields `id`, `name`, and `email`.

### Products

| Method | Endpoint | Auth | Description |
| --- | --- | --- | --- |
| `GET` | `/api/v1/products` | Public | List all products |
| `POST` | `/api/v1/products` | Required | Create a product owned by the current user |
| `PUT` | `/api/v1/products/{id}` | Required | Update an owned product or any product as an admin |
| `DELETE` | `/api/v1/products/{id}` | Required | Delete an owned product or any product as an admin |

Create or update a product with:

```json
{
  "name": "Starter kit",
  "description": "A useful starter product with everything needed to begin.",
  "price": 19.99
}
```

`name` must be 3-600 characters, `description` must be 50-4000 characters, and `price` must be positive. Product responses contain `id`, `name`, `desc`, and `price`.

Example:

```bash
curl -k -X POST https://localhost:8443/api/v1/products \
  -H 'Authorization: Bearer <jwt>' \
  -H 'Content-Type: application/json' \
  -d '{"name":"Starter kit","description":"A useful starter product with everything needed to begin.","price":19.99}'
```

### Users

| Method | Endpoint | Auth | Description |
| --- | --- | --- | --- |
| `GET` | `/api/v1/users` | Admin | List all users |
| `GET` | `/api/v1/users/{id}` | Self or admin | Get one user |
| `PUT` | `/api/v1/users/{id}` | Self or admin | Update a user's name and email |
| `DELETE` | `/api/v1/users/{id}` | Self or admin | Delete a user |

Update fields:

```json
{
  "email": "new-alex@example.com",
  "name": "alex-new"
}
```

User responses contain only `id`, `name`, and `email`; passwords are never returned.

## Common responses

- `400 Bad Request`: invalid request data
- `401 Unauthorized`: missing, invalid, or expired JWT
- `403 Forbidden`: authenticated user cannot modify the requested resource
- `404 Not Found`: resource or route does not exist
- `409 Conflict`: email or name is already in use
- `500 Internal Server Error`: unexpected server error

Error responses generally contain an `error` or `message` field.

## Project structure

```text
src/main/java/com/zone01/letsplay/
  controller/   REST endpoints
  dto/          Request and response records
  model/        MongoDB documents
  repository/   Spring Data repositories
  security/     JWT filter and Spring Security configuration
  service/      Business logic
```