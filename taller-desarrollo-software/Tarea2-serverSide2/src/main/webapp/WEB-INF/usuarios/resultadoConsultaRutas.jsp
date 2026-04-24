<%@page import="java.util.List"%>
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
    <title>Volando.uy - Ruta de Vuelo Categoria</title>
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
                    System.out.println("aca larrancamos");
                    List<List<String>> rutasVuelo = (List<List<String>>) request.getAttribute("datosRutas");
                    if (!rutasVuelo.isEmpty()){
                        System.out.println(rutasVuelo.get(0).get(1));
                    }
                    else {
                        System.out.println("esta vacia");
                    }
                    String categoria = request.getParameter("categoria");
                    String foto = "/media/images/default.webp";
                    %>

                    <h2 class="mb-4">Rutas de vuelo con categoría: <%= categoria %></h2>
                    
                    <div id="flight-info">
                        <% if (rutasVuelo != null && !rutasVuelo.isEmpty()) { %>
                            <% for (List<String> ruta : rutasVuelo) { 
                                String imagenRuta = ruta.get(15);
                                System.out.println(imagenRuta);
                                System.out.println(ruta.get(1));
                                System.out.println(ruta.get(2));
                                System.out.println(ruta.get(3));
                                String fotoo = "media/images/defaultRutas.webp";
                                if(!imagenRuta.equalsIgnoreCase("sinImagen")){
                                	fotoo = imagenRuta;
                                	if (!fotoo.contains("uploads")){
                                		fotoo= "media/images/" +imagenRuta;
                                	}
                                }
                            %>
                                <div class="d-flex mb-3">
                                    <img src="${pageContext.request.contextPath}/<%=fotoo %>" alt="Ruta de Vuelo" class="img-fluid rounded" style="width: 150px; height: 100px; object-fit: cover;">
                                    <div class="route-info ml-3">
                                        <h5><a href="rutaDeVuelo?route=<%= ruta.get(0) %>" class="route-link"><%= ruta.get(0) %></a></h5>
                                        <p><%= ruta.get(2) %></p>
                                        <p><small><%= ruta.get(7) %> -> <%= ruta.get(8) %></small></p>
                                    </div>
                                </div>
                            <% } %>
                        <% } else { %>
                            <p>No se encontraron rutas de vuelo para esta categoría.</p>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
