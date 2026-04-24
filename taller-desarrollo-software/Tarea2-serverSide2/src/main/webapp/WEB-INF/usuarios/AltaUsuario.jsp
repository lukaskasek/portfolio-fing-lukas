<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>Volando.uy - Alta de Usuario</title>
    <!-- Enlace a Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/media/styles/estilos.css">

    <!-- Script para mostrar campos según el tipo de usuario -->
    <script>
        function toggleFields() {
            var userType = document.getElementById('userType').value;
            var clientFields = document.getElementById('clientFields');
            var airlineFields = document.getElementById('airlineFields');

            if (userType === 'cliente') {
                clientFields.style.display = 'block';
                airlineFields.style.display = 'none';
            } else if (userType === 'aerolinea') {
                clientFields.style.display = 'none';
                airlineFields.style.display = 'block';
            } else {
                clientFields.style.display = 'none';
                airlineFields.style.display = 'none';
            }
        }
       
        function validarFormulario(event) {
            // Evitar el envío del formulario inicialmente
            event.preventDefault();

            // Obtener valores de los campos generales
            var nickName = document.getElementById("nickName").value;
            var nombre = document.getElementById("nombre").value;
            var correo = document.getElementById("correo").value;
            var contrasena = document.getElementById("contraseña").value;
            var confirmacion = document.getElementById("confirmacion").value;
            var imagen = document.getElementById("imagen").value;
            var userType = document.getElementById("userType").value;

            // Validación de los campos generales
            if (nickName === "" || nombre === "" || correo === "" || contrasena === "" || confirmacion === "") {
                alert("Por favor, completa todos los campos obligatorios.");
                return false;
            }

            // Validación de contraseñas iguales
            if (contrasena !== confirmacion) {
                alert("Las contraseñas no coinciden.");
                return false;
            }

            // Validación de formato de imagen (opcional)
            if (imagen && !(/\.(jpg|jpeg|png|gif)$/i).test(imagen)) {
                alert("Solo se permiten imágenes con formato JPG, JPEG, PNG o GIF.");
                return false;
            }

            // Validaciones adicionales basadas en el tipo de usuario
            if (userType === "cliente") {
                // Obtener valores del cliente
                var apellido = document.getElementById("apellido").value;
                var fechaNacimiento = document.getElementById("fechaNacimiento").value;
                var nacionalidad = document.getElementById("nacionalidad").value;
                var tipoDocumento = document.getElementById("tipoDocumento").value;
                var numDocumento = document.getElementById("numDocumento").value;

                // Validación de los campos obligatorios para cliente
                if (apellido === "" || fechaNacimiento === "" || nacionalidad === "" || tipoDocumento === "" || numDocumento === "") {
                    alert("Por favor, completa todos los campos obligatorios del cliente.");
                    return false;
                }
            } else if (userType === "aerolinea") {
                // Obtener valores de la aerolínea
                var descripcion = document.getElementById("descripcion").value;

                // Validación del campo obligatorio para aerolínea
                if (descripcion === "") {
                    alert("Por favor, completa el campo de descripción de la aerolínea.");
                    return false;
                }
            } else {
                // Validación en caso de que no se haya seleccionado un tipo de usuario
                alert("Por favor, selecciona un tipo de usuario.");
                return false;
            }

            // Si todo es válido, enviar el formulario
            document.getElementById("registroUsuario").submit();
        }
    </script>
</head>
<body>
<!-- Navbar solo con el nombre de la página -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/home">Volando.uy</a>
    </nav>

    <!-- Contenido principal -->
    <div class="container mt-5">
        <h2 class="text-center">Crear Usuario</h2>
        <div class="form-container mt-4 p-4 bg-white rounded shadow">
			<%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div class="alert alert-danger" role="alert">
                <%= error %>
            </div>
        <%
            }
        %>
            <form action="registroUsuario" id="registroUsuario" onsubmit="return validarFormulario(event)" method="POST" enctype="multipart/form-data">
                <!-- NickName -->
                <div class="form-group">
                    <label for="nickName">NickName:</label>
                    <input type="text" class="form-control" id="nickName" name="nickName" required>
                </div>

                <!-- Nombre -->
                <div class="form-group">
                    <label for="nombre">Nombre:</label>
                    <input type="text" class="form-control" id="nombre" name="nombre" required>
                </div>

                <!-- Tipo de Usuario -->
                <div class="form-group">
                    <label for="userType">Tipo de Usuario:</label>
                    <select id="userType" name="userType" class="form-control" onchange="toggleFields()" required>
                        <option value="">Selecciona un tipo</option>
                        <option value="cliente">Cliente</option>
                        <option value="aerolinea">Aerolínea</option>
                    </select>
                </div>

                <!-- Campos Cliente -->
                <div id="clientFields" style="display:none;">
                    <div class="form-group">
                        <label for="apellido">Apellido:</label>
                        <input type="text" class="form-control" id="apellido" name="apellido" >
                    </div>
                    <div class="form-group">
                        <label for="fechaNacimiento">Fecha de Nacimiento:</label>
                        <input type="date" class="form-control" id="fechaNacimiento" name="fechaNacimiento" >
                    </div>
                    <div class="form-group">
                        <label for="nacionalidad">Nacionalidad:</label>
                        <input type="text" class="form-control" id="nacionalidad" name="nacionalidad"  >
                    </div>
                    <div class="form-group">
                        <label for="tipoDocumento">Tipo de Documento:</label>
                        <input type="text" class="form-control" id="tipoDocumento" name="tipoDocumento" >
                    </div>
                    <div class="form-group">
                        <label for="tipoDocumento">Número de Documento:</label>
                        <input type="text" class="form-control" id="numDocumento" name="numDocumento" >
                    </div>
                </div>

                <!-- Campos Aerolínea -->
                <div id="airlineFields" style="display:none;">
                    <div class="form-group">
                        <label for="descripcion">Descripción:</label>
                        <input type="text" class="form-control" id="descripcion" name="descripcion" >
                    </div>
                    <div class="form-group">
                        <label for="link">Link:(opcional)</label>
                        <input type="url" class="form-control" id="link" name="link">
                    </div>
                </div>

                <!-- Contraseña -->
                <div class="form-group">
                    <label for="contraseña">Contraseña:</label>
                    <input type="password" class="form-control" id="contraseña" name="contraseña" required>
                </div>

                <!-- Confirmar Contraseña -->
                <div class="form-group">
                    <label for="confirmacion">Confirmar Contraseña:</label>
                    <input type="password" class="form-control" id="confirmacion" name="confirmacion" required>
                </div>

                <!-- Correo -->
                <div class="form-group">
                    <label for="correo">Correo:</label>
                    <input type="email" class="form-control" id="correo" name="correo" required>
                </div>

                <!--Imagen (opcional)-->
                <div class="form-group">
                    <label for = "fotoPerfil">Foto de Perfil(opcional)</label>
                    <!--<input type = "image" class="form-control" id="fotoPerfil">-->
                    <input type="file" id="imagen" name="imagen" accept="image/*">
                </div>
                <!-- Botón Registrar -->
                <div class="form-group text-center">
                    <button type="submit" class="btn btn-primary" >Registrar</button>
                </div>
            </form>

        </div>
    </div>

    <script src="../js/scripts.js"></script>
    <!-- Enlaces a JavaScript de Bootstrap -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>