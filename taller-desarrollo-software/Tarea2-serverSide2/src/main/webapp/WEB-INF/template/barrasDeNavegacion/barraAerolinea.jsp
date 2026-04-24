<%@page import="logica.DTUser"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Volando.uy - barraAerolínea</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/media/styles/styles.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script type="text/javascript">
    window.onload = function(){
        const urlParams = new URLSearchParams(window.location.search);
        const fromMenu = urlParams.get('from');
        if(fromMenu){
        	if(fromMenu === 'barra'){
                mostrarInfoPrivA();
            }
        }
        
    };
    function mostrarInfoPrivA(){
    	const contenedor = document.getElementById('info-privada');
        if(contenedor){
                contenedor.style.display = 'block';

        }
    };
        function confirmarCerrarSesion() {
            var confirmacion = confirm("¿Estás seguro de que deseas cerrar la sesión?");
            if (confirmacion) {
                // Redirigir al servlet que maneja la acción de cerrar sesión
                window.location.href = "${pageContext.request.contextPath}/cerrarSesion";
            }
        }
    </script>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="homeAerolinea">Volando.uy</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
    <!-- Barra de búsqueda centrada y alineada horizontalmente -->
    <div class="container-search mx-auto">
        <div class="search-row d-flex justify-content-center">
            <div class="search-input">
                <input type="text" placeholder="Origen, Destino, Aerolínea, Paquete">
            </div>
            <button class="btn btn-primary">Buscar</button>
        </div>
    </div>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ml-auto">
                
                <li class="nav-item">
                    <a class="nav-link" href="#" onclick="confirmarCerrarSesion()">Cerrar Sesión</a>
                </li>
                <li class="nav-item d-flex align-items-center">
                    <!-- Nombre del usuario desde la sesión -->
                    <%
                    DTUser usuario = (DTUser) request.getSession().getAttribute("user");
                    if (usuario != null) {
                    %>
                    <img src="media/images/<%=usuario.getFoto() %>" class="rounded-circle" style="width: 30px; height: 30px; margin-right: 5px;">
                   <a class="nav-link" href="${pageContext.request.contextPath}/perfil?nick=<%= usuario.getNick() %>&from=barra" id="perfil-btn"><%= usuario.getNick() %></a>
                    <%
                    } else {
                    %>
                    <a class="nav-link" href="#" id="perfil-btn">Usuario no logueado</a>
                    <%
                    }
                    %>
                </li>
            </ul>
        </div>
    </nav>
    
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>