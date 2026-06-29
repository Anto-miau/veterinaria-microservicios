# 🐾 Veterinaria Microservicios — DSY1103

Proyecto **Sumativa 3** — Desarrollo FullStack 1 — DuocUC

## 📋 Descripción del proyecto

Sistema de gestión para una clínica veterinaria, desarrollado bajo una **arquitectura de microservicios** con Spring Boot. El dominio cubre el registro de mascotas, sus dueños, citas veterinarias, tratamientos, insumos y métodos de pago, además de la ubicación geográfica (región/comuna) asociada a cada dueño.

El proyecto migra desde un monolito (Unidad 1 y 2) hacia una arquitectura distribuida (Unidad 3), incorporando descubrimiento de servicios (Eureka), un API Gateway centralizado, documentación interactiva con Swagger/OpenAPI, comunicación entre microservicios vía WebClient, y pruebas unitarias con JUnit + Mockito + DataFaker.



`eureka-server`
|
 8761 
|
 Servidor de descubrimiento de servicios (Service Discovery) 
|
|
`api-gateway`
|
 8080 
|
 Punto de entrada único, enruta las peticiones a cada microservicio 
|
|
`ms-ubicacion`
|
 8081 
|
 Gestión de Región y Comuna 
|
|
`ms-personas`
|
 8082 
|
 Gestión de Dueño, Veterinario y Especialidad 
|
|
`ms-mascotas`
|
 0 (asignado dinámicamente vía Eureka) 
|
 Gestión de Mascota, Especie, Raza, Enfermedad y Contacto 
|
|
`ms-citas`
|
 0 (asignado dinámicamente vía Eureka) 
|
 Gestión de Cita, Tratamiento, Insumo y Método de Pago 
|

Cada microservicio implementa:
- Arquitectura en capas: `model` → `repository` → `service` (+ `validaciones`) → `controller`
- **Dos versiones de API**: `v1` (CRUD REST básico) y `v2` (con HATEOAS)
- Migraciones de base de datos con **Flyway**
- Documentación interactiva con **Swagger/OpenAPI**
- Registro automático en **Eureka**

## 🔗 Comunicación entre microservicios

`ms-citas` consulta a `ms-mascotas` vía **WebClient** (con balanceo de carga a través de Eureka) para enriquecer la información de cada cita con los datos de la mascota asociada.

## 🌐 Rutas principales del API Gateway

Todas las peticiones pasan por `http://localhost:8080`:

**Hacia `ms-mascotas`:**
```
/api/v1/mascotas/**           /api/v2/mascotas/**
/api/v1/especies/**           /api/v2/especies/**
/api/v1/razas/**              /api/v2/razas/**
/api/v1/enfermedades/**       /api/v2/enfermedades/**
/api/v1/contactos/**          /api/v2/contactos/**
/api/v1/contactos-mascota/**       /api/v2/contactos-mascota/**
/api/v1/enfermedades-mascota/**    /api/v2/enfermedades-mascota/**
```

**Hacia `ms-citas`:**
```
/api/v1/citas/**              /api/v2/citas/**
/api/v1/tratamientos/**       /api/v2/tratamientos/**
/api/v1/insumos/**            /api/v2/insumos/**
/api/v1/pagos/**              /api/v2/pagos/**
/api/v1/tratamientos-cita/**       /api/v2/tratamientos-cita/**
/api/v1/insumos-tratamiento/**     /api/v2/insumos-tratamiento/**
/api/v1/pagos-cita/**              /api/v2/pagos-cita/**
/api/v1/veterinarios-cita/**       /api/v2/veterinarios-cita/**
```

## 📖 Documentación Swagger

|
 Recurso 
|
 URL 
|
|
---
|
---
|
|
**
Swagger unificado
**
 (a través del Gateway) 
|
 http://localhost:8080/swagger-ui/index.html 
|
|
 Swagger 
`ms-mascotas`
 (individual) 
|
 http://localhost:8083/doc/swagger-ui/index.html 
*
(puerto real puede variar, ver consola al iniciar)
*
|
|
 Swagger 
`ms-citas`
 (individual) 
|
 http://localhost:8084/doc/swagger-ui/index.html 
*
(puerto real puede variar, ver consola al iniciar)
*
|
|
 Dashboard de Eureka 
|
 http://localhost:8761 
|

## ⚙️ Requisitos previos

- Java 21
- Maven (o usar el wrapper `mvnw` incluido)
- MySQL 8+ corriendo en `localhost:3306`, usuario `root`, sin contraseña (ej. vía [Laragon](https://laragon.org/))

## 🚀 Instrucciones de ejecución local

### Opción 1 — Manual (orden importante)

Cada microservicio debe levantarse en **una terminal distinta**, siguiendo este orden:

```bash
# 1. Eureka Server (esperar a que termine de iniciar)
cd eureka
./mvnw spring-boot:run

# 2. ms-mascotas
cd ms-mascotas
./mvnw spring-boot:run

# 3. ms-citas
cd ms-citas
./mvnw spring-boot:run

# 4. API Gateway (al final, una vez que los anteriores estén registrados en Eureka)
cd api-gateway
./mvnw spring-boot:run
```

Las bases de datos se crean automáticamente (`createDatabaseIfNotExist=true`) y las tablas se generan vía Flyway al iniciar cada servicio.

### Opción 2 — Script automático

```bash
# Windows
iniciar-todo.bat

# Linux / Mac
./iniciar-todo.sh
```

### Verificación

1. Eureka: http://localhost:8761 → deben aparecer `MS-MASCOTAS`, `MS-CITAS` y `API-GATEWAY` registrados.
2. Swagger unificado: http://localhost:8080/swagger-ui/index.html
3. Probar un endpoint vía Gateway, ej: `GET http://localhost:8080/api/v1/especies`

## 🧪 Pruebas unitarias

Pruebas implementadas con **JUnit 5 + Mockito + DataFaker**, cubriendo la capa `Service` de cada entidad (búsqueda exitosa, búsqueda fallida, guardado exitoso, validación fallida, eliminación) en `ms-mascotas` y `ms-citas`.

```bash
cd ms-mascotas
./mvnw test

cd ms-citas
./mvnw test
```

## 🛠️ Stack tecnológico

- Spring Boot 4.0.6 / Java 21
- Spring Data JPA + MySQL
- Flyway (migraciones)
- Spring HATEOAS
- Springdoc OpenAPI (Swagger)
- Spring Cloud Gateway + Eureka (Netflix)
- WebClient (comunicación entre microservicios)
- JUnit 5 + Mockito + DataFaker
- Lombok
