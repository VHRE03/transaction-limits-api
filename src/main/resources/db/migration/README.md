# Guía de Flyway — `src/main/resources/db/migration`

Esta carpeta contiene las **migraciones de base de datos** del proyecto. Todo
cambio de esquema (DDL) y todo cambio de datos (DML) que acompañe a una nueva
funcionalidad **debe vivir aquí**, nunca ejecutarse a mano contra la base de
datos.

La carpeta nace **vacía** (sin scripts): `V1` queda reservado para la primera
migración real del negocio.

---

## 1. ¿Cómo está integrada en este proyecto?

La integración es automática mediante Spring Boot:

| Pieza                                                    | Ubicación                                  |
|----------------------------------------------------------|--------------------------------------------|
| Dependencias (`spring-boot-starter-flyway`, `flyway-database-postgresql`) | `pom.xml`                  |
| Habilitación y carpeta de migraciones                     | `application.yml` (`spring.flyway.*`)      |
| Archivos de migración                                    | esta carpeta (`classpath:db/migration`)    |
| Historial en la base de datos                             | tabla `flyway_schema_history` (la crea Flyway solo) |

Al arrancar la aplicación, **antes** de que JPA/Hibernate inicialice, Spring
Boot ejecuta Flyway automáticamente: aplica los cambios pendientes **en orden
y exactamente una vez**. No hay ningún comando extra que correr.

Como Flyway es el dueño del esquema, `spring.jpa.hibernate.ddl-auto` está en
`validate` (local/dev) y `none` (prod): Hibernate **nunca** crea ni modifica
tablas; solo comprueba que las entidades coincidan con el esquema migrado.

## 2. Convención de nombres (obligatoria)

```
V<versión>__<descripción_en_snake_case>.sql
  │    │      │
  │    │      └─ doble guion bajo (OBLIGATORIO, no uno solo)
  │    └─ entero creciente: 1, 2, 3, ... 10, 11 ...
  └─ V = migración versionada (se ejecuta una sola vez, en orden)
```

Ejemplo: `V1__create_transaction_limits_table.sql`

Reglas:

- La versión es un **entero secuencial** sin saltos ni subversiones (`V3`, no
  `V2.5` ni `V3_beta`).
- La **descripción** es corta, en `snake_case` y en inglés
  (`create_transaction_limits`, `add_channel_to_limits`).
- Existen otros prefijos que **no usamos por ahora**: `R__` (repeatable) y
  `U__` (undo, desaconsejado). Con `V__` es suficiente.

## 3. Las reglas de oro

> 1. **Nunca edites una migración que ya se aplicó** en alguna base de datos
>    (ni siquiera un comentario): el checksum cambia y la aplicación no
>    arranca. ¿Corregir algo? Escribe una **migración nueva**.
> 2. **Solo agrega archivos nuevos.** La historia es *append-only*, igual que Git.
> 3. **Migraciones hacia adelante** (*forward-only*): no escribimos `DROP` para
>    revertir; creamos una migración que compense el cambio.
> 4. **Un archivo nuevo = siguiente versión global.** Si dos ramas crean la
>    misma versión, quien haga merge segundo renumera la suya.
> 5. **La migración es la única forma de cambiar la base de datos.** Nada de
>    `ALTER` manuales "rápidos".
> 6. **Prueba cada migración en local** contra una base de datos limpia
>    (`DROP DATABASE` + `CREATE DATABASE` + arrancar la app).

## 4. Cómo añadir una migración (paso a paso)

1. Mira el archivo con el número `V` más alto de esta carpeta (o consulta
   `flyway_schema_history`) para saber el último número aplicado.
2. Crea el archivo con el siguiente número: `V<siguiente>__<descripción>.sql`.
3. Escríbelo en PostgreSQL (puedes usar `UUID`, `JSONB`, `TIMESTAMPTZ`,
   `GEN_RANDOM_UUID()`...). Los montos e importes son `NUMERIC` — nunca
   `FLOAT`/`DOUBLE` para dinero.
