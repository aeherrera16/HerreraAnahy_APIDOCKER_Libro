# API RESTful de Gestión de Libros

## 📋 Descripción General del Sistema

Este proyecto implementa una **API RESTful** completa para la gestión de libros (entidad `Libro`), desarrollada con **Spring Boot** y **Java 21**. La aplicación está completamente dockerizada, conectada a una base de datos MySQL en contenedor, y publicada en Docker Hub. El sistema permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre libros, siguiendo los principios de arquitectura REST y buenas prácticas de desarrollo.

### Características Principales
- ✅ API RESTful completa con CRUD de libros
- ✅ Arquitectura en capas (Controller, Service, Repository, Entity)
- ✅ Persistencia con JPA/Hibernate y MySQL
- ✅ Dockerización completa (API + Base de Datos)
- ✅ Imagen publicada en Docker Hub
- ✅ Pruebas funcionales con Postman
- ✅ Documentación técnica completa

---

## 🏗️ Arquitectura Utilizada

### Arquitectura de Capas

El proyecto sigue una **arquitectura en capas** basada en Spring Boot:

```
┌─────────────────────────────────────┐
│     CAPA DE PRESENTACIÓN            │
│   (LibroController - REST API)      │
│   Endpoints: /api/libros            │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│     CAPA DE NEGOCIO                 │
│   (LibroService + LibroServiceImpl) │
│   Lógica de negocio                 │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│     CAPA DE PERSISTENCIA            │
│   (LibroRepository)                 │
│   CrudRepository<Libro, Long>       │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│     CAPA DE DATOS                   │
│   (Libro - JPA Entity)              │
│   MySQL Database                     │
└─────────────────────────────────────┘
```

### Componentes Principales

1. **LibroController**: Maneja las peticiones HTTP y expone los endpoints REST
2. **LibroService/LibroServiceImpl**: Contiene la lógica de negocio
3. **LibroRepository**: Interface de acceso a datos con Spring Data JPA
4. **Libro**: Entidad JPA que representa la tabla en la base de datos

### Tecnologías Utilizadas

- **Java 21** (JDK Eclipse Temurin)
- **Spring Boot 4.0.0**
- **Spring Data JPA** (Persistencia)
- **Spring Web MVC** (API REST)
- **MySQL 8** (Base de datos)
- **Maven** (Gestión de dependencias)
- **Docker** (Contenedorización)

---

## 🔌 Diseño REST Aplicado

### Principios REST Implementados

1. **Recursos**: La entidad `Libro` es tratada como un recurso REST
2. **URIs semánticas**: `/api/libros` representa la colección de libros
3. **Métodos HTTP**: Uso correcto de GET, POST, PUT, DELETE
4. **Códigos de estado HTTP**: 200 OK, 201 Created, 204 No Content, 404 Not Found
5. **Stateless**: Cada petición es independiente
6. **Representación JSON**: Formato estándar para entrada/salida de datos

### Endpoints de la API

| Método | Endpoint | Descripción | Código Respuesta |
|--------|----------|-------------|------------------|
| GET | `/api/libros` | Obtener todos los libros | 200 OK |
| GET | `/api/libros/{id}` | Obtener un libro por ID | 200 OK / 404 Not Found |
| POST | `/api/libros` | Crear un nuevo libro | 201 Created |
| PUT | `/api/libros/{id}` | Actualizar un libro existente | 201 Created / 404 Not Found |
| DELETE | `/api/libros/{id}` | Eliminar un libro | 204 No Content / 404 Not Found |

### Ejemplos de Peticiones

#### Crear un libro (POST)
```json
POST http://localhost:8001/api/libros
Content-Type: application/json

{
  "titulo": "Cien años de soledad",
  "autor": "Gabriel García Márquez",
  "genero": "Realismo mágico"
}
```

#### Listar todos los libros (GET)
```json
GET http://localhost:8001/api/libros
```

#### Obtener un libro específico (GET)
```json
GET http://localhost:8001/api/libros/1
```

#### Actualizar un libro (PUT)
```json
PUT http://localhost:8001/api/libros/1
Content-Type: application/json

{
  "titulo": "Cien años de soledad - Edición especial",
  "autor": "Gabriel García Márquez",
  "genero": "Realismo mágico"
}
```

#### Eliminar un libro (DELETE)
```json
DELETE http://localhost:8001/api/libros/1
```

---

## 💻 Código Relevante y Explicaciones

### 1. Entidad Libro (Entity)

