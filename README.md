# ESPE Club Match — README

Resumen rápido

Proyecto backend en Java (Spring Boot) para emparejar estudiantes con clubs (ESPE Clubs Match). Incluye autenticación JWT, rate limiting, integración con un servicio externo de recomendaciones, envío de correos (SMTP) y soporte para LLM (Google Gemini vía Spring AI).

Estado: artefacto empaquetado en `target/sample-spring-boot-0.0.1-SNAPSHOT.jar`.

---

Índice

- Descripción
- Tecnologías principales
- Requisitos
- Instalación y build (PowerShell)
- Configuración (variables de entorno / .env)
- Ejecutar (local / Docker)
- Ejecutar tests
- Endpoints principales (resumen)
- Autenticación (JWT / cookies)
- Integraciones externas
- Arquitectura y estructura de carpetas
- Notas de seguridad y operaciones
- Problemas conocidos y recomendaciones
- Contribuir
- Licencia


Descripción

API REST para gestionar usuarios (pre-registro, activación, recuperación), encuestas de estudiantes, generación de recomendaciones y listado/gestión de clubs. Proporciona:

- Endpoints públicos para autenticación y algunos recursos.
- Endpoints internos protegidos por JWT.
- Rate limiting por IP con bucket4j y whitelist configurable desde la base de datos.
- Integración con un servicio externo de recomendaciones y con Google GenAI para chat/LLM.
- Envío de correos mediante SMTP y plantillas Thymeleaf.
- Documentación OpenAPI / Swagger habilitada.


Tecnologías principales

- Java 17
- Spring Boot 3.x
- Spring Data JPA (PostgreSQL)
- Spring Security (JWT)
- bucket4j (rate limiting)
- springdoc-openapi (Swagger)
- java-dotenv (carga de .env)
- spring-ai (Google GenAI)
- Maven (wrapper incluido: `mvnw.cmd`)


Requisitos

- JDK 17
- Maven (opcional, el proyecto trae `mvnw`/`mvnw.cmd`)
- PostgreSQL (o configurar `DB_URL` hacia su base de datos)
- (Opcional) Docker


Instalación y build (PowerShell)

- Construir y empaquetar (usa el wrapper incluido):

```powershell
.\mvnw.cmd clean package
```

- Ejecutar pruebas:

```powershell
.\mvnw.cmd test
```


Configuración (variables de entorno / .env)

El proyecto usa `java-dotenv` para cargar variables desde un `.env` en el directorio del proyecto y además `EspeClubMatchApplication` copia algunas variables importantes a System properties.

Variables esperadas (mínimo para arrancar):

