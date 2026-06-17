# Tarea 1 - CRUD back-end (Proyecto 3)

API REST en Spring Boot con autenticación JWT, roles (`SUPER_ADMIN_ROLE`, `USER`) y CRUD completo para `Category` y `Product`.

## Requisitos previos

- JDK 21
- Docker Desktop (para levantar PostgreSQL en un contenedor local)
- IntelliJ IDEA

No es necesario instalar PostgreSQL en la máquina: se ejecuta dentro de un contenedor Docker definido en `docker-compose.yml`.

## Cómo ejecutar el proyecto

### 1. Levantar la base de datos

Desde la raíz del proyecto, con Docker Desktop abierto:

```bash
docker compose up -d
```

Esto crea un contenedor PostgreSQL (`tarea1-postgres`) en `localhost:5433`, con la base `tarea1_db`, usuario `tarea1_user` y password `tarea1_pass` (ya configurados en `src/main/resources/application.properties`).

> **Nota:** se usa el puerto **5433** (no el 5432 por defecto) porque es común tener ya un PostgreSQL nativo de Windows u otro servicio ocupando el 5432. Si en tu máquina el 5432 está libre y prefieres usarlo, puedes cambiarlo en `docker-compose.yml` y en `spring.datasource.url` de `application.properties`.

Para detenerlo: `docker compose down` (los datos persisten en un volumen Docker; usar `docker compose down -v` para borrarlos por completo).

### 2. Abrir el proyecto en IntelliJ IDEA

1. `File -> Open` y seleccionar la carpeta `tarea1-crud-backend`.
2. IntelliJ detectará el proyecto Gradle automáticamente y descargará las dependencias.
3. Ejecutar la clase `com.project.demo.DemoApplication` (botón ▶️ o `Shift+F10`).

También se puede ejecutar desde terminal:

```bash
./gradlew bootRun
```

La aplicación queda disponible en `http://localhost:8080`.

### 3. Verificar que todo funciona

Al arrancar, los *seeders* crean automáticamente (si no existen):

- Roles: `SUPER_ADMIN_ROLE`, `USER`.
- Usuario `superadmin@tarea1.com` / `SuperAdmin123!` con rol `SUPER_ADMIN_ROLE`.
- Usuario `user@tarea1.com` / `User123!` con rol `USER`.

No se crea ninguna categoría ni producto de ejemplo.

### 4. Probar con Insomnia

Importar el archivo `insomnia-collection.json` (ubicado en la raíz del repo) en Insomnia. Incluye:

- **Auth**: login con el usuario `SUPER_ADMIN_ROLE` y con el usuario `USER`.
- **Categories**: crear, listar, obtener por id, actualizar y borrar. Incluye una petición que demuestra que un `USER` recibe `403` al intentar crear.
- **Products**: mismo set de operaciones, demostrando la relación con `Category` vía `categoryId`. Incluye una petición que demuestra `403` para `USER` en creación.

El token de autenticación se inyecta automáticamente en cada petición protegida tomándolo de la respuesta del login correspondiente (no es necesario copiarlo manualmente).

## Endpoints

```
POST   /auth/login                 -> público

GET    /categories                 -> autenticado (paginado: ?page=1&size=10)
GET    /categories/{id}            -> autenticado
POST   /categories                 -> SUPER_ADMIN_ROLE
PUT    /categories/{id}            -> SUPER_ADMIN_ROLE
DELETE /categories/{id}            -> SUPER_ADMIN_ROLE

GET    /products                   -> autenticado (paginado: ?page=1&size=10)
GET    /products/{id}              -> autenticado
POST   /products                   -> SUPER_ADMIN_ROLE
PUT    /products/{id}              -> SUPER_ADMIN_ROLE
DELETE /products/{id}              -> SUPER_ADMIN_ROLE
```

## Notas técnicas

- Base de datos: **PostgreSQL** (en lugar de MySQL/MariaDB) corriendo en Docker local, por instrucción explícita del profesor para este proyecto en particular.
- `spring.jpa.hibernate.ddl-auto=update`: las tablas se crean/actualizan automáticamente al arrancar.
- No existe endpoint de registro de usuarios: los usuarios se crean únicamente vía seeder.