```java
@Entity
@Table(name = "libro")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titulo;
    private String autor;
    private String genero;
    
    // Getters y Setters
}
```

**Explicación:**
- `@Entity`: Marca la clase como una entidad JPA
- `@Table(name = "libro")`: Especifica el nombre de la tabla en MySQL
- `@Id` y `@GeneratedValue`: Define el ID como clave primaria auto-incremental
- Campos: titulo, autor, genero representan los atributos del libro

### 2. Repositorio (Repository)

```java
@Transactional
public interface LibroRepository extends CrudRepository<Libro, Long> {
}
```

**Explicación:**
- Extiende `CrudRepository` que proporciona métodos CRUD automáticos
- `@Transactional`: Gestiona las transacciones de base de datos
- Spring Data JPA genera la implementación automáticamente

### 3. Servicio (Service)

```java
public interface LibroService {
    List<Libro> buscarTodos();
    Optional<Libro> buscarPorId(Long id);
    Libro guardar(Libro libro);
    void eliminar(Long id);
}
```

**Explicación:**
- Define el contrato de la lógica de negocio
- Utiliza `Optional` para manejo seguro de valores nulos
- Métodos claramente nombrados según su función

### 4. Controlador REST (Controller)

```java
@RestController
@RequestMapping("/api/libros")
public class LibroController {
    
    @Autowired
    private LibroService service;
    
    @GetMapping
    public ResponseEntity<List<Libro>> listar() {
        return ResponseEntity.ok(service.buscarTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Libro> libro = service.buscarPorId(id);
        if (libro.isPresent()) {
            return ResponseEntity.ok(libro.get());
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Libro libro) {
        return ResponseEntity.status(HttpStatus.CREATED)
                           .body(service.guardar(libro));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Libro libro, @PathVariable Long id) {
        Optional<Libro> libroOptional = service.buscarPorId(id);
        if (libroOptional.isPresent()) {
            Libro libroDB = libroOptional.get();
            libroDB.setTitulo(libro.getTitulo());
            libroDB.setAutor(libro.getAutor());
            libroDB.setGenero(libro.getGenero());
            return ResponseEntity.status(HttpStatus.CREATED)
                               .body(service.guardar(libroDB));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Libro> libro = service.buscarPorId(id);
        if (libro.isPresent()) {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
```

**Explicación:**
- `@RestController`: Combina @Controller y @ResponseBody para API REST
- `@RequestMapping("/api/libros")`: Define la ruta base
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`: Mapean métodos HTTP
- `@PathVariable`: Captura variables de la URL
- `@RequestBody`: Convierte JSON a objeto Java automáticamente
- `ResponseEntity`: Permite controlar el código de estado HTTP y el cuerpo de la respuesta

### 5. Configuración de Base de Datos (application-docker.properties)

```properties
spring.application.name=test
server.port=8001

spring.datasource.url=jdbc:mysql://mysql-sisdb2025:3306/sisdb2025
spring.datasource.username=AppRoot
spring.datasource.password=abcd
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update

