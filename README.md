# **AI Code Reviewer**

A simple **Spring Boot** application that uses the **Google Gemini API** to review Java code and suggest improvements.

## **Features**

- Review Java code through a **REST API**
- Get AI-generated code review feedback
- Responses include **summary, issues, severity, suggestions, and improved code**
- **Request validation**
- **Swagger API documentation**
- API key managed using **environment variables**
- **Spotless** code formatting
- **GitHub Actions** build

## **Tech Stack**

- **Java 17**
- **Spring Boot**
- **Spring Web**
- **Google Gemini API**
- **RestClient**
- **Jackson**
- **Lombok**
- **Swagger / OpenAPI**
- **Gradle**
- **Spotless**

## **API**

### **POST `/api/v1/code-review`**

**Request:**

```json
{
  "code": "public class UserService { ... }"
}
```

**Response:**

```json
{
  "summary": "The code has a potential performance issue.",
  "issues": [
    {
      "severity": "HIGH",
      "issue": "A database query is executed inside a loop.",
      "suggestion": "Fetch the required data using a single query."
    }
  ],
  "improvedCode": "..."
}
```

## **Configuration**

The **Gemini API key is not stored in the repository**.

In `application.properties`:

```properties
gemini.api.url=${GEMINI_API_URL}
gemini.api.key=${GEMINI_API_KEY}
```

Set the values as **environment variables** before running the application.

## **Testing**

```bash
./gradlew test
```

## **Run the Application**

```bash
./gradlew bootRun
```

Application:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```
