# 📋 Incident Manager - Avance 01

Sistema de gestión de incidencias técnicas desarrollado con **Spring Boot**. Este proyecto permite el registro, seguimiento, actualización y eliminación de reportes de errores o tareas técnicas de manera eficiente.

## 🚀 Tecnologías Utilizadas
* **Java 25**
* **Spring Boot 3.x** (Spring Data JPA, Spring Security, Spring Web)
* **H2 Database** (Base de datos en memoria para persistencia rápida en desarrollo)
* **Maven** (Gestor de dependencias y construcción)
* **JUnit 5** (Pruebas unitarias y de integración)

## 🏗️ Arquitectura del Proyecto
El proyecto implementa una **Arquitectura en Capas** para garantizar el desacoplamiento y la mantenibilidad:
* **Controller:** Capa de entrada que expone los endpoints REST.
* **Service:** Capa de lógica de negocio y validaciones.
* **Repository:** Capa de persistencia que utiliza Spring Data JPA.
* **Model:** Definición de entidades de datos (`Incident`).
* **Config:** Configuraciones globales de seguridad y acceso.

## 🛣️ API Endpoints
| Método | Endpoint | Acción |
| :--- | :--- | :--- |
| **GET** | `/api/incidents` | Obtener todas las incidencias. |
| **POST** | `/api/incidents` | Crear un nuevo reporte. |
| **PUT** | `/api/incidents/{id}` | Actualizar datos de una incidencia. |
| **DELETE** | `/api/incidents/{id}` | Eliminar una incidencia del sistema. |

## 🛠️ Instalación y Ejecución
1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/FabrizioMn/Incident-Manager.git]
   cd Incident-Manager/backend/incident-manager
    ```

2. **Correr la aplicacion**

    *En Windows*
    ```bash
    mvnw.cmd spring-boot:run
    ```

    *En Linux*
    ```bash
    ./mvnw spring-boot:run
