# Instrucciones para Usar la Colección de Postman

## 📥 Importar la Colección

### Opción 1: Importar desde archivo local

1. **Abre Postman**
2. Click en **"Import"** (esquina superior izquierda)
3. Click en **"Upload Files"**
4. Selecciona el archivo: `API-Libros-CRUD.postman_collection.json`
5. Click en **"Import"**

### Opción 2: Importar desde GitHub

1. **Abre Postman**
2. Click en **"Import"**
3. Click en **"Link"**
4. Pega esta URL:
   ```
   https://raw.githubusercontent.com/aeherrera16/HerreraAnahy_APIDOCKER_Libro/main/API-Libros-CRUD.postman_collection.json
   ```
5. Click en **"Continue"** y luego **"Import"**

---

## 🚀 Ejecutar las Pruebas

### Paso 1: Asegúrate de que la API esté corriendo

```bash
# Verificar que los contenedores estén activos
docker ps

# Deberías ver:
# - mysql-sisdb2025
# - api-libros-container
```

Si no están corriendo, levántalos:

```bash
# Levantar MySQL
docker run -d --name mysql-sisdb2025 --network red-sisdb2025 \
  -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=sisdb2025 \
  -e MYSQL_USER=AppRoot -e MYSQL_PASSWORD=abcd \
  -p 3307:3306 mysql:8

# Esperar 30 segundos
sleep 30

# Levantar la API
docker run -d --name api-libros-container --network red-sisdb2025 \
  -p 8001:8001 anahy/api-libros:1.0
```

### Paso 2: Verificar que la API responde

Abre en tu navegador:
```
http://localhost:8001/api/libros
```

O ejecuta en terminal:
```bash
curl http://localhost:8001/api/libros
```

### Paso 3: Ejecutar las pruebas en Postman

#### Ejecutar Prueba Individual:
1. Abre la colección **"API Libros - CRUD Completo"**
2. Click en cualquier petición
3. Click en **"Send"**
4. Verifica los tests en la pestaña **"Test Results"**

#### Ejecutar Toda la Colección:
1. Click derecho en la colección **"API Libros - CRUD Completo"**
2. Click en **"Run collection"**
3. Click en **"Run API Libros - CRUD Completo"**
4. Verás todas las pruebas ejecutándose secuencialmente
5. Al final verás un resumen: ✅ Passed y ❌ Failed

---

## 📋 Pruebas Incluidas en la Colección

| # | Prueba | Método | Endpoint | Código Esperado |
|---|--------|--------|----------|-----------------|
| 1 | Crear Libro - Caso Exitoso | POST | `/api/libros` | 201 Created |
| 2 | Crear Segundo Libro | POST | `/api/libros` | 201 Created |
| 3 | Crear Tercer Libro | POST | `/api/libros` | 201 Created |
| 4 | Listar Todos los Libros | GET | `/api/libros` | 200 OK |
| 5 | Buscar Libro por ID | GET | `/api/libros/1` | 200 OK |
| 6 | Buscar Libro Inexistente | GET | `/api/libros/999` | 404 Not Found |
| 7 | Actualizar Libro | PUT | `/api/libros/1` | 201 Created |
| 8 | Actualizar Libro Inexistente | PUT | `/api/libros/999` | 404 Not Found |
| 9 | Verificar Listado Actualizado | GET | `/api/libros` | 200 OK |
| 10 | Eliminar Libro | DELETE | `/api/libros/2` | 204 No Content |
| 11 | Eliminar Libro Inexistente | DELETE | `/api/libros/999` | 404 Not Found |
| 12 | Verificar Listado Final | GET | `/api/libros` | 200 OK |

---

## ✅ Validaciones Automáticas

Cada prueba incluye validaciones automáticas (tests) que verifican:

- ✅ Código de estado HTTP correcto
- ✅ Estructura de la respuesta JSON
- ✅ Presencia de campos requeridos
- ✅ Valores esperados en los datos
- ✅ Manejo correcto de errores

---

## 🔧 Troubleshooting

### Error: "Could not get response"

**Problema**: La API no está corriendo o no es accesible.

**Solución**:
```bash
# Verificar que el contenedor esté activo
docker ps | grep api-libros

# Ver logs de la API
docker logs api-libros-container

# Reiniciar el contenedor si es necesario
docker restart api-libros-container
```

### Error: 404 Not Found en todas las pruebas

**Problema**: Puerto o URL incorrecta.

**Solución**: Verifica que la API esté en `http://localhost:8001`

### Error: Connection Refused

**Problema**: MySQL no está corriendo o la API no puede conectarse.

**Solución**:
```bash
# Verificar MySQL
docker ps | grep mysql

# Ver logs de MySQL
docker logs mysql-sisdb2025

# Reiniciar ambos contenedores en orden
docker restart mysql-sisdb2025
sleep 10
docker restart api-libros-container
```

---

## 📊 Ejemplo de Resultado Esperado

Cuando ejecutes toda la colección, deberías ver algo como:

```
✅ 1. Crear Libro - Caso Exitoso
   ✓ Status code es 201 Created
   ✓ Respuesta contiene id
   ✓ Libro creado correctamente

✅ 2. Crear Segundo Libro
   ✓ Status code es 201

... (más pruebas)

✅ 12. Verificar Listado Final
   ✓ Status code es 200
   ✓ Libros restantes después de eliminación

Summary:
Total: 12
Passed: 12 ✅
Failed: 0
```

---

## 💡 Tips

1. **Orden de Ejecución**: Las pruebas están diseñadas para ejecutarse en orden secuencial.

2. **Base de Datos Limpia**: Si quieres empezar desde cero:
   ```bash
   docker stop mysql-sisdb2025 api-libros-container
   docker rm mysql-sisdb2025 api-libros-container
   # Luego vuelve a levantarlos
   ```

3. **Modificar Variables**: Puedes cambiar la URL base editando la variable `base_url` en la colección.

4. **Exportar Resultados**: En el Runner de Postman, puedes exportar los resultados de las pruebas.

---

¡Listo para probar tu API! 🚀
