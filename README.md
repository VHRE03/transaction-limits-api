# transaction-limits-api

API REST **monolítica** para la **gestión de límites de transacciones**.
Proyecto base: solo infraestructura (base de datos, caché distribuida,
seguridad, documentación y migraciones). El dominio se agregará después como
módulos funcionales.

## Stack tecnológico

| Componente            | Tecnología                                            |
|-----------------------|--------------------------------------------------------|
| Lenguaje              | Java 21                                                |
| Framework             | Spring Boot **4.1.1** (última versión estable en Maven Central) |
| Arquitectura          | Monolito (Spring MVC, **sin Kafka**, sin mensajería)  |
| Base de datos         | PostgreSQL                                             |
| Migraciones           | **Flyway** (ver `src/main/resources/db/migration/README.md`) |
| Gestión del esquema   | Flyway es el **único** dueño del esquema: `ddl-auto=validate` (local/dev) y `none` (prod) |
| Persistencia          | Spring Data JPA + Hibernate                            |
| Caché distribuida     | **Redis** (Spring Cache, beans en `RedisCacheConfig`)  |
| Seguridad             | Spring Security (stateless) + **JJWT** (listo para el módulo de autenticación) |
| Documentación API     | springdoc-openapi 3.x (Swagger UI)                     |
| Clases base internas  | `com.vhre:base-project-spring-boot-starter` (GitHub Packages) |
| Mappers               | MapStruct                                              |
| Boilerplate           | Lombok                                                 |
| Testing               | JUnit 5 + Mockito + AssertJ + MockMvc + **Testcontainers** (PostgreSQL y Redis) |
| Build                 | **Maven** (`./mvnw`)                                   |
| Configuración         | YAML: `application.yml` + perfiles `local`/`dev`/`prod` |

## Estructura del proyecto

```
transaction-limits-api/
├── pom.xml
├── mvnw / mvnw.cmd                    # Maven wrapper (no requiere Maven instalado)
├── Dockerfile                         # Multi-stage (Maven+JDK 21 → JRE 21)
├── docker-compose.yml                 # Stack local: PostgreSQL + Redis + API
├── .env.example                       # Credenciales del compose (copiar a .env)
└── src
    ├── main
    │   ├── java/com/vhre/transactionlimitsengine
    │   │   ├── TransactionLimitsApiApplication.java   # Punto de entrada
    │   │   └── config/
    │   │       ├── OpenApiConfig.java        # Swagger / OpenAPI 3
    │   │       ├── RedisCacheConfig.java     # Spring Cache sobre Redis
    │   │       └── SecurityConfig.java       # Base de seguridad (stateless)
    │   └── resources
    │       ├── application.yml               # Perfil por defecto + Flyway + Swagger
    │       ├── application-local.yml         # BD/Redis local (NO se comitea: en .gitignore)
    │       ├── application-dev.yml           # Dev (vía variables de entorno)
    │       ├── application-prod.yml          # Prod (vía variables de entorno)
    │       └── db/migration/                 # MIGRACIONES FLYWAY (nace vacía)
    │           └── README.md                 # Guía completa de Flyway
    └── test
        └── java/com/vhre/transactionlimitsengine/   # (sin tests aún)
```

## Prerrequisitos

1. **JDK 21 o superior** (`java -version`). El proyecto compila con `release 21`
   pero el build puede correr desde cualquier JDK moderno.
   - **Importante (Lombok + JDK)**: cada JDK nuevo requiere una versión mínima
     de Lombok. Este proyecto usa **Lombok 1.18.48** (soporta hasta JDK 27). Si
     al compilar ves `ExceptionInInitializerError: EndPosTable`, el JDK del
     build es más nuevo de lo que soporta el Lombok del proyecto: actualiza
     `lombok.version` en el `pom.xml`.
2. **Docker** (para el stack local y para Testcontainers).
3. **Token de GitHub Packages** para el starter interno (una sola vez por
   máquina): en `~/.m2/settings.xml` debe existir el servidor `github` con un
   PAT (`ghp_...`) con permiso `read:packages`:

   ```xml
   <settings xmlns="http://maven.apache.org/SETTINGS/1.2.0"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.2.0 https://maven.apache.org/xsd/settings-1.2.0.xsd">
       <servers>
           <server>
               <id>github</id>
               <username>VHRE03</username>
               <password>ghp_tu_token_aqui</password>
           </server>
       </servers>
   </settings>
   ```

   El starter aporta: `BaseEntity`, `BaseDTO`, `BaseMapper`, `BaseService`,
   `BaseServiceImpl`, `BaseController` y el `GlobalExceptionHandler`.

## Levantar la infraestructura local (PostgreSQL + Redis)

```bash
cp .env.example .env              # docker compose lee .env automáticamente
docker compose up -d postgres-limits redis-limits
```

Puertos host elegidos para no chocar con los otros stacks del workspace:

| Servicio   | Puerto host | Motivo                                                    |
|------------|-------------|-----------------------------------------------------------|
| PostgreSQL | **5436**    | 5432 nativo, 5434 loan-simulator, 5435 fee-calculator     |
| Redis      | **6380**    | 6379 suele estar tomado por un Redis nativo               |
| API        | 8080        | estándar (solo un stack a la vez)                          |

## Ejecutar

```bash
./mvnw spring-boot:run
```

Al arrancar, Flyway valida/aplica las migraciones pendientes y después verás
el Tomcat en `http://localhost:8080`.

- Swagger UI: <http://localhost:8080/swagger-ui.html>
- OpenAPI JSON: <http://localhost:8080/v3/api-docs>

**Nota de seguridad**: la configuración base es *stateless* y exige
autenticación para todos los endpoints (excepto la documentación de Swagger),
hasta que se implemente el filtro JWT (JJWT). Mientras tanto, cualquier
endpoint de negocio será rechazado (`401`/`403`). Las llaves del token están
reservadas en `app.security.jwt.*` (`secret`, `expiration-ms`).

### Perfiles

| Perfil  | Activación               | Uso                                                        |
|---------|--------------------------|-------------------------------------------------------------|
| `local` | Por defecto (`APP_PROFILE` vacío) | Tu máquina: lee `application-local.yml` (gitignored) |
| `dev`   | `APP_PROFILE=dev`        | Vía variables de entorno (`DB_*`, `REDIS_*`); lo usa docker-compose |
| `prod`  | `APP_PROFILE=prod`      | Producción (variables obligatorias sin defaults, Swagger desactivado) |

```bash
APP_PROFILE=dev DB_HOST=... DB_NAME=... DB_USER=... DB_PASSWORD=... \
REDIS_HOST=... ./mvnw spring-boot:run
```

## Migraciones (Flyway)

Toda la guía de uso, convenciones y reglas está en
[`src/main/resources/db/migration/README.md`](src/main/resources/db/migration/README.md).

Resumen operativo:

- Las migraciones viven en `src/main/resources/db/migration` y se aplican
  **automáticamente al arrancar**, en orden y una sola vez.
- La carpeta nace **vacía**: `V1` queda reservado para la primera migración
  real del negocio. Para un cambio nuevo crea `V<siguiente>__<descripcion>.sql`
  (doble guion bajo).
- **Nunca edites ni borres una migración ya aplicada** en algún entorno: el
  checksum cambiaría y la aplicación no arrancaría.

## Testing

```bash
./mvnw test
```

- **JUnit 5 + Mockito + AssertJ + MockMvc** llegan vía los starters de test
  (`spring-boot-starter-webmvc-test` y `spring-boot-starter-data-jpa-test`).
- **Testcontainers** está listo para los tests de integración: levanta
  PostgreSQL y Redis reales (requiere Docker corriendo). Versiones gestionadas
  por el BOM de Spring Boot.
- Por ahora el proyecto base no incluye tests: se agregan junto con cada
  módulo funcional.

## Empaquetado y Docker

### 1. Construir el JAR

```bash
./mvnw clean package
```

Produce **`target/transaction-limits-api.jar`** (nombre estable definido con
`finalName` en el `pom.xml`).

### 2. Construir la imagen (multi-stage, Java 21)

El `Dockerfile` compila **dentro** de la imagen (stage Maven + JDK 21) y copia
el JAR a un stage final con solo el JRE 21. Como el starter interno vive en
GitHub Packages, el build recibe `~/.m2/settings.xml` como **secreto de
BuildKit**: la credencial nunca queda almacenada en la imagen ni en sus capas.

```bash
docker build --secret id=maven_settings,src=$HOME/.m2/settings.xml \
             -t transaction-limits-api:latest .
```

### 3. Levantar el stack completo (app + PostgreSQL + Redis)

```bash
cp .env.example .env        # solo la primera vez
docker compose up -d --build
```

- `postgres-limits` (PostgreSQL 15, healthcheck `pg_isready`) y `redis-limits`
  (Redis 7, AOF activado, healthcheck `redis-cli ping`).
- `transaction-limits-api` (perfil **dev**, Swagger activo) espera a que la BD
  y Redis estén healthy antes de arrancar.
- Swagger UI del contenedor: <http://localhost:8080/swagger-ui.html>
- La imagen arranca por defecto con perfil **prod** (config 100% por variables
  de entorno); el compose lo sobreescribe a dev para poder probar desde el
  host.
- Detener solo la API (libera el 8080 para `./mvnw spring-boot:run`):
  `docker compose stop transaction-limits-api`

## Caché Redis

- Conexión: `spring.data.redis.*` en cada perfil; beans de serialización y TTL
  en `config/RedisCacheConfig.java` (TTL por defecto: 30 minutos, valores en
  JSON con tipo para poder deserializarlos a su clase original).
- Uso: anota los servicios con `@Cacheable` / `@CachePut` / `@CacheEvict` a
  medida que se agreguen módulos funcionales.
