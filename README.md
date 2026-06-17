# Tarea 1 - CRUD back-end (Proyecto 3)

API REST en Spring Boot con autenticación JWT, roles (`SUPER_ADMIN_ROLE`, `USER`) y CRUD completo para `Category` y `Product`.

## Inicio rápido

```bash
docker compose up -d        # 1. levantar PostgreSQL
./gradlew bootRun           # 2. levantar la app (o ejecutar DemoApplication desde IntelliJ)
```

La API queda en `http://localhost:8080`. Para probarla, importa **uno de estos dos archivos** (ambos están en la raíz del repo y cubren exactamente los mismos casos: login, CRUD de Categoría y Producto, y verificación de roles):

- **Insomnia**: `insomnia-collection.json`
- **Postman**: `postman-collection.json`

Detalle de cada paso más abajo.

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

### 4. Probar con Insomnia o con Postman

Ambas colecciones cubren exactamente los mismos casos (login, CRUD completo de `Category` y `Product`, y pruebas de restricción de rol con `403`). Usa la que tengas a mano; no es necesario importar las dos.

**Opción A — Insomnia** (`insomnia-collection.json`):

1. `Insomnia -> Application -> Preferences -> Data -> Import Data` (o el botón "Import" del workspace) y selecciona el archivo.
2. Folders: **Auth**, **Categories**, **Products**.
3. El token se inyecta automáticamente en cada petición protegida, tomándolo de la respuesta del login correspondiente (no hay que copiarlo a mano).

**Opción B — Postman** (`postman-collection.json`):

1. `File -> Import` y selecciona el archivo.
2. Carpetas: **Auth**, **Categories**, **Products**.
3. Ejecuta primero **Auth -> Login Super Admin** y **Auth -> Login User**: cada uno guarda su token en una variable de colección automáticamente (`super_admin_token`, `user_token`) vía un script en la pestaña "Tests".
4. **Categories -> Create Category (SUPER_ADMIN)** guarda el id creado en `category_id`, que las demás peticiones de Categories y Products usan automáticamente. No hace falta editar nada a mano.

En ambos casos, las peticiones de creación con el usuario `USER` (marcadas "expect 403") deben fallar con `403 Forbidden` — eso confirma que las reglas de rol funcionan.

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
