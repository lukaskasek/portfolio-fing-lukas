<%@page import="logica.DTUser"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Volando.uy - Cliente</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/media/styles/styles.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script type="text/javascript">
        function confirmarCerrarSesion() {
            var confirmacion = confirm("¿Estás seguro de que deseas cerrar la sesión?");
            if (confirmacion) {
                window.location.href = "${pageContext.request.contextPath}/cerrarSesion";
            }
        }
    </script>
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="homeCliente">Volando.uy</a>
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
                    <% } %>
                </li>
            </ul>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="container mt-2">
        <div class="row">
            <!-- Sidebar para Categorías -->
            <div class="col-md-2">
                <nav id="sidebar" class="p-2">
        <h5>Categorías</h5>
        <ul class="nav flex-column">
            <% 
            List<String> categorias = (List<String>)request.getAttribute("categorias");
            if(categorias != null) {
                for(String categoria : categorias) {
            %>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/rutaDeVuelo?categoria=<%=categoria%>"><%=categoria%></a>
                </li>
            <% 
                }
            }
            %>
        </ul>
    </nav>
                <!-- Gestión de Vuelos -->
                <nav id="gestion" class="gestion vuelos p-3 mt-4">
                    <h5>Mi Perfil</h5>
                    <ul class="nav flex-column">
                        <li class="nav-item">
						    <a class="nav-link" href="${pageContext.request.contextPath}/reservarVuelo">Reservar Vuelo</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">Comprar Paquete</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="ConsultaDeReservaDeVuelo.html?from=cli">Reservas</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/usuarios">Usuarios</a>
                        </li>
                    </ul>
                </nav>
            </div>

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
                            <!-- Card de cada vuelo -->
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
                        <p>No se encontraron rutas de vuelo.</p>
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
