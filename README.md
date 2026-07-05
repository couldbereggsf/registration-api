# Registration API
A multi-stage user registration API demonstrating clean architectural layering, tiered validation, and global exception handling. This project serves as a foundational example of structured Spring Boot development, featuring custom validation logic, database integration, and a decoupled module design.

# Module layout
```
registration-api/            (parent pom)
├── common/                 framework-light: APIResponse, FirstCheck/SecondCheck
│                           group markers, @PasswordsMatch, @MinimumAge,
│                           custom exceptions
├── persistence/            JPA: User entity, UserRepository
├── service/                RegistrationService — the Default-group,
│                           DB-hitting uniqueness check + save
└── web/                    the runnable Spring Boot app: RegistrationRequest
                            DTO (with @GroupSequence), RegistrationController,
                            GlobalExceptionHandler
```
Dependency direction: **web → service → persistence → common.** The common module maintains a clean separation of concerns with zero Spring Web dependencies.

# The validation pipeline (RegistrationRequest)
```
Stage 1 (FirstCheck)   →  not-blank / not-null / email format
Stage 2 (SecondCheck)  →  passwords match (@PasswordsMatch), age >= 18 (@MinimumAge)
Stage 3 (Default)      →  username/email uniqueness — lives in the SERVICE layer,
                          not as a Jakarta constraint, because it needs a DB call
```

If Stage 1 fails, subsequent stages are skipped. If Stages 1 and 2 pass, the request reaches ***`RegistrationService`***, which performs a database-backed uniqueness check. If this fails, the service throws a domain exception, which is caught
by `GlobalExceptionHandler` and returned to the client as a `409 Conflict`.

# Running locally
To run the application:

```Bash
cd registration-api
mvn clean install        # builds all 4 modules in dependency order
cd web
mvn spring-boot:run
```
The app starts on `http://localhost:8080` with an in-memory H2 database.

H2 Console: `http://localhost:8080/h2-console`

JDBC URL: `jdbc:h2:mem:registration`

### API Examples
```Bash
# Stage 1 failure (blank fields) → 400 Bad Request
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{"username":"","email":"","password":"","confirmPassword":"","age":null}'

# Stage 2 failure (passwords don't match) → 400 Bad Request
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{"username":"reagan","email":"reagan@example.com","password":"S3cret!","confirmPassword":"Different!","age":25}'

# Success → 201 Created + Location header
curl -i -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{"username":"reagan","email":"reagan@example.com","password":"S3cret!","confirmPassword":"S3cret!","age":25}'
```
## Testing
Run all tests via:
```Bash
mvn test
```
* `persistence`: `UserRepositoryTest` utilizes real H2-backed `@DataJpaTest` to verify entity persistence.

* `service`: `RegistrationServiceTest` uses Mockito to isolate business logic, covering both the happy path and uniqueness constraints.

* `web`: `RegistrationControllerTest` performs full MockMvc testing, exercising the `@GroupSequence` validation pipeline.

## Roadmap & Improvements
* **Security**: Current password hashing is a placeholder (`PLAINTEXT_PLACEHOLDER:`). This will be replaced with Spring Security's `PasswordEncoder` (BCrypt).

* **Error Handling:** Add comprehensive tests for `@ControllerAdvice` to ensure the 500 fallback logic does not leak sensitive stack trace details.

* **Validation Refinement:** The `RegistrationRequest.OrderedValidation` sequence currently utilizes two groups before defaulting to the `Default` group. Future constraints added directly to the class should be evaluated to ensure they trigger at the expected stage in the validation sequence.

