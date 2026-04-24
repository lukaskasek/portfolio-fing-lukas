<%@page import="java.util.List"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.time.LocalTime"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="logica.EstadoSesion"%>
<%@page import="logica.DTCliente"%>
<%@page import="logica.DTAerolinea"%>
<%@page import="logica.DTUser"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Volando.uy - Ruta de Vuelo</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="media/styles/styles.css">
</head>
<body>
    <%
    switch((EstadoSesion) request.getSession().getAttribute("estado_sesion")){
        case NO_LOGIN:
            System.out.println("no esta iniciado");
    %>
            <jsp:include page="/WEB-INF/template/barrasDeNavegacion/barraVisitante.jsp"/>
    <%
            break;
        case LOGIN_CORRECTO:
            System.out.println(" esta iniciado");
            if(request.getSession().getAttribute("user") instanceof DTCliente){
    %>
                <jsp:include page="/WEB-INF/template/barrasDeNavegacion/barraCliente.jsp"/>
    <%
            } else if(request.getSession().getAttribute("user") instanceof DTAerolinea){
    %>
                <jsp:include page="/WEB-INF/template/barrasDeNavegacion/barraAerolinea.jsp"/>
    <%
            }
            break;
        case LOGIN_INCORRECTO:
            break;
    }
    %>

<div class="row">
    <%
    switch((EstadoSesion) request.getSession().getAttribute("estado_sesion")){
        case NO_LOGIN:
            System.out.println("no esta iniciado");
    %>
            <jsp:include page="/sideBarVisitante"/>
    <%
            break;
        case LOGIN_CORRECTO:
            System.out.println(" esta iniciado");
            if(request.getSession().getAttribute("user") instanceof DTCliente){
    %>
                <jsp:include page="/sideBarCliente"/>
    <%
            } else if(request.getSession().getAttribute("user") instanceof DTAerolinea){
    %>
                <jsp:include page="/sideBarAerolinea"/>
    <%
            }
            break;
        case LOGIN_INCORRECTO:
            break;
    }
    %>
        <div class="col-md-10">
            <div class="container mt-5">
                <div class="row justify-content-center">
                    <%
                    
                    List<String> datosRuta = (List<String>) request.getAttribute("datosRuta");
                   
                    List<String> vuelos = (List<String>) request.getAttribute("vuelos");
                 
                    List<String> imagenes = (List<String>) request.getAttribute("imagenes");
                  
                    String from = request.getParameter("from");
                  

                    // Extraer datos de la lista
                    String nombre = datosRuta.get(0);
                    String descripcionCorta = datosRuta.get(2);
                    String descripcion = datosRuta.get(1);
                    String costoBaseTurista = datosRuta.get(4);
                    String costoBaseEjecutivo = datosRuta.get(5);
                    String costoEquipajeExtra = datosRuta.get(6);
                 
                    String ciudadOrigen = datosRuta.get(7);
                    String ciudadDestino = datosRuta.get(8);
                 
                    LocalDate fechaAlta = LocalDate.of(
                        Integer.parseInt(datosRuta.get(11)), 
                        Integer.parseInt(datosRuta.get(10)), 
                        Integer.parseInt(datosRuta.get(9))
                    );
                 
                    LocalTime hora = LocalTime.of(
                        Integer.parseInt(datosRuta.get(12)),
                        Integer.parseInt(datosRuta.get(13))
                    );
                  
                    String estado = datosRuta.get(14);
                    String imagenRuta = datosRuta.get(15);

                    // Categorías estarán desde el índice 15 en adelante
                    List<String> categorias = datosRuta.subList(16, datosRuta.size());
                 
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                
                    String foto = "media/images/defaultRuta.webp";
                    if(!imagenRuta.equalsIgnoreCase("sinImagen")){
                    	foto = imagenRuta;
                    	if (!foto.contains("uploads")){
                    		foto= "media/images/" +imagenRuta;
                    	}
                    }

                    %>

                    <div class="row" id="flight-info">
                        <div class="col-md-8">
                            <div class="d-flex mb-3">
                                <img src="${pageContext.request.contextPath}/<%=foto %>" alt="Imagen de Ruta" class="img-fluid rounded" style="width: 262px; height: 175px; object-fit: cover;">
                                <div class="ms-3">
                                    <h3><%= nombre %></h3>
                                    <p><%= descripcionCorta %></p>
                                </div>
                            </div>
                            <div class="details">
                                <p><strong>Descripción:</strong> <%= descripcion %></p>
                                <p><strong>Hora:</strong> <%= hora.format(timeFormatter) %></p>
                                <p><strong>Ciudad Origen:</strong> <%= ciudadOrigen %></p>
                                <p><strong>Ciudad Destino:</strong> <%= ciudadDestino %></p>
                                <p><strong>Costo Turista:</strong> <%= costoBaseTurista %></p>
                                <p><strong>Costo Ejecutivo:</strong> <%= costoBaseEjecutivo %></p>
                                <p><strong>Costo Equipaje Extra:</strong> <%= costoEquipajeExtra %></p>
                                <p><strong>Categorías:</strong> <%= String.join(", ", categorias) %></p>
                                <p><strong>Fecha de alta:</strong> <%= fechaAlta.format(dateFormatter) %></p>
                                <p><strong>Estado:</strong> <%= estado %></p>
                            </div>
                        </div>
                        <div class="col-md-4">
   	<h4>Vuelos asociados</h4>
    <%
    if (vuelos != null && !vuelos.isEmpty() && !vuelos.get(0).equals("noHayVuelos")) {
        for (int i = 0; i < vuelos.size(); i++) {
            String vuelo = vuelos.get(i);
            String imagen = "/media/images/default.webp";
            if (imagenes.get(i)!=null){
            	imagen = imagenes.get(i);
            }
    %>
        <div class="d-flex align-items-center mb-3">
            <img src="media/images/<%= imagen %>" alt="Vuelo" class="img-fluid rounded me-2" style="width: 100px; height: 68px; object-fit: cover;">
            <!-- Redirigir a un nuevo servlet para mostrar los detalles del vuelo -->
            <a href="MostrarVueloServlet?codigo=<%= vuelo %>" class="text-decoration-none"><%= vuelo %></a>
        </div>
    <%
        }
    } else {
    %>
        <p>(no hay vuelos asociados)</p>
    <%
    }
    %>
</div>

                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>