<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Volando.uy - Visitante</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/media/styles/styles.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="home">Volando.uy</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <!-- Barra de búsqueda centrada -->
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
                    <a class="nav-link" href="${pageContext.request.contextPath}/usuarios">Usuarios</a>
                </li>
                <!-- Opción común para visitantes -->
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/loginUsuario">Login</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link btn btn-primary text-white" href="${pageContext.request.contextPath}/registroUsuario">Registrarse</a>
                </li>
            </ul>
        </div>
    </nav>

<div class="container mt-2">
    <div class="row">
        <jsp:include page="/WEB-INF/template/sideBars/sideBarVisitante.jsp"/>
        
        <!-- Contenido Principal -->
        <div class="col-md-8">
            <div class="row">
                <% List<List<String>> rutasVuelo = (List<List<String>>) request.getAttribute("rutasConfirmadas");
                if (rutasVuelo != null && !rutasVuelo.isEmpty()) { %>
                    <% for (List<String> ruta : rutasVuelo) { 
                        String imagenRuta = ruta.get(15);
                        String foto = "media/images/defaultRuta.webp";
                        if (!imagenRuta.equalsIgnoreCase("sinImagen")) {
                            foto = imagenRuta;
                            if (!foto.contains("uploads")) {
                                foto = "media/images/" + imagenRuta;
                            }
                        }
                    %>
                    <div class="col-md-6 mb-3">
                        <div class="d-flex align-items-center">
                            <div class="card">
                                <img src="${pageContext.request.contextPath}/<%= foto %>" alt="Ruta de Vuelo" class="card-img-top" style="height: 150px; object-fit: cover;">
                                <div class="card-body">
                                    <h5 class="card-title">
                                        <a href="rutaDeVuelo?route=<%= ruta.get(0) %>" class="route-link"><%= ruta.get(0) %></a>
                                    </h5>
                                    <p class="card-text"><%= ruta.get(2) %></p>
                                    <p class="card-text"><small><%= ruta.get(7) %> -> <%= ruta.get(8) %></small></p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <% } %>
                <% } else { %>
                    <div class="col-md-12">
                        <p>No se encontraron rutas de vuelo.</p>
                    </div>
                <% } %>
            </div>
        </div>
    </div>
</div>


    <!-- Scripts -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
