# Tarea 1 - CRUD back-end (Proyecto 3)

Proyecto académico del curso Proyecto 3 (Bachillerato en Ingeniería del Software, Universidad Cenfotec). Es una API REST construida con Spring Boot para la gestión de productos y categorías, con autenticación y control de acceso por roles.

## ¿Qué hace?

- Administra **Categorías** y **Productos**, donde cada producto pertenece a una única categoría.
- Toda la API requiere autenticación mediante **JWT**.
- Existen dos roles: `SUPER_ADMIN_ROLE`, que puede crear, actualizar y eliminar Productos y Categorías, y `USER`, que únicamente puede consultarlos.
- Los roles y los usuarios iniciales se crean automáticamente al levantar la aplicación; no existe un endpoint de registro público.

## Tecnologías

- Java 21 y Spring Boot
- Spring Data JPA
- Spring Security con JWT
- PostgreSQL
- Gradle

## Estructura

El proyecto sigue una arquitectura por capas (controlador, servicio, repositorio y entidad), con DTOs para separar lo que se expone por la API de las entidades de persistencia.
