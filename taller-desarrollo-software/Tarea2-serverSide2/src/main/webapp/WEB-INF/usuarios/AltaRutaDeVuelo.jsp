<%@page import="logica.DTUser"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Volando.uy - Alta Ruta de Vuelo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/media/styles/styles.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
    

    <script type="text/javascript">
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
	<!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/homeAerolinea">Volando.uy</a>
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
                   <img src="${pageContext.request.contextPath}/media/images/imagenuser.jpg" class="rounded-circle" style="width: 30px; height: 30px; margin-right: 5px;">
                    <!-- Nombre del usuario desde la sesión -->
                    <%
                    DTUser usuario = (DTUser) request.getSession().getAttribute("user");
                    if (usuario != null) {
                    %>
                   <a class="nav-link" href="${pageContext.request.contextPath}/perfil?nick=<%= usuario.getNick() %>" id="perfil-btn"><%= usuario.getNick() %></a>
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

    <!-- Navbar y otros elementos... -->

    <div class="container mt-5">
        <h2 class="text-center mb-4">Alta de Ruta de Vuelo</h2>
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="form-container p-4">
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
                    <form id="altaRutaVueloForm" method="post" enctype="multipart/form-data">
                        <div class="form-group">
                            <label for="nombre">Nombre:</label>
                            <input type="text" class="form-control" id="nombre" name="nombre" required>
                        </div>

                        <div class="form-group">
                            <label for="descripcionCorta">Descripción Corta:</label>
                            <input type="text" class="form-control" id="descripcionCorta" name="descripcionCorta" required>
                        </div>

                        <div class="form-group">
                            <label for="descripcion">Descripción:</label>
                            <textarea class="form-control" id="descripcion" name="descripcion" rows="3" required></textarea>
                        </div>

                        <div class="form-group">
                            <label for="hora">Hora:</label>
                            <input type="time" class="form-control" id="hora" name="hora" required>
                        </div>

                        <div class="form-group">
                            <label for="costoBaseTurista">Costo Base Turista:</label>
                            <input type="number" class="form-control" id="costoBaseTurista" name="costoBaseTurista" required>
                        </div>

                        <div class="form-group">
                            <label for="costoBaseEjecutivo">Costo Base Ejecutivo:</label>
                            <input type="number" class="form-control" id="costoBaseEjecutivo" name="costoBaseEjecutivo" required>
                        </div>

                        <div class="form-group">
                            <label for="costoEquipajeExtra">Costo Equipaje Extra:</label>
                            <input type="number" class="form-control" id="costoEquipajeExtra" name="costoEquipajeExtra" required>
                        </div>

                        <div class="form-group">
                            <label for="ciudadOrigen">Ciudad de Origen:</label>
                            <select class="form-control" id="ciudadOrigen" name="ciudadOrigen" required>
                                <option value="">Seleccione una ciudad</option>
                                <% 
                                List<String> ciudades = (List<String>) request.getAttribute("ciudades");
                                if (ciudades != null) {
                                    for (String ciudad : ciudades) {
                                %>
                                    <option value="<%= ciudad %>"><%= ciudad %></option>
                                <%
                                    }
                                }
                                %>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="ciudadDestino">Ciudad de Destino:</label>
                            <select class="form-control" id="ciudadDestino" name="ciudadDestino" required>
                                <option value="">Seleccione una ciudad</option>
                                <% 
                                if (ciudades != null) {
                                    for (String ciudad : ciudades) {
                                %>
                                    <option value="<%= ciudad %>"><%= ciudad %></option>
                                <%
                                    }
                                }
                                %>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="fechaAlta">Fecha de Alta:</label>
                            <input type="date" class="form-control" id="fechaAlta" name="fechaAlta" required>
                        </div>

                        <div class="form-group">
                            <label for="categorias">Categorías:</label>
                            <select multiple class="form-control" id="categorias" name="categorias" size="5">
                                <% 
                                List<String> categorias = (List<String>) request.getAttribute("categorias");
                                if (categorias != null) {
                                    for (String categoria : categorias) {
                                %>
                                    <option value="<%= categoria %>"><%= categoria %></option>
                                <%
                                    }
                                }
                                %>
                            </select>
                        </div>
					 <div class="form-group">
                    <label for = "fotoPerfil">Foto de Ruta(opcional)</label>
                    <!--<input type = "image" class="form-control" id="fotoPerfil">-->
                    <input type="file" id="imagen" name="imagen" accept="image/*">
                </div>
					
                        <div class="text-center">
                            <button type="submit" class="btn btn-primary">Registrar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
      
    <!-- Modal (Aviso emergente) -->
<div class="modal fade" id="errorModal" tabindex="-1" role="dialog" aria-labelledby="errorModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="errorModalLabel">Error</h5>
            </div>
            <div class="modal-body">
                <p>${errorMessage}</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">Aceptar</button>
            </div>
        </div>
    </div>
</div>

<script>
    // Mostrar el modal si hay un mensaje de error
    $(document).ready(function() {
        var errorMessage = "${errorMessage}";
        if (errorMessage !== "") {
            $('#errorModal').modal('show');
        }
    });
</script>
    

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>