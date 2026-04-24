<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Volando.uy - Inicio de Sesión</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="media/styles/estilos.css">
</head>
<body>

    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/home">Volando.uy</a>
    </nav>

    <!-- Contenedor principal -->
    <div class="container mt-5">
        <h2 class="text-center">Iniciar Sesión</h2>
        <div class="form-container mt-4 p-4 bg-white rounded shadow">

            <!-- Mostrar mensajes de error si existen -->
            <%
                String error = (String) request.getAttribute("error");
                if (error != null) {
            %>
                <div class="alert alert-danger text-center">
                    <%= error %>
                </div>
            <%
                }
            %>

            <!-- Formulario de inicio de sesión -->
            <form id="loginForm" action="loginUsuario" method="POST">
                <div class="form-group">
                    <label for="nickname">Nickname o Email:</label>
                    <input type="text" class="form-control" id="nickname" name="nickname" required>
                </div>

                <div class="form-group">
                    <label for="password">Contraseña:</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>

                <div class="form-group text-center">
                    <button type="submit" class="btn btn-primary">Iniciar Sesión</button>
                </div>
            </form>
        </div>
    </div>

</body>
</html>
