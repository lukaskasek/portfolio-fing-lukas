<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="logica.DTUser" %>
<%@ page import="logica.DTRutaDeVuelo" %>
<%@ page import="logica.DTVuelo" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Volando.uy - Reserva de Vuelo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/media/styles/styles.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <!-- Navbar Cliente -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/homeCliente">Volando.uy</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/cerrarSesion" id="logout-btn">Cerrar Sesión</a>
                </li>
                <li class="nav-item d-flex align-items-center">
                    <% 
                        DTUser usuario = (DTUser) request.getSession().getAttribute("user");
                        if (usuario != null) {
                    %>
                        <img src="<%= usuario.getFoto() != null ? usuario.getFoto() : request.getContextPath() + "/media/images/default.webp" %>" class="rounded-circle" style="width: 30px; height: 30px; margin-right: 5px;">
                        <a class="nav-link" href="${pageContext.request.contextPath}/perfil?nick=<%= usuario.getNick() %>" id="perfil-btn"><%= usuario.getNick() %></a>
                    <% } %>
                </li>
            </ul>
        </div>
    </nav>

    <!-- Contenido principal -->
    <div class="container mt-5">
        <h2 class="text-center">Reserva de Vuelo</h2>

        <!-- Formulario de reserva -->
        <form action="${pageContext.request.contextPath}/reservarVuelo" method="GET" class="mt-4">
            <!-- Selección de Aerolínea -->
            <div class="form-group">
                <label for="aerolinea">Seleccione Aerolínea:</label>
                <select id="aerolinea" name="aerolinea" class="form-control" onchange="this.form.submit()" required>
                    <option value="">Seleccione una aerolínea</option>
                    <%
                        String aerolineaSeleccionada = request.getParameter("aerolinea");
                        List<String> aerolineas = (List<String>) request.getAttribute("aerolineas");
                        if (aerolineas != null) {
                            for (String aerolinea : aerolineas) {
                                String selected = (aerolinea.equals(aerolineaSeleccionada)) ? "selected" : "";
                    %>
                        <option value="<%= aerolinea %>" <%= selected %>><%= aerolinea %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>

            <!-- Selección de Ruta de Vuelo -->
            <div class="form-group">
                <label for="rutaVuelo">Seleccione Ruta de Vuelo:</label>
                <select id="rutaVuelo" name="rutaVuelo" class="form-control" onchange="this.form.submit()" required>
                    <option value="">Seleccione una ruta de vuelo</option>
                    <%
                        String rutaSeleccionada = request.getParameter("rutaVuelo");
                        List<String> rutas = (List<String>) request.getAttribute("rutas");
                        if (rutas != null) {
                            for (String ruta : rutas) {
                                String selectedRuta = (ruta.equals(rutaSeleccionada)) ? "selected" : "";
                    %>
                        <option value="<%= ruta %>" <%= selectedRuta %>><%= ruta %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>

<!-- Selección de Vuelo -->
			<div class="form-group">
			    <label for="vuelo">Seleccione Vuelo:</label>
			    <select id="vuelo" name="vuelo" class="form-control" required>
			        <option value="">Seleccione un vuelo</option>
			        <%
			            String vueloSeleccionado = request.getParameter("vuelo");
			            List<String> vuelos = (List<String>) request.getAttribute("vuelos");
			            if (vuelos != null && !vuelos.isEmpty()) {
			                for (String vuelo : vuelos) {
			                    String selectedVuelo = (vuelo.equals(vueloSeleccionado)) ? "selected" : "";
			        %>
			                    <option value="<%= vuelo %>" <%= selectedVuelo %>><%= vuelo %></option>
			        <%
			                }
			            } else {
			        %>
			                <option value="">No hay vuelos disponibles para esta ruta</option>
			        <%
			            }
			        %>
			    </select>
			</div>



            <!-- Otros campos de la reserva (ej. tipo de asiento, cantidad de pasajeros, etc.) -->
            <!-- Tipo de Asiento -->
            <div class="form-group">
                <label for="tipoAsiento">Seleccione Tipo de Asiento:</label>
                <select id="tipoAsiento" name="tipoAsiento" class="form-control" required>
                    <option value="Turista">Turista</option>
                    <option value="Ejecutivo">Ejecutivo</option>
                </select>
            </div>

            <!-- Fecha de Reserva -->
            <div class="form-group">
                <label for="fechaReserva">Fecha de Reserva:</label>
                <input type="date" id="fechaReserva" name="fechaReserva" class="form-control" required>
            </div>

            <!-- Cantidad de Pasajeros -->
            <div class="form-group">
                <label for="cantidadPasajes">Cantidad de Pasajes:</label>
                <input type="number" id="cantidadPasajes" name="cantidadPasajes" class="form-control" min="1" required>
            </div>

            <!-- Equipaje Extra -->
            <div class="form-group">
                <label for="equipajeExtra">Unidades de Equipaje Extra:</label>
                <input type="number" id="equipajeExtra" name="equipajeExtra" class="form-control" min="0" required>
            </div>

            <!-- Botón de Reservar -->
            <div class="form-group text-center">
                <button type="submit" class="btn btn-primary">Reservar Vuelo</button>
            </div>
        </form>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

