# Guía: Cómo Obtener el Phone Number ID Correcto de WhatsApp Business

## 🚨 Error Actual
```
Object with ID '2041909506562032' does not exist, cannot be loaded due to missing permissions
```

**Significado**: El Phone Number ID que estás usando NO existe o el token no tiene permisos para acceder a él.

---

## 📋 Pasos para Obtener los Valores Correctos

### PASO 1: Acceder a Meta for Developers

1. Ve a: **https://developers.facebook.com/apps/**
2. Inicia sesión con tu cuenta de Facebook/Meta
3. Deberías ver una lista de tus aplicaciones

### PASO 2: Seleccionar tu Aplicación de WhatsApp Business

1. Busca la aplicación asociada a tu cuenta de WhatsApp Business
2. Si no tienes una app creada, necesitas crear una:
   - Click en **"Create App"** (Crear aplicación)
   - Selecciona tipo: **"Business"**
   - Agrega el producto **"WhatsApp"**

### PASO 3: Ir a la Configuración de WhatsApp

Una vez dentro de tu aplicación:

1. En el menú lateral izquierdo, busca **"WhatsApp"**
2. Click en **"API Setup"** o **"Getting Started"**
3. Deberías ver una pantalla con:
   - **"Temporary access token"** (Token temporal)
   - **"Phone number ID"** (ID del número de teléfono)
   - **"WhatsApp Business Account ID"** (ID de la cuenta)

### PASO 4: Copiar el Phone Number ID

```
⚠️ IMPORTANTE: NO es tu número de teléfono!
```

**Ejemplos:**

✅ **CORRECTO - Phone Number ID:**
```
109123456789012
```
Es un número largo (generalmente 15 dígitos)

❌ **INCORRECTO - Número de teléfono:**
```
+51 994 339 535
51994339535
```

### PASO 5: Copiar el Access Token

En la misma página, encontrarás:

**Token Temporal** (válido 24-48 horas):
- Click en **"Copy"** junto al token
- Úsalo solo para pruebas

**Token Permanente** (para producción):
1. Ve a **"System Users"** en Business Settings
2. Crea un System User
3. Genera un token permanente con permisos de WhatsApp

---

## 🧪 Probar las Credenciales

### Método 1: Usando cURL (Rápido)

```bash
# Reemplaza TU_PHONE_NUMBER_ID y TU_TOKEN con tus valores

# 1. Verificar que el Phone Number ID existe
curl -X GET "https://graph.facebook.com/v19.0/TU_PHONE_NUMBER_ID" \
  -H "Authorization: Bearer TU_TOKEN"

# Si funciona, verás algo como:
# {"verified_name":"Tu Negocio","display_phone_number":"+51 994 339 535", ...}

# 2. Enviar un mensaje de prueba con template "hello_world"
curl -X POST "https://graph.facebook.com/v19.0/TU_PHONE_NUMBER_ID/messages" \
  -H "Authorization: Bearer TU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "messaging_product": "whatsapp",
    "to": "51994339535",
    "type": "template",
    "template": {
      "name": "hello_world",
      "language": { "code": "en_US" }
    }
  }'
```

### Método 2: Usando la Clase Java de Prueba

1. Abre el archivo: `ManualWhatsAppTest.java`
2. Modifica las constantes al inicio:
   ```java
   String TOKEN = "TU_TOKEN_AQUI";
   String PHONE_NUMBER_ID = "TU_PHONE_NUMBER_ID_AQUI";
   String NUMERO_DESTINO = "51994339535";
   ```
3. Click derecho en el archivo > **Run 'ManualWhatsAppTest.main()'**
4. Observa el output en la consola

---

## 🔍 Verificar el Template

Tu código usa: `recordatorio_cuota_personal`

### Para verificar que existe y está aprobado:

1. En la misma página de WhatsApp API Setup
2. Ve a la pestaña **"Message Templates"**
3. Busca `recordatorio_cuota_personal`
4. Verifica:
   - ✅ Status: **"Approved"** (Aprobado)
   - ✅ Language: **"Spanish"** o **"es"**
   - ✅ Parámetros: **Exactamente 5 parámetros** en el body

### Estructura esperada del template:

```
Hola {{1}}, te recordamos que tienes pendiente la cuota #{{2}} por un monto de ${{3}}.
Fecha de vencimiento: {{4}}.
Saldo pendiente total: ${{5}}.
```

Si no tienes este template:
1. Click en **"Create Template"**
2. Crea uno con exactamente 5 parámetros
3. Espera la aprobación de Meta (puede tardar minutos u horas)

---

## 🎯 Solución Paso a Paso

### Opción A: Obtener Phone Number ID desde Meta Dashboard

```
1. developers.facebook.com/apps/
2. Tu App > WhatsApp > API Setup
3. Copiar "Phone number ID"
4. Actualizar application.properties
5. Reiniciar aplicación
```

### Opción B: Obtener Phone Number ID por API

Si tienes acceso al **WhatsApp Business Account ID**, puedes obtener todos tus phone number IDs:

```bash
curl -X GET "https://graph.facebook.com/v19.0/TU_WABA_ID/phone_numbers" \
  -H "Authorization: Bearer TU_TOKEN"
```

Response ejemplo:
```json
{
  "data": [
    {
      "verified_name": "Mi Negocio",
      "display_phone_number": "+51 994 339 535",
      "id": "109123456789012",  // <-- Este es el Phone Number ID
      "quality_rating": "GREEN"
    }
  ]
}
```

---

## 📝 Actualizar la Configuración

Una vez que tengas los valores correctos:

### 1. Editar `application.properties`:

```properties
# Token de acceso (temporal o permanente)
whatsapp.api.token=EAAxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

# Phone Number ID (NO el número de teléfono)
whatsapp.phone.number.id=109123456789012
```

### 2. Reiniciar la aplicación

```bash
# Detener la aplicación actual (Ctrl+C)
# Compilar y ejecutar de nuevo
mvn clean package
mvn spring-boot:run
```

---

## ⚠️ Problemas Comunes

| Error | Causa | Solución |
|-------|-------|----------|
| `Object with ID 'XXX' does not exist` | Phone Number ID incorrecto | Verificar en Meta Dashboard |
| `cannot be loaded due to missing permissions` | Token sin permisos | Generar token con permisos de WhatsApp |
| `Invalid OAuth access token` | Token expirado | Generar token nuevo |
| `template does not exist` | Template no creado/aprobado | Crear y esperar aprobación |

---

## ✅ Checklist Final

Antes de volver a intentar:

- [ ] Tengo el **Phone Number ID** correcto (no mi número de teléfono)
- [ ] El token es válido (probado con cURL o Postman)
- [ ] El token tiene permisos de **WhatsApp Business Management**
- [ ] El template `recordatorio_cuota_personal` está **aprobado**
- [ ] El template tiene exactamente **5 parámetros**
- [ ] He actualizado `application.properties`
- [ ] He reiniciado la aplicación

---

## 🆘 ¿Aún no funciona?

Ejecuta esto y comparte el output:

```bash
# Ver información de tu Phone Number ID
curl -X GET "https://graph.facebook.com/v19.0/2041909506562032" \
  -H "Authorization: Bearer TU_TOKEN"
```

Si sale error 400 con código 100, confirma que el Phone Number ID `2041909506562032` NO es el correcto.

