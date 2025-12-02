# 📚 API de Gestión de Libros

> API REST simple para administrar libros usando Spring Boot, MySQL y Docker

## ¿Qué hace esta aplicación?

Esta es una API que te permite crear, ver, actualizar y eliminar libros de una base de datos. Todo funciona con peticiones HTTP y responde en formato JSON.

**Tecnologías:** Java 21 + Spring Boot + MySQL + Docker

---

## 🚀 Cómo empezar (Lo más fácil)

### Requisitos
- Tener Docker instalado
- Eso es todo 😊

### Pasos

**1. Crear una red para que los contenedores se comuniquen:**
```bash
docker network create red-sisdb2025
```

**2. Levantar la base de datos MySQL:**
```bash
docker run -d --name mysql-sisdb2025 --network red-sisdb2025 \
  -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=sisdb2025 \
  -e MYSQL_USER=AppRoot -e MYSQL_PASSWORD=abcd \
  -p 3307:3306 mysql:8
```

**3. Espera 30 segundos** para que MySQL termine de iniciar ☕

**4. Levantar la API desde Docker Hub:**
```bash
docker run -d --name api-libros-container --network red-sisdb2025 \
  -p 8001:8001 anahy/api-libros:1.0
```

**5. ¡Listo! Prueba que funciona:**

Abre tu navegador en: http://localhost:8001/api/libros

O desde terminal:
```bash
curl http://localhost:8001/api/libros
```

---

## 📖 ¿Qué puedes hacer con la API?

### Ver todos los libros
```
GET http://localhost:8001/api/libros
```

### Ver un libro específico
```
GET http://localhost:8001/api/libros/1
```

### Crear un libro nuevo
```
POST http://localhost:8001/api/libros
Content-Type: application/json

{
  "titulo": "Don Quijote de la Mancha",
  "autor": "Miguel de Cervantes",
  "genero": "Novela"
}
```

### Actualizar un libro
```
PUT http://localhost:8001/api/libros/1
Content-Type: application/json

{
  "titulo": "Don Quijote - Edición Especial",
  "autor": "Miguel de Cervantes",
  "genero": "Novela"
}
```

### Eliminar un libro
```
DELETE http://localhost:8001/api/libros/1
```

---

## 🧪 Probar con Postman

**1. Importa la colección:**
- Descarga: [`API-Libros-CRUD.postman_collection.json`](./API-Libros-CRUD.postman_collection.json)
- En Postman: Import → Upload Files → Selecciona el archivo

**2. Ejecuta las pruebas:**
- Click derecho en la colección → Run collection
- Verás 12 pruebas ejecutándose automáticamente

---

## 🏗️ Arquitectura Sencilla

```
┌─────────────────────┐
│   Tu Navegador      │  
│   o Postman         │
└──────────┬──────────┘
           │ HTTP (puerto 8001)
           ▼
┌─────────────────────┐
│   API Spring Boot   │ ← Contenedor Docker
│   /api/libros       │
└──────────┬──────────┘
           │ JDBC
           ▼
┌─────────────────────┐
│   MySQL Database    │ ← Contenedor Docker
│   sisdb2025         │
└─────────────────────┘
```

**Los contenedores hablan entre sí gracias a una red Docker llamada `red-sisdb2025`**

---

## � Estructura del Código

```
src/main/java/com/espe/test/test/
├── controllers/
│   └── LibroController.java    ← Endpoints de la API
├── services/
│   ├── LibroService.java        ← Interface
│   └── LibroServiceImpl.java    ← Lógica de negocio
├── repositories/
│   └── LibroRepository.java     ← Conexión a la BD
└── models/entities/
    └── Libro.java               ← Modelo de datos
```

**En resumen:**
- `Controller` → Recibe peticiones HTTP
- `Service` → Procesa la lógica
- `Repository` → Guarda/Lee de MySQL
- `Entity` → Define cómo es un libro

---

## 🐳 Comandos Docker Útiles

**Ver qué contenedores están corriendo:**
```bash
docker ps
```

**Ver logs de la API:**
```bash
docker logs -f api-libros-container
```

**Ver logs de MySQL:**
```bash
docker logs -f mysql-sisdb2025
```

**Detener todo:**
```bash
docker stop api-libros-container mysql-sisdb2025
```

**Eliminar contenedores:**
```bash
docker rm api-libros-container mysql-sisdb2025
```