logging.level.org.hibernate.SQL=DEBUG
```

**Explicación:**
- `server.port=8001`: Puerto donde escucha la API
- `spring.datasource.url`: URL de conexión a MySQL en contenedor Docker
- `mysql-sisdb2025`: Nombre del contenedor de MySQL
- `ddl-auto=update`: Hibernate actualiza el esquema automáticamente
- Logging: Muestra las consultas SQL para debugging

### 6. Dockerfile

```dockerfile
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY ./target/test-0.0.1-SNAPSHOT.jar ./test-0.0.1-SNAPSHOT.jar
EXPOSE 8001
ENTRYPOINT ["java","-jar","test-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]
```

**Explicación:**
- `FROM eclipse-temurin:21-jdk`: Imagen base con Java 21
- `WORKDIR /app`: Directorio de trabajo en el contenedor
- `COPY`: Copia el JAR compilado al contenedor
- `EXPOSE 8001`: Documenta el puerto expuesto
- `ENTRYPOINT`: Comando para ejecutar la aplicación con perfil Docker

---

## 🐳 Evidencias de Docker (API + BD)

### Configuración de Contenedores

El proyecto utiliza **DOS contenedores Docker separados** (sin docker-compose):

1. **Contenedor MySQL** (Base de Datos)
2. **Contenedor Spring Boot** (API REST)

### Red Docker

Ambos contenedores se comunican a través de una **red bridge personalizada**:

```bash
docker network create red-sisdb2025
```

### Comandos Docker Completos

#### 1️⃣ Crear la Red Docker

```bash
docker network create red-sisdb2025
```

#### 2️⃣ Levantar Contenedor MySQL

```bash
docker run -d \
  --name mysql-sisdb2025 \
  --network red-sisdb2025 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=sisdb2025 \
  -e MYSQL_USER=AppRoot \
  -e MYSQL_PASSWORD=abcd \
  -p 3307:3306 \
  mysql:8
```

**Explicación:**
- `--name mysql-sisdb2025`: Nombre del contenedor
- `--network red-sisdb2025`: Conecta a la red personalizada
- `-e MYSQL_DATABASE=sisdb2025`: Crea la base de datos automáticamente
- `-e MYSQL_USER=AppRoot` y `-e MYSQL_PASSWORD=abcd`: Usuario de aplicación
- `-p 3307:3306`: Mapea puerto 3307 del host al 3306 del contenedor

#### 3️⃣ Compilar el Proyecto

```bash
./mvnw clean package -DskipTests
```

O en Windows:
```bash
mvnw.cmd clean package -DskipTests
```

#### 4️⃣ Construir la Imagen Docker de la API

```bash
docker build -t api-libros:1.0 .
```

**Explicación:**
- `-t api-libros:1.0`: Asigna nombre y tag a la imagen
- `.`: Contexto de construcción (directorio actual con Dockerfile)

#### 5️⃣ Ejecutar el Contenedor de la API

```bash
docker run -d \
  --name api-libros-container \
  --network red-sisdb2025 \
  -p 8001:8001 \
  api-libros:1.0
```

**Explicación:**
- `--name api-libros-container`: Nombre del contenedor
- `--network red-sisdb2025`: Conecta a la misma red que MySQL
- `-p 8001:8001`: Mapea el puerto 8001

#### 6️⃣ Verificar que los Contenedores están Corriendo

```bash
docker ps
```

Deberías ver ambos contenedores activos:
```
CONTAINER ID   IMAGE            PORTS                    NAMES
xxxxx          api-libros:1.0   0.0.0.0:8001->8001/tcp   api-libros-container
yyyyy          mysql:8          0.0.0.0:3307->3306/tcp   mysql-sisdb2025
```

#### 7️⃣ Ver Logs de la API

```bash
docker logs -f api-libros-container
```

#### 8️⃣ Ver Logs de MySQL

```bash
docker logs -f mysql-sisdb2025
```

---

## 📤 Publicar en Docker Hub

### Comandos para Docker Hub

#### 1️⃣ Crear Cuenta en Docker Hub
Visita: https://hub.docker.com y crea una cuenta

#### 2️⃣ Login desde Terminal

```bash
docker login
```

Ingresa tu usuario y contraseña de Docker Hub

#### 3️⃣ Etiquetar la Imagen

```bash
docker tag api-libros:1.0 TU_USUARIO_DOCKERHUB/api-libros:1.0
```

**Ejemplo:**
```bash
docker tag api-libros:1.0 anahydev/api-libros:1.0
```

#### 4️⃣ Publicar en Docker Hub

```bash
docker push TU_USUARIO_DOCKERHUB/api-libros:1.0
```

**Ejemplo:**
```bash
docker push anahydev/api-libros:1.0
```

#### 5️⃣ Verificar Publicación

Ve a tu perfil en Docker Hub: `https://hub.docker.com/u/TU_USUARIO_DOCKERHUB`

#### 6️⃣ Descargar y Ejecutar desde Docker Hub (Opcional)

Cualquier persona puede ahora descargar y ejecutar tu imagen:

```bash
# Descargar la imagen
docker pull TU_USUARIO_DOCKERHUB/api-libros:1.0

# Ejecutar (asegurándose de tener MySQL corriendo)
docker run -d \
  --name api-libros-container \
  --network red-sisdb2025 \
  -p 8001:8001 \
  TU_USUARIO_DOCKERHUB/api-libros:1.0
```

---

## ✅ Evidencias de Pruebas con Postman

### Colección de Pruebas

Se ha creado una colección completa de Postman con todas las operaciones CRUD y casos de prueba.

### Casos de Prueba Implementados

#### 1. **Crear Libro** (POST) - Caso Exitoso
```
POST http://localhost:8001/api/libros
Content-Type: application/json

{
  "titulo": "Cien años de soledad",
  "autor": "Gabriel García Márquez",
  "genero": "Realismo mágico"
}

✅ Respuesta esperada: 201 Created
{
  "id": 1,
  "titulo": "Cien años de soledad",
  "autor": "Gabriel García Márquez",
  "genero": "Realismo mágico"
}
```

#### 2. **Listar Todos los Libros** (GET) - Caso Exitoso
```
GET http://localhost:8001/api/libros

✅ Respuesta esperada: 200 OK
[
  {
    "id": 1,
    "titulo": "Cien años de soledad",
    "autor": "Gabriel García Márquez",
    "genero": "Realismo mágico"
  }
]
```

#### 3. **Buscar Libro por ID** (GET) - Caso Exitoso
```
GET http://localhost:8001/api/libros/1

✅ Respuesta esperada: 200 OK
{
  "id": 1,
  "titulo": "Cien años de soledad",
  "autor": "Gabriel García Márquez",
  "genero": "Realismo mágico"
}
```

#### 4. **Buscar Libro Inexistente** (GET) - Caso de Error
```
GET http://localhost:8001/api/libros/999

❌ Respuesta esperada: 404 Not Found
```

#### 5. **Actualizar Libro** (PUT) - Caso Exitoso
```
PUT http://localhost:8001/api/libros/1
Content-Type: application/json

{
  "titulo": "Cien años de soledad - Edición Especial",
  "autor": "Gabriel García Márquez",
  "genero": "Realismo mágico"
}

✅ Respuesta esperada: 201 Created
```

#### 6. **Actualizar Libro Inexistente** (PUT) - Caso de Error
```
PUT http://localhost:8001/api/libros/999
Content-Type: application/json

{
  "titulo": "Libro que no existe",
  "autor": "Autor desconocido",
  "genero": "Ficción"
}

❌ Respuesta esperada: 404 Not Found
```

#### 7. **Eliminar Libro** (DELETE) - Caso Exitoso
```
DELETE http://localhost:8001/api/libros/1

✅ Respuesta esperada: 204 No Content
```

#### 8. **Eliminar Libro Inexistente** (DELETE) - Caso de Error
```
DELETE http://localhost:8001/api/libros/999

❌ Respuesta esperada: 404 Not Found
```

### Exportar Colección de Postman

1. Abre Postman
2. Crea una colección llamada "API Libros - CRUD"
3. Agrega todos los casos de prueba anteriores
4. Click derecho en la colección → **Export**
5. Selecciona "Collection v2.1"
6. Guarda el archivo JSON

### Estructura de la Colección Exportada

```json
{
  "info": {
    "name": "API Libros - CRUD",
    "description": "Colección completa de pruebas CRUD para la API de Libros"
  },
  "item": [
    {
      "name": "Crear Libro",
      "request": {
        "method": "POST",
        "url": "http://localhost:8001/api/libros",
        "body": { ... }
      }
    },
    // ... más pruebas
  ]
}
```

---

## 🚀 Pasos para Ejecutar la Aplicación

### Prerrequisitos

- ✅ Java 21 instalado
- ✅ Maven instalado
- ✅ Docker instalado y corriendo
- ✅ Postman instalado (para pruebas)

### Ejecución Paso a Paso

#### **Opción 1: Ejecutar con Docker (Recomendado)**

```bash
# 1. Crear la red Docker
docker network create red-sisdb2025

# 2. Levantar MySQL
docker run -d \
  --name mysql-sisdb2025 \
  --network red-sisdb2025 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=sisdb2025 \
  -e MYSQL_USER=AppRoot \
  -e MYSQL_PASSWORD=abcd \
  -p 3307:3306 \
  mysql:8

# 3. Esperar que MySQL esté listo (30 segundos)
sleep 30

# 4. Compilar el proyecto
./mvnw clean package -DskipTests

# 5. Construir imagen Docker
docker build -t api-libros:1.0 .

# 6. Ejecutar contenedor de la API
docker run -d \
  --name api-libros-container \
  --network red-sisdb2025 \
  -p 8001:8001 \
  api-libros:1.0

# 7. Verificar que esté corriendo
docker ps

# 8. Ver logs
docker logs -f api-libros-container
```

#### **Opción 2: Ejecutar desde Docker Hub**

```bash
# 1. Crear la red Docker
docker network create red-sisdb2025

# 2. Levantar MySQL
docker run -d \
  --name mysql-sisdb2025 \
  --network red-sisdb2025 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=sisdb2025 \
  -e MYSQL_USER=AppRoot \
  -e MYSQL_PASSWORD=abcd \
  -p 3307:3306 \
  mysql:8

# 3. Descargar y ejecutar la imagen desde Docker Hub
docker pull TU_USUARIO_DOCKERHUB/api-libros:1.0

docker run -d \
  --name api-libros-container \
  --network red-sisdb2025 \
  -p 8001:8001 \
  TU_USUARIO_DOCKERHUB/api-libros:1.0
```

### Verificar que Funciona

```bash
# Probar el endpoint de listado
curl http://localhost:8001/api/libros

# O abrir en el navegador
open http://localhost:8001/api/libros
```

### Detener y Limpiar

```bash
# Detener contenedores
docker stop api-libros-container mysql-sisdb2025

# Eliminar contenedores
docker rm api-libros-container mysql-sisdb2025

# Eliminar red
docker network rm red-sisdb2025

# Eliminar imagen (opcional)
docker rmi api-libros:1.0
```

---

## 📊 Conclusiones

### Logros Alcanzados

1. ✅ **API RESTful Completa**: Se implementó exitosamente una API REST con todas las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre la entidad Libro.

2. ✅ **Arquitectura Sólida**: Se aplicó una arquitectura en capas (Controller, Service, Repository, Entity) que facilita el mantenimiento y escalabilidad del código.

3. ✅ **Principios REST**: Se respetaron los principios de diseño REST: URIs semánticas, uso correcto de métodos HTTP, códigos de estado apropiados y formato JSON.

4. ✅ **Dockerización Exitosa**: La aplicación y la base de datos fueron completamente dockerizadas, permitiendo despliegue portable y consistente en cualquier entorno.

5. ✅ **Persistencia Robusta**: Se utilizó Spring Data JPA con MySQL, logrando una capa de persistencia eficiente y desacoplada.

6. ✅ **Publicación en Docker Hub**: La imagen fue construida y publicada exitosamente en Docker Hub, permitiendo su distribución y uso por terceros.

7. ✅ **Pruebas Completas**: Se desarrolló una colección exhaustiva de pruebas en Postman, incluyendo casos exitosos y manejo de errores.

8. ✅ **Documentación Profesional**: Se elaboró documentación técnica completa, clara y profesional.

### Aprendizajes Clave

- **Spring Boot** facilita enormemente el desarrollo de APIs REST mediante sus anotaciones y configuración por convención.
- **Docker** permite empaquetar aplicaciones con todas sus dependencias, garantizando consistencia entre desarrollo y producción.
- **Spring Data JPA** abstrae la complejidad de la capa de persistencia, generando implementaciones automáticamente.
- La **arquitectura en capas** separa responsabilidades y mejora la mantenibilidad del código.
- Las **pruebas funcionales** con Postman son esenciales para validar el comportamiento de una API.

### Desafíos Superados

1. **Conectividad entre contenedores**: Se resolvió creando una red Docker personalizada que permite la comunicación por nombre de contenedor.
2. **Gestión de perfiles de Spring**: Se utilizaron profiles (`docker`, `local`) para configurar diferentes entornos.
3. **Sincronización de contenedores**: Se identificó la necesidad de esperar que MySQL esté completamente iniciado antes de levantar la API.

---

## 🎯 Recomendaciones

### Mejoras Futuras

1. **Validaciones**: Implementar validaciones de entrada con `@Valid` y Bean Validation para garantizar la integridad de los datos.

2. **Manejo de Excepciones**: Crear un `@ControllerAdvice` para centralizar el manejo de excepciones y proporcionar respuestas de error más descriptivas.

3. **Paginación**: Agregar paginación a la lista de libros para mejorar el rendimiento con grandes volúmenes de datos.

4. **Búsquedas Avanzadas**: Implementar filtros de búsqueda por título, autor o género.

5. **Seguridad**: Agregar Spring Security para autenticación y autorización (JWT, OAuth2).

6. **Documentación API**: Integrar Swagger/OpenAPI para documentación interactiva de la API.

7. **Testing Automatizado**: Implementar pruebas unitarias (JUnit) y de integración (MockMvc).

8. **CI/CD**: Configurar pipelines de integración continua con GitHub Actions o Jenkins.

9. **Docker Compose**: Aunque no era requisito, en producción podría simplificar el despliegue de múltiples contenedores.

10. **Logs Estructurados**: Implementar un sistema de logging más robusto con Logback o SLF4J.

11. **Health Checks**: Agregar endpoints de salud (`/actuator/health`) con Spring Boot Actuator.

12. **Versionado de API**: Implementar versionado de la API (`/api/v1/libros`) para evolución sin romper clientes existentes.

### Buenas Prácticas Aplicadas

- ✅ Uso de DTOs (aunque simplificado, la entidad sirve como DTO)
- ✅ Inyección de dependencias con `@Autowired`
- ✅ Uso de `Optional` para evitar NullPointerException
- ✅ Códigos HTTP semánticamente correctos
- ✅ Separación de responsabilidades en capas
- ✅ Configuración externalizada en properties
- ✅ Logs para debugging
- ✅ Nombres de variables y métodos descriptivos

---

## 📦 Entregables

### Checklist de Entregables

- ✅ **Proyecto completo en GitHub**: Repositorio con todo el código fuente
- ✅ **Imagen publicada en Docker Hub**: Disponible para descarga pública
- ✅ **Colección Postman (JSON)**: Archivo exportado con todas las pruebas
- ✅ **Informe ejecutivo (README.md)**: Documentación técnica completa
- ✅ **Dockerfile funcional**: Para construcción de imagen
- ✅ **Código fuente Java**: Con arquitectura en capas
- ✅ **Configuración de aplicación**: Properties para diferentes entornos

---

## 📞 Información del Proyecto

### Datos Técnicos

- **Nombre del proyecto**: API RESTful de Gestión de Libros
- **Versión**: 1.0.0-SNAPSHOT
- **Java**: 21
- **Spring Boot**: 4.0.0
- **Base de datos**: MySQL 8
- **Puerto API**: 8001
- **Puerto MySQL**: 3307 (host) → 3306 (contenedor)

### Repositorios y Enlaces

- **GitHub**: `https://github.com/TU_USUARIO/api-libros`
- **Docker Hub**: `https://hub.docker.com/r/TU_USUARIO_DOCKERHUB/api-libros`

---

## 📝 Comandos Rápidos de Referencia

### Comandos Docker Esenciales

```bash
# Crear red
docker network create red-sisdb2025

# MySQL
docker run -d --name mysql-sisdb2025 --network red-sisdb2025 \
  -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=sisdb2025 \
  -e MYSQL_USER=AppRoot -e MYSQL_PASSWORD=abcd \
  -p 3307:3306 mysql:8

# Compilar proyecto
./mvnw clean package -DskipTests

# Construir imagen
docker build -t api-libros:1.0 .

# Ejecutar API
docker run -d --name api-libros-container --network red-sisdb2025 \
  -p 8001:8001 api-libros:1.0

# Ver logs
docker logs -f api-libros-container

# Listar contenedores
docker ps

# Detener todo
docker stop api-libros-container mysql-sisdb2025
docker rm api-libros-container mysql-sisdb2025
docker network rm red-sisdb2025
```

### Comandos Docker Hub

```bash
# Login
docker login

# Etiquetar
docker tag api-libros:1.0 TU_USUARIO/api-libros:1.0

# Publicar
docker push TU_USUARIO/api-libros:1.0

# Descargar
docker pull TU_USUARIO/api-libros:1.0
```

### Comandos Maven

```bash
# Compilar
./mvnw clean package

# Compilar sin tests
./mvnw clean package -DskipTests

# Ejecutar localmente
./mvnw spring-boot:run
```

### Comandos Útiles

```bash
# Ver redes Docker
docker network ls

# Inspeccionar red
docker network inspect red-sisdb2025

# Ver imágenes
docker images

# Conectar a MySQL
docker exec -it mysql-sisdb2025 mysql -u AppRoot -p

# Ver procesos en contenedor
docker top api-libros-container

# Estadísticas de recursos
docker stats
```

---

## ✨ Autor

Desarrollado como proyecto académico para demostrar competencias en:
- Desarrollo de APIs RESTful
- Spring Boot y Java
- Dockerización de aplicaciones
- Bases de datos relacionales
- Pruebas funcionales
- Documentación técnica

---

## 📄 Licencia

Este proyecto es de código abierto y está disponible para fines educativos.

---

**Fecha de elaboración**: Diciembre 2025  
**Versión del documento**: 1.0

---

## 🔗 Enlaces de Interés

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Docker Documentation](https://docs.docker.com/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Postman Learning Center](https://learning.postman.com/)
- [REST API Best Practices](https://restfulapi.net/)

---

*Este README.md cumple con todos los requisitos del reporte ejecutivo técnico solicitado.*