- DB_URL — URL JDBC de Postgres (ej: jdbc:postgresql://localhost:5432/clubmatch)
- DB_USERNAME
- DB_PASSWORD
- SECRET_KEY_JWT — clave secreta JWT (BASE64URL)
- EXPIRATION_TIME_JWT — tiempo de expiración en segundos

Variables relacionadas con correo (SMTP):
- ALERT_SMTP_HOST
- ALERT_SMTP_FROM
- ALERT_SMTP_USER
- ALERT_SMTP_PASSWORD
- ALERT_SMTP_TO
- ALERT_SMTP_PORT

Integraciones / IA:
- API_GEMINI_KEY — API key para Google GenAI (se inyecta en `spring.ai.google.genai.api-key`)

Parámetros externos (propiedades con valores por defecto en código o properties):
- `external.recommendations.base-url` — URL del servicio de recomendaciones externo (por defecto configurado en `RecommendationClient`)
- `external.recommendations.api-key` — API key para llamadas internas al servicio de recomendaciones

Ejemplo mínimo de `.env` (NO usar valores reales en repositorios):

```
DB_URL=jdbc:postgresql://localhost:5432/clubmatch
DB_USERNAME=clubmatch_user
DB_PASSWORD=changeme
SECRET_KEY_JWT=<TOKEN_BASE64URL_32_BYTES>
EXPIRATION_TIME_JWT=3600
ALERT_SMTP_HOST=smtp.example.com
ALERT_SMTP_FROM=no-reply@example.com
ALERT_SMTP_USER=smtp-user
ALERT_SMTP_PASSWORD=smtp-pass
ALERT_SMTP_TO=admin@example.com
ALERT_SMTP_PORT=587
API_GEMINI_KEY=your_gemini_api_key_here
```

Generar una clave JWT (PowerShell — 32 bytes en base64url):

```powershell
$r = New-Object Byte[] 32; (New-Object Random).NextBytes($r); $b = [Convert]::ToBase64String($r).TrimEnd('=') -replace '\+','-' -replace '/','_'; Write-Output $b
```

Nota: `JwtProvider` decodifica `jwt.secret` como BASE64URL; asegúrate de proveerla en ese formato.


Perfiles y properties

- `application.properties` activa por defecto el perfil `dev`.
- `src/main/resources/application-dev.properties` contiene placeholders para DB, JWT, SpringDoc, cookies y la propiedad `spring.ai.google.genai.api-key`.


Ejecutar (local / JAR)

- Ejecutar con `mvn spring-boot:run` (perfil dev):

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```

- Ejecutar JAR empaquetado:

```powershell
java -jar .\target\sample-spring-boot-0.0.1-SNAPSHOT.jar
```


Docker (build y run)

- Construir imagen (desde la raíz del proyecto):

```powershell
docker build -t clubmatch .
```

- Ejecutar contenedor (mapea puerto 8080 por defecto):

```powershell
docker run -p 8080:8080 clubmatch
```

Observación: el `Dockerfile` usa una imagen `maven:3.9.6-eclipse-temurin-17` para construir y `openjdk:26-ea-17-jdk-slim` para runtime.


Endpoints principales (resumen)

Base: `/api/v1`

Rutas públicas (no requieren token):

- POST /api/v1/auth/login
  - Body: { "username": "...", "password":"..." }
  - Respuesta: JsonDtoResponse<Boolean>. Si login es exitoso, la API setea una cookie `access_token` con el JWT (httpOnly).

- GET /api/v1/auth/validate
  - Valida el token enviado en cookie o header `Authorization: Bearer <token>`.

- POST /api/v1/auth/logout
  - Borra la cookie `access_token`.

- POST /api/v1/auth/pre-register
  - Pre-registro de usuario (envía PIN por correo).

- POST /api/v1/auth/activation
  - Activar usuario con PIN.

- POST /api/v1/auth/recovery-password
  - Inicia recuperación de contraseña (envía PIN).

- POST /api/v1/auth/validate-pin
  - Valida PIN de recuperación.

- POST /api/v1/auth/update-password
  - Actualiza contraseña con PIN.

- GET /api/v1/gemini/chat?prompt=...  (incluido en whitelist, público)
  - Devuelve respuesta de LLM (usa `spring-ai` ChatModel bean).

Rutas protegidas (requieren JWT):

- GET /api/v1/clubs
  - Lista todos los clubs (AdminClubService). Ej: devolución `ClubAdminDto`.

- GET /api/v1/clubs/{id}
  - Obtener club por id.

- POST /api/v1/students/survey
  - Crear survey para el estudiante autenticado. Requiere token. Body: `CreateSurveyRequestDto`.

- POST /api/v1/students/survey/recommendation
  - Genera recomendaciones externas para el estudiante autenticado. Internamente llama al `RecommendationClient` y retorna `RecommendationListDto`.

Observaciones adicionales:
- Swagger/UI: `http://localhost:8080/swagger-ui.html` (habilitado en dev).
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`.

Ejemplo de login con curl (obtén la cookie):

```bash
curl -i -X POST http://localhost:8080/api/v1/auth/login -H "Content-Type: application/json" -d '{"username":"alumno","password":"password123"}'
```

Ejemplo usando Authorization header:

```bash
curl -H "Authorization: Bearer <TOKEN>" http://localhost:8080/api/v1/clubs
```


Autenticación y cookies

- La cookie se llama `access_token` (definido en `CookieUtils`).
- Si el cliente no usa cookies, puede enviar `Authorization: Bearer <token>`.
- `SecurityConfig` declara las rutas en `AUTH_WHITELIST` que están exentas de autenticación (Swagger, `/api/v1/auth/**`, `/api/v1/gemini/**`, etc.).


Integraciones externas

- Servicio de recomendaciones: `RecommendationClient` llama a `{external.recommendations.base-url}/api/recommendations/{studentId}` usando cabecera `x-internal-ia: <api-key>`.
  - Propiedades configurables:
    - external.recommendations.base-url
    - external.recommendations.api-key

- Google GenAI / Gemini: el proyecto usa `spring-ai` con la propiedad `spring.ai.google.genai.api-key` configurada desde `API_GEMINI_KEY` en el `.env`.

- SMTP: envío de correos usando `spring-boot-starter-mail` y la configuración en `MailConfig` (plantillas Thymeleaf en `resources/templates`).


Rate limiting

- `RateLimitAndJwtFilter` aplica limitación por IP con `bucket4j`. Límite por defecto en código: capacidad 10, refill greedy 50 cada minuto (configurada en `createNewBucket`).
- IPs confiables (whitelist) se obtienen desde la base de datos (`SystemParameters` con `CatalogEnums.WHITE_LISTED_IP`).


Arquitectura y estructura de carpetas (resumen)

- `src/main/java/com/especlub/match/controller` — Controladores REST (subcarpetas `publics`, `internal`, `admin`).
- `src/main/java/com/especlub/match/services` — Interfaces y `impl` con la lógica de negocio.
- `src/main/java/com/especlub/match/repositories` — Repositorios JPA.
- `src/main/java/com/especlub/match/models` — Entidades JPA.
- `src/main/java/com/especlub/match/dto` — DTOs de request/response.
- `src/main/java/com/especlub/match/security` — SecurityConfig, JWT provider, filtros.
- `src/main/java/com/especlub/match/config` — MailConfig, DotenvConfig, OpenApiConfig, etc.
- `src/main/resources` — `application-*.properties`, templates, mensajes.


Notas de seguridad y operaciones

- Mantén `SECRET_KEY_JWT` fuera del control de versiones. Usar gestores de secretos / variables de entorno en despliegue.
- `jwt.secret` debe ser BASE64URL y tener la longitud adecuada (ej. 256 bits) para HMAC-SHA.
- Revisa los valores de cookies (`cookie.sameSite`, `cookie.secure`) para producción (por ejemplo SameSite=None + Secure=true para dominios cross-site).
- Ajusta `spring.datasource.hikari.*` si la carga aumenta.
- Si activas `springdoc` en producción, restringe su acceso o protéjelo con autenticación.


Problemas conocidos y recomendaciones

- `Dockerfile` usa `openjdk:26-ea-17-jdk-slim` como runtime (imagen rara/EA); revisar y cambiar a una imagen estable si es necesario (ej: `eclipse-temurin:17-jdk-jammy` o `eclipse-temurin:17-jre`).
- `JwtProvider#getSecret` decodifica `jwt.secret` como BASE64URL; claves mal formadas provocarán excepciones al arrancar/validar tokens.


Comandos útiles de diagnóstico (PowerShell)

- Listar endpoints (búsqueda rápida en código):

```powershell
Get-ChildItem -Recurse -Filter *.java src\main\java | Select-String -Pattern '@RequestMapping|@GetMapping|@PostMapping|@PutMapping|@DeleteMapping' | Sort-Object Path
```

- Ejecutar la app y ver logs:

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
```


Contribuir

1. Crear una rama por funcionalidad: `feature/<nombre>`.
2. Ejecutar tests localmente: `.\mvnw.cmd test`.
3. Hacer PR con descripción y pasos de verificación.


Licencia

- Ninguna licencia explícita en el repositorio; añade un `LICENSE` si quieres publicarlo. Hasta entonces, asume uso privado.


Contacto y ayuda

- Consulta `HELP.md` para documentación de referencia.
- Swagger/OpenAPI (`/swagger-ui.html`) contiene ejemplos y descripciones detalladas extraídas de las interfaces `*ControllerDoc`.


---

Si quieres, genero también:
- Un archivo `.env.example` con placeholders listo para usar.
- Un listado más detallado de todos los endpoints (extraeré todos los `@*Mapping` y crearé una tabla con verbos, rutas y controladores).

Dime cuál de esas dos tareas (crear `.env.example` o listar todos los endpoints) hago a continuación y lo genero.