**Eliminar la red:**
```bash
docker network rm red-sisdb2025
```

**Empezar de nuevo:**
```bash
# Detener y eliminar todo
docker stop api-libros-container mysql-sisdb2025
docker rm api-libros-container mysql-sisdb2025
docker network rm red-sisdb2025

# Volver a levantar
docker network create red-sisdb2025
docker run -d --name mysql-sisdb2025 --network red-sisdb2025 \
  -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=sisdb2025 \
  -e MYSQL_USER=AppRoot -e MYSQL_PASSWORD=abcd \
  -p 3307:3306 mysql:8

sleep 30

docker run -d --name api-libros-container --network red-sisdb2025 \
  -p 8001:8001 anahy/api-libros:1.0
```

---

## 🛠️ Compilar y Construir tu Propia Imagen

Si quieres modificar el código y crear tu propia imagen Docker:

**1. Clona el repositorio:**
```bash
git clone https://github.com/aeherrera16/HerreraAnahy_APIDOCKER_Libro.git
cd HerreraAnahy_APIDOCKER_Libro
```

**2. Compila el proyecto:**
```bash
./mvnw clean package -DskipTests
```

**3. Construye la imagen Docker:**
```bash
docker build -t mi-api-libros:1.0 .
```

**4. Ejecuta tu imagen:**
```bash
docker run -d --name api-libros-container --network red-sisdb2025 \
  -p 8001:8001 mi-api-libros:1.0
```

---

## 📤 Subir tu Imagen a Docker Hub

**1. Login:**
```bash
docker login
```

**2. Etiqueta tu imagen:**
```bash
docker tag mi-api-libros:1.0 TU_USUARIO/api-libros:1.0
```

**3. Súbela:**
```bash
docker push TU_USUARIO/api-libros:1.0
```

---

## 📝 Ejemplos de JSON para Probar

### Crear libros

### Crear libros

```json
{
  "titulo": "Cien años de soledad",
  "autor": "Gabriel García Márquez",
  "genero": "Realismo mágico"
}
```

```json
{
  "titulo": "1984",
  "autor": "George Orwell",
  "genero": "Distopía"
}
```

```json
{
  "titulo": "Harry Potter y la piedra filosofal",
  "autor": "J.K. Rowling",
  "genero": "Fantasía"
}
```

```json
{
  "titulo": "El principito",
  "autor": "Antoine de Saint-Exupéry",
  "genero": "Fábula"
}
```

---

## 🎯 ¿Qué aprendí con este proyecto?

- ✅ Crear APIs REST con Spring Boot
- ✅ Conectar una aplicación Java con MySQL
- ✅ Dockerizar aplicaciones (hacer que funcionen en cualquier computadora)
- ✅ Publicar imágenes en Docker Hub
- ✅ Probar APIs con Postman
- ✅ Trabajar con arquitectura en capas

---

## 🔗 Enlaces

- **Código en GitHub:** https://github.com/aeherrera16/HerreraAnahy_APIDOCKER_Libro
- **Imagen en Docker Hub:** https://hub.docker.com/r/anahy/api-libros
- **Colección Postman:** [API-Libros-CRUD.postman_collection.json](./API-Libros-CRUD.postman_collection.json)

---

## ❓ Problemas Comunes

### "Connection refused" al probar la API
→ Espera 30-60 segundos después de levantar MySQL antes de iniciar la API

### "Port 8001 is already in use"
→ Ya tienes algo corriendo en ese puerto. Detén el contenedor anterior:
```bash
docker stop api-libros-container
docker rm api-libros-container
```

### "Cannot connect to Docker daemon"
→ Asegúrate de que Docker Desktop esté abierto y corriendo

### La API no encuentra la base de datos
→ Verifica que ambos contenedores estén en la misma red:
```bash
docker network inspect red-sisdb2025
```

---

## 📚 Tecnologías Usadas

| Tecnología | Versión | Para qué sirve |
|------------|---------|----------------|
| Java | 21 | Lenguaje de programación |
| Spring Boot | 4.0.0 | Framework para crear la API |
| MySQL | 8 | Base de datos |
| Docker | Latest | Contenedores |
| Maven | Latest | Gestión de dependencias |
| Postman | Latest | Probar la API |

---

**Hecho con ❤️ para aprender Docker y Spring Boot**
