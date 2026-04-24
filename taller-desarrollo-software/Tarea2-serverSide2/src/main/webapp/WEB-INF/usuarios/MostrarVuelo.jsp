<%@page import="logica.DTPasaje"%>
<%@page import="logica.DTReserva"%>
<%@page import="logica.DTVuelo"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.util.List"%>
<%@page import="logica.EstadoSesion"%>
<%@page import="logica.DTCliente"%>
<%@page import="logica.DTAerolinea"%>
<%@page import="logica.DTUser"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalles del Vuelo</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="media/styles/styles.css">
</head>
<body>
    <%
    // Manejo del estado de sesión
    EstadoSesion estadoSesion = (EstadoSesion) request.getSession().getAttribute("estado_sesion");
    DTUser user = (DTUser) request.getSession().getAttribute("user");

    if (estadoSesion == null) {
        estadoSesion = EstadoSesion.NO_LOGIN; // Establecer un estado por defecto si no hay sesión
    }

    switch(estadoSesion) {
        case NO_LOGIN:
            %><jsp:include page="/WEB-INF/template/barrasDeNavegacion/barraVisitante.jsp"/><%
            break;
        case LOGIN_CORRECTO:
            if (user instanceof DTCliente) {
                %><jsp:include page="/WEB-INF/template/barrasDeNavegacion/barraCliente.jsp"/><%
            } else if (user instanceof DTAerolinea) {
                %><jsp:include page="/WEB-INF/template/barrasDeNavegacion/barraAerolinea.jsp"/><%
            }
            break;
        case LOGIN_INCORRECTO:
            // Mostrar barra de navegación para login incorrecto si es necesario
            break;
    }
    %>

    <div class="row">
        <%
        // Manejo de las sidebars según el tipo de usuario
        switch(estadoSesion) {
            case NO_LOGIN:
            	%>
                <jsp:include page="/sideBarVisitante"/>
        <%                break;
            case LOGIN_CORRECTO:
                if (user instanceof DTCliente) {
                	%>
                    <jsp:include page="/sideBarCliente"/>
        <%                } else if (user instanceof DTAerolinea) {
        	%>
            <jsp:include page="/sideBarAerolinea"/>
<%                }
                break;
            case LOGIN_INCORRECTO:
                // Incluir sideBar para login incorrecto si es necesario
                break;
        }
        %>
        
	<div class="col-md-10">
        <div class="container mt-5">
        	<div class="row justify-content-center">
        	
        <%
        // Obtener el vuelo desde los atributos de la solicitud
        DTVuelo vuelo = (DTVuelo) request.getAttribute("info"); 

        if (vuelo != null) {
            // Formateadores para las fechas
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Mostrar detalles generales del vuelo
        %>
            <h1>Detalles del vuelo: <%= vuelo.getNombre() %></h1>
            
            <!-- Bloque para mostrar la imagen del vuelo si existe -->
            <div class="flight-image">
                <%
                String imagen = vuelo.getImagen();
                if (imagen != null && !imagen.isEmpty()) {
                %>
                    <img src="media/images/<%= imagen %>" alt="Imagen del vuelo <%= vuelo.getNombre() %>" class="img-fluid rounded" style="width: 262px; height: 175px; object-fit: cover;">
                <%
                } else {
                %>
                    <p>No hay imagen disponible para este vuelo.</p>
                <%
                }
                %>
            </div>

            <div class="details">
                <p><strong>Fecha del vuelo:</strong> <%= vuelo.getFecha().format(dateFormatter) %></p>
                <p><strong>Fecha de alta:</strong> <%= vuelo.getFechaAlta().format(dateFormatter) %></p>
                <p><strong>Duración:</strong> <%= vuelo.getDuracion() %> horas</p>
                <p><strong>Asientos Turista:</strong> <%= vuelo.getAsientosTurista() %></p>
                <p><strong>Asientos Ejecutivo:</strong> <%= vuelo.getAsientosEjecutivo() %></p>
            </div>

            <%
            // Verificar si el usuario es una aerolínea que publicó el vuelo
            if (user instanceof DTAerolinea && ((DTAerolinea) user).getNick().equals(vuelo.getAerolinea().getNickName())) {
                // Mostrar todas las reservas si el usuario es la aerolínea
           // %>
                <h3 class="mt-4">Reservas Asociadas</h3>
                <%
                List<DTReserva> reservas = vuelo.getReservas();
                if (reservas != null && !reservas.isEmpty()) {
                %>
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Tipo de Asiento</th>
                                <th>Equipaje Extra</th>
                                <th>Costo</th>
                                <th>Fecha de Reserva</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                            int index = 1;
                            for (DTReserva reserva : reservas) {
                            %>
                                <tr>
                                    <td><%= index++ %></td>
                                    <td><%= reserva.getTipoAsiento() %></td>
                                    <td><%= reserva.getEquipajeExtra() %> </td>
                                    <td><%= reserva.getCosto() %></td>
                                    <td><%= reserva.getFecha().format(dateFormatter) %></td>
                                </tr>
                                <tr>
                                    <td colspan="5">
                                        <h5>Pasajeros</h5>
                                        <ul>
                                            <%
                                            List<DTPasaje> pasajes = reserva.getPasajes();
                                            if (pasajes != null && !pasajes.isEmpty()) {
                                                for (DTPasaje pasaje : pasajes) {
                                            %>
                                                    <li><%= pasaje.getNombre() %> <%= pasaje.getApellido() %></li>
                                            <%
                                                }
                                            } else {
                                            %>
                                                <li>No hay pasajeros asociados a esta reserva.</li>
                                            <%
                                            }
                                            %>
                                        </ul>
                                    </td>
                                </tr>
                            <%
                            }
                            %>
                        </tbody>
                    </table>
                <%
                } else {
                %>
                    <p>No hay reservas asociadas a este vuelo.</p>
                <%
                }
            // Si el usuario es un cliente, mostrar solo su reserva o permitir que reserve si no tiene una
            } else if (user instanceof DTCliente) {
                DTCliente cliente = (DTCliente) user;
                boolean reservaEncontrada = false;

                for (DTReserva reserva : vuelo.getReservas()) {
                    if (reserva.getPasajes().stream().anyMatch(p -> p.getNombre().equals(cliente.getName()))) {
                        reservaEncontrada = true;
                        // Mostrar detalles de la reserva del cliente
            %>
                        <h3 class="mt-4">Tu Reserva</h3>
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Tipo de Asiento</th>
                                    <th>Equipaje Extra</th>
                                    <th>Costo</th>
                                    <th>Fecha de Reserva</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td><%= reserva.getTipoAsiento() %></td>
                                    <td><%= reserva.getEquipajeExtra() %> kg</td>
                                    <td><%= reserva.getCosto() %></td>
                                    <td><%= reserva.getFecha().format(dateFormatter) %></td>
                                </tr>
                                <tr>
                                    <td colspan="4">
                                        <h5>Pasajeros</h5>
                                        <ul>
                                            <%
                                            List<DTPasaje> pasajes = reserva.getPasajes();
                                            for (DTPasaje pasaje : pasajes) {
                                            %>
                                                <li><%= pasaje.getNombre() %> <%= pasaje.getApellido() %></li>
                                            <%
                                            }
                                            %>
                                        </ul>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
            <%
                        break;
                    }
                }

                if (!reservaEncontrada) {
                    // Si no tiene una reserva, mostrar el formulario de reserva
            %>
                    <button class="btn btn-primary mt-3" onclick="mostrarFormularioReserva()">Reservar este vuelo</button>
                    
                    <div id="formularioReserva" style="display: none; margin-top: 20px;">
                        <h3>Formulario de Reserva</h3>
                        <form action="MostrarVueloServlet" method="post">
                            <input type="hidden" name="nombreVuelo" value="<%= vuelo.getNombre() %>">
                            
                            <div class="form-group">
                                <label for="tipoAsiento">Tipo de Asiento:</label>
                                <select name="tipoAsiento" class="form-control" required>
                                    <option value="Turista">Turista</option>
                                    <option value="Ejecutivo">Ejecutivo</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="cantidadPasajes">Cantidad de Pasajes:</label>
                                <input type="number" name="cantidadPasajes" class="form-control" min="1" required>
                            </div>

                            <div class="form-group">
                                <label for="equipajeExtra">Unidades de Equipaje Extra:</label>
                                <input type="number" name="equipajeExtra" class="form-control" min="0">
                            </div>

                            <!-- Campos dinámicos para nombres y apellidos de los pasajeros -->
                            <div id="pasajeros" class="form-group">
                                <h5>Pasajeros</h5>
                                <!-- Aquí solo se agregarán los campos si es más de un pasaje -->
                            </div>

                            <button type="submit" class="btn btn-success mt-3">Confirmar Reserva</button>
                        </form>
                    </div>

                    <script>
                        function mostrarFormularioReserva() {
                            document.getElementById('formularioReserva').style.display = 'block';
                        }

                        // Lógica para agregar o no los campos de pasajeros adicionales
                        document.querySelector('input[name="cantidadPasajes"]').addEventListener('change', function() {
                            const cantidad = parseInt(this.value);
                            const pasajerosDiv = document.getElementById('pasajeros');
                            pasajerosDiv.innerHTML = ''; // Limpiar los campos actuales

                            if (cantidad > 1) {
                                for (let i = 2; i <= cantidad; i++) {  // Comenzar desde el segundo pasajero
                                    const pasajeroDiv = document.createElement('div');
                                    pasajeroDiv.id = 'pasajero' +i;
                                    pasajeroDiv.innerHTML = `
                                        <label for="nombrePasajero${i}">Nombre:</label>
                                        <input type="text" name="nombrePasajero${i}" class="form-control" required>
                                        <label for="apellidoPasajero${i}">Apellido:</label>
                                        <input type="text" name="apellidoPasajero${i}" class="form-control" required>
                                    `;
                                    pasajerosDiv.appendChild(pasajeroDiv);
                                }
                            } else {
                                pasajerosDiv.innerHTML = '<p>No es necesario ingresar los nombres de los pasajeros para un solo pasaje.</p>';
                            }
                        });
                    </script>
            <%
                }
            }
        } else {
            // Si no se encontró información del vuelo
            %><p>No se encontró información del vuelo.</p><%
        }
        %>
        </div></div></div>
    </div>

</body>
</html>
