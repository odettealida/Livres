## Overview
My project is an online library. It is used by students.
A borrowed book is unavailable until it is returned.

## Tech Stack
- Java 17
- Spring Boot 4
- Spring Data JPA
- H2 database
- Swagger (springdoc) for API documentation
- JUnit and Mockito for testing

## Features
- Full CRUD on books
- Borrow and return a book, with business rules (an already borrowed book is refused)
- Search by title and author, with pagination and sorting
- Input validation with clear error messages

## Getting Started
1. Run the application (Run As > Spring Boot App).
2. Open Swagger UI: http://localhost:8080/swagger-ui/index.html

## API Endpoints
| Method | URL | Description |
|---|---|---|
| GET | /livres | List books (params: titre, auteur, page, size, sort) |
| GET | /livres/{id} | Get one book |
| POST | /livres | Create a book |
| PUT | /livres/{id} | Update a book |
| DELETE | /livres/{id} | Delete a book |
| POST | /livres/{id}/emprunter | Borrow a book |
| POST | /livres/{id}/rendre | Return a book |

## Error Handling
| Status | Meaning |
|---|---|
| 400 | Invalid data (one message per field) |
| 404 | Book not found |
| 409 | Book unavailable, or not borrowed |

## Tests
Run the tests with `./mvnw test`, or with JUnit in Eclipse.

## Future Improvements
DTO , borrowing history, Docker, PostgreSQL, Spring Security.
