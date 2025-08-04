# Sistema de Login - Clínica Dental

## Descripción
Se ha implementado un sistema de autenticación para la aplicación de gestión de la Clínica Dental.

## Características Implementadas

### 1. Pantalla de Login (`LoginView.java`)
- Interfaz moderna y elegante que coincide con el diseño de la aplicación
- Campos para usuario y contraseña
- Botones de "Iniciar Sesión" y "Cancelar"
- Soporte para navegación con teclado (Enter)

### 2. Controlador de Login (`LoginController.java`)
- Validación de campos vacíos
- Autenticación de credenciales
- Navegación automática al sistema principal tras login exitoso
- Manejo de errores con mensajes informativos

### 3. Funcionalidad de Logout
- Botón "Cerrar Sesión" en el panel lateral
- Confirmación antes de cerrar la aplicación
- Integrado en el `PrincipalController`

### 4. Lanzador Principal (`MainApp.java`)
- Punto de entrada que inicia con la pantalla de login
- Reemplaza el lanzamiento directo del `PrincipalView`

## Credenciales por Defecto
- **Usuario:** `admin`
- **Contraseña:** `admin123`

## Estructura de Archivos

```
src/
├── view/
│   ├── LoginView.java          # Pantalla de login
│   └── PrincipalView.java      # Modificado con botón de logout
├── controller/
│   ├── LoginController.java    # Controlador del login
│   └── PrincipalController.java # Modificado con funcionalidad de logout
├── model/
│   └── UserModel.java          # Modelo de usuario para futura integración con BD
└── MainApp.java               # Nuevo punto de entrada de la aplicación
```

## Cómo Usar

1. **Ejecutar la aplicación:**
   ```bash
   java -cp "lib/*:src" MainApp
   ```

2. **Iniciar sesión:**
   - Ingresar usuario: `admin`
   - Ingresar contraseña: `admin123`
   - Presionar "Iniciar Sesión" o Enter

3. **Cerrar sesión:**
   - Hacer clic en "Cerrar Sesión" en el panel lateral
   - Confirmar la acción

## Características Técnicas

- **Diseño Responsivo:** Interfaz que se adapta al contenido
- **Validación de Entrada:** Verificación de campos obligatorios
- **Navegación por Teclado:** Soporte completo para uso con teclado
- **Mensajes de Error:** Feedback claro al usuario
- **Integración Seamless:** Transición suave entre login y aplicación principal

## Futuras Mejoras

1. **Integración con Base de Datos:**
   - Almacenamiento seguro de usuarios
   - Encriptación de contraseñas
   - Roles y permisos

2. **Funcionalidades Adicionales:**
   - Recuperación de contraseña
   - Recordar sesión
   - Múltiples usuarios
   - Auditoría de login

3. **Seguridad:**
   - Límite de intentos de login
   - Bloqueo temporal de cuenta
   - Logs de actividad

## Rama Git
Este código está implementado en la rama `feature/login` para mantener el código principal limpio hasta que se complete la integración. 