# Fitness Trainer Backend API - MVP

Un serviciu backend complet pentru o aplicație de antrenor fitness cu suport pentru utilizatori cu roluri diferite (ADMIN, TRAINER, TRAINEE).

## Caracteristici MVP

✅ **Autentificare și Autorizare**
- Înregistrare utilizatori cu 3 roluri: ADMIN, TRAINER, TRAINEE
- Password encryption cu BCrypt
- Validare email și username unici

✅ **Gestionarea Utilizatorilor**
- Creare, citire, actualizare, ștergere utilizatori
- Filtrare utilizatori după rol
- Profil complet: username, email, nume complet

✅ **Gestionarea Exercițiilor**
- Înregistrare exerciții zilnice
- Tracking: sets, reps, greutate, note
- Query după utilizator, dată sau interval de date

✅ **Gestionarea Meselor**
- Înregistrare mese zilnice
- Tracking: calorii, proteine, carbohidrați, grăsimi, note
- Query după utilizator, dată sau interval de date

✅ **API Standardizat**
- Răspunsuri consistente cu ApiResponse wrapper
- Validare input automată
- Error handling apropiat
- CORS enabled pentru frontend integration

## Tech Stack

- **Java 17**
- **Spring Boot 4.0.0**
- **Spring Data JPA**
- **H2 Database** (in-memory pentru MVP)
- **Lombok** (reduce boilerplate)
- **Maven**

## Endpoints API

### Health Check
```
GET /api/health
Response: ApiResponse with service status
```

### User Management
```
POST   /api/users/register              - Crează utilizator nou
GET    /api/users                       - Obține toți utilizatorii
GET    /api/users/{id}                  - Obține utilizator după ID
GET    /api/users/username/{username}   - Obține utilizator după username
GET    /api/users/role/{role}           - Obține utilizatori după rol (ADMIN/TRAINER/TRAINEE)
PUT    /api/users/{id}                  - Actualizează utilizator
DELETE /api/users/{id}                  - Șterge utilizator
```

### Exercise Management
```
POST   /api/exercises/user/{userId}              - Crează exercițiu nou
GET    /api/exercises/{id}                       - Obține exercițiu după ID
GET    /api/exercises/user/{userId}              - Obține toate exercițiile unui utilizator
GET    /api/exercises/user/{userId}/date        - Obține exerciții pentru o dată specifică
GET    /api/exercises/user/{userId}/range       - Obține exerciții într-un interval de date
PUT    /api/exercises/{id}                       - Actualizează exercițiu
DELETE /api/exercises/{id}                       - Șterge exercițiu
```

### Meal Management
```
POST   /api/meals/user/{userId}                 - Crează meniu nou
GET    /api/meals/{id}                          - Obține meniu după ID
GET    /api/meals/user/{userId}                 - Obține toate mesele unui utilizator
GET    /api/meals/user/{userId}/date            - Obține mese pentru o dată specifică
GET    /api/meals/user/{userId}/range           - Obține mese într-un interval de date
PUT    /api/meals/{id}                          - Actualizează meniu
DELETE /api/meals/{id}                          - Șterge meniu
```

## Setup și Rulare

### Prerequisite
- Java 17+
- Maven 3.6+

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

API va fi disponibil la: `http://localhost:8080`

### H2 Database Console
```
URL: http://localhost:8080/h2-console
Username: sa
Password: (lăsat gol)
```

## Structura Proiectului

```
src/main/java/com/stoltrainer/
├── StolTrainerApplication.java          - Entry point
├── config/
│   ├── SecurityConfig.java              - Password encoder, CORS
│   └── WebConfig.java                   - Web configuration
├── controller/
│   ├── HealthController.java            - Health check endpoint
│   ├── UserController.java              - User management
│   ├── ExerciseController.java          - Exercise management
│   └── MealController.java              - Meal management
├── service/
│   ├── UserService.java                 - User business logic
│   ├── ExerciseService.java             - Exercise business logic
│   └── MealService.java                 - Meal business logic
├── model/
│   ├── User.java                        - User entity
│   ├── Exercise.java                    - Exercise entity
│   ├── Meal.java                        - Meal entity
│   └── UserRole.java                    - Enum (ADMIN, TRAINER, TRAINEE)
├── repository/
│   ├── UserRepository.java              - User data access
│   ├── ExerciseRepository.java          - Exercise data access
│   └── MealRepository.java              - Meal data access
└── dto/
    ├── UserDTO.java                     - User transfer object
    ├── CreateUserRequest.java           - Registration request
    ├── ExerciseDTO.java                 - Exercise transfer object
    ├── MealDTO.java                     - Meal transfer object
    └── ApiResponse.java                 - Standard API response wrapper
```

## Exemple API Calls

Vezi `API_EXAMPLES.json` pentru exemple complete de request/response.

## Validare Input

### CreateUserRequest
- `username`: 3-50 caractere (obligatoriu)
- `password`: minim 6 caractere (obligatoriu)
- `email`: format valid email (obligatoriu)
- `firstName`, `lastName`: obligatoriu
- `role`: ADMIN, TRAINER, TRAINEE (obligatoriu)

### ExerciseDTO
- `name`: obligatoriu
- `sets`: pozitiv, obligatoriu
- `reps`: pozitiv, obligatoriu
- `weight`: pozitiv (opțional)
- `exerciseDate`: dacă nu e specificat, se va folosi ziua curentă

### MealDTO
- `name`: obligatoriu
- `calories`: pozitiv, obligatoriu
- `protein`, `carbs`, `fats`: opțional (format text)
- `mealDate`: dacă nu e specificat, se va folosi ziua curentă

## NextSteps pentru Production

- [ ] Implementare JWT authentication
- [ ] Role-based access control (RBAC)
- [ ] Database persistență (MySQL/PostgreSQL)
- [ ] API Documentation cu Swagger/OpenAPI
- [ ] Unit & Integration tests
- [ ] Logging structured (ELK stack)
- [ ] Docker containerization
- [ ] CI/CD pipeline
- [ ] Rate limiting
- [ ] Caching strategy (Redis)