4. Incluye siempre las columnas de auditoría estándar (sección 5).
5. Arranca la aplicación y verifica en el log:
   ```
   Migrating schema "public" to version "1 - tu descripcion"
   Successfully applied 1 migration to schema "public"
   ```
6. Verifica con tu cliente SQL y **comitea el archivo junto con el código
   Java** que lo requiere (misma rama, mismo PR).

Estado real de la base de datos:

```sql
SELECT installed_rank, version, description, success, installed_on
FROM flyway_schema_history
ORDER BY installed_rank;
```

## 5. Columnas obligatorias: la relación con `BaseEntity`

**Todas** las entidades del proyecto extienden `BaseEntity` (del starter
interno `com.vhre:base-project-spring-boot-starter`), que aporta `id`,
`createdAt`, `updatedAt` y `deleted` (mapeada a la columna `is_deleted`).
Por eso **toda tabla nueva** debe incluir exactamente estas columnas:

| Columna      | Tipo PostgreSQL | Restricción              | Origen                                 |
|--------------|-----------------|--------------------------|----------------------------------------|
| `id`         | `UUID`          | `NOT NULL`, `PRIMARY KEY` | `BaseEntity` (Hibernate lo genera)     |
| `created_at` | `TIMESTAMP`     | `NOT NULL`                | Spring Data auditing (`@CreatedDate`)  |
| `updated_at` | `TIMESTAMP`     | `NULL`                    | Spring Data auditing (`@LastModifiedDate`) |
| `is_deleted` | `BOOLEAN`       | `NOT NULL DEFAULT FALSE`  | Soft-delete de `BaseEntity`             |

Plantilla mínima para una tabla nueva:

```sql
CREATE TABLE transaction_limits
(
    id          UUID         NOT NULL,
    -- ... columnas de negocio ...
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NULL,
    is_deleted  BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_transaction_limits PRIMARY KEY (id)
);
```

- Los **nombres de tabla** son **plural, `snake_case` y minúsculas**
  (`transaction_limits`, nunca `TransactionLimit`).
- `BaseEntity` aplica un filtro global `is_deleted = false`: las filas con
  `is_deleted = true` son invisibles para toda la aplicación (borrado lógico).

## 6. Convenciones de nombres en la base de datos

| Objeto             | Prefijo | Ejemplo                                |
|-------------------|---------|----------------------------------------|
| Tabla             | —       | `transaction_limits`                   |
| Primary key       | `pk_`   | `pk_transaction_limits`                |
| Foreign key       | `fk_`   | `fk_limits_movements_limit_id`         |
| Índice            | `ix_`   | `ix_transaction_limits_is_deleted`     |
| Unique constraint | `uq_`   | `uq_transaction_limits_reference`      |
| Check constraint  | `ck_`   | `ck_transaction_limits_amount_positive`|

## 7. Errores frecuentes y cómo resolverlos

| Error en el arranque                                  | Causa                                        | Solución                                              |
|--------------------------------------------------------|----------------------------------------------|--------------------------------------------------------|
| `Migration checksum mismatch for migration version 1`  | Se editó un archivo ya aplicado              | Restaurar el original; si el cambio es necesario: migración nueva |
| `Detected applied migration not resolved locally`      | La BD tiene versiones que ya no existen      | NO borrar archivos: reponerlos desde Git                |
| `Found more than one migration with version 3`         | Dos ramas crearon `V3`                      | Renumerar la de la rama que hizo merge segundo         |
| La app arranca pero la tabla no aparece                | Archivo fuera de `db/migration` o mal nombrado | Revisar: `V3__descripcion.sql` con **doble** guion bajo |
| `FlywayException: Unable to connect to database`       | Credenciales incorrectas en el perfil       | Verificar host/puerto/usuario/contraseña                |

**Nunca uses `flyway repair` ni borres filas de `flyway_schema_history` sin
entender lo que hacen**: enmascaran desincronizaciones en lugar de resolverlas.

---

*Para la arquitectura general del proyecto, revisa el `README.md` raíz.*
