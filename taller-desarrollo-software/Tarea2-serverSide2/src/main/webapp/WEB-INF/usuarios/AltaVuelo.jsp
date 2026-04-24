<%@page import="logica.RutaDeVuelo"%>
<%@page import="java.util.List"%>
<%@page import="logica.RutaDeVuelo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Volando.uy - Alta de Vuelo</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Enlace a tu archivo estilos.css -->
    <link rel="stylesheet" href="../css/estilos.css">
    
    <script>
        function validarFormulario() {
            const duracion = document.getElementById("duracion").value;
            const asientosTurista = document.getElementById("asientosTurista").value;
            const asientosEjecutivo = document.getElementById("asientosEjecutivo").value;
            const fechaVuelo = new Date(document.getElementById("fecha").value);
            const fechaActual = new Date();

            // Comparar solo las fechas (ignorar la hora)
            fechaActual.setHours(0, 0, 0, 0); // Establecer hora a 00:00:00
            fechaVuelo.setHours(0, 0, 0, 0); // Establecer hora a 00:00:00

            if (duracion <= 0 || asientosTurista <= 0 || asientosEjecutivo <= 0) {
                alert("Por favor, ingrese números mayores que 0 en todos los campos numéricos.");
                return false; // Evita el envío del formulario
            }

            if (fechaVuelo < fechaActual) {
                alert("La fecha del vuelo no puede ser anterior a la fecha actual.");
                return false; // Evita el envío del formulario
            }

            return true; // Permite el envío del formulario
        }
    </script>
</head>
<body>
    <!-- Barra de navegación -->
    <jsp:include page="/WEB-INF/template/barrasDeNavegacion/barraAerolinea.jsp"/>
	
    <!-- Contenedor del formulario -->
    <div class="container mt-5">
        <h2 class="text-center mb-4">Alta de Vuelo</h2>

        <!-- Mostrar mensaje de error si existe -->
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

        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="form-container p-4">
                    <!-- Formulario de alta de vuelo -->
                    <form action="AltaVuelo" method="post" enctype="multipart/form-data" onsubmit="return validarFormulario()">
                        
                        <!-- Lista de rutas de vuelo existentes -->
                        <!-- Verificar si hay rutas disponibles -->
                        <%
                            List<RutaDeVuelo> rutas = (List<RutaDeVuelo>) request.getAttribute("rutasUsuario");
                            if (rutas == null || rutas.isEmpty()) {
                        %>
                            <!-- Mostrar un mensaje si no hay rutas disponibles -->
                            <div class="alert alert-warning" role="alert">
                                No hay rutas de vuelo disponibles en este momento.
                            </div>
                        <%
                            } else {
                        %>
                            <!-- Lista de rutas de vuelo existentes -->
                            <div class="mb-3">
                                <label for="ruta" class="form-label">Ruta de Vuelo</label>
                                <select class="form-select" id="ruta" name="ruta" required>
                                    <option value="">Seleccione una ruta</option>
                                    <!-- Iterar sobre las rutas pasadas desde el backend -->
                                    <%
                                        for (RutaDeVuelo ruta : rutas) {
                                    %>
                                        <option value="<%= ruta.getName() %>">
                                            <%= ruta.getName() %> 
                                        </option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                        <%
                            }
                        %>
                        <!-- Nombre del vuelo -->
                        <div class="mb-3">
                            <label for="nombreVuelo" class="form-label">Nombre del Vuelo</label>
                            <input type="text" class="form-control" id="nombreVuelo" name="nombreVuelo" required>
                        </div>

                        <!-- Fecha del vuelo -->
                        <div class="mb-3">
                            <label for="fecha" class="form-label">Fecha del Vuelo</label>
                            <input type="date" class="form-control" id="fecha" name="fecha" required>
                        </div>

                        <!-- Duración del vuelo -->
                        <div class="mb-3">
                            <label for="duracion" class="form-label">Duración (en horas)</label>
                            <input type="number" class="form-control" id="duracion" name="duracion" required min="1" step="1">
                        </div>

                        <!-- Asientos -->
                        <div class="mb-3">
                            <label for="asientosTurista" class="form-label">Cantidad de Asientos Clase Turista</label>
                            <input type="number" class="form-control" id="asientosTurista" name="asientosTurista" required min="1" step="1">
                        </div>

                        <div class="mb-3">
                            <label for="asientosEjecutivo" class="form-label">Cantidad de Asientos Clase Ejecutivo</label>
                            <input type="number" class="form-control" id="asientosEjecutivo" name="asientosEjecutivo" required min="1" step="1">
                        </div>

                        <!-- Campo opcional para cargar imagen -->
                        <div class="form-group mb-3">
                            <label for="imagen" class="form-label">Imagen del Vuelo (opcional)</label>
                            <input type="file" class="form-control" id="imagen" name="imagen" accept="image/*">
                        </div>

                        <!-- Botones de acción -->
                        <div class="text-center">
                            <button type="submit" class="btn btn-primary me-2">Registrar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
