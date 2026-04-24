<%@ page import="java.util.List"%>
<%@ page import="logica.EstadoSesion" %>
<%@ page import="logica.Vuelo" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Volando.uy - Vuelos</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="media/styles/styles.css">
</head>
<body>

    <!-- Barra de navegación según el estado de sesión -->
    <% switch((EstadoSesion) request.getSession().getAttribute("estado_sesion")) { 
        case NO_LOGIN: %>
            <jsp:include page="/WEB-INF/template/barrasDeNavegacion/barraVisitante.jsp"/>    
        <% break;
        case LOGIN_CORRECTO: %>
            <jsp:include page="/WEB-INF/template/barrasDeNavegacion/barraCliente.jsp"/>
        <% break;
        case LOGIN_INCORRECTO: %>
            <!-- No hay barra o mostrar una para login incorrecto si es necesario -->
        <% break;
    } %>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <!-- Aquí va la lista de vuelos -->
            <% 
                List<Vuelo> vuelos = (List<Vuelo>) request.getAttribute("vuelos");
                if (vuelos != null && !vuelos.isEmpty()) {
                    for (Vuelo vuelo : vuelos) { %>
                        <div class="col-md-12 d-flex align-items-center mb-3">
                            <h3 class="ms-3">
 							   	<a href="${pageContext.request.contextPath}/MostrarVueloServlet?codigo=<%=vuelo.getNombre()%>" 
       								style="color: inherit; text-decoration: none;">
      										  <%= vuelo.getNombre() %> - <%= vuelo.getFecha() %>
   							 	</a>
							</h3>
                        </div>
                    <% }
                } else { %>
                    <div class="col-md-12 text-center">
                        <p>No hay vuelos disponibles en este momento.</p>
                    </div>
                <% } %>
        </div>
    </div>

    <!-- Scripts de Bootstrap -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
