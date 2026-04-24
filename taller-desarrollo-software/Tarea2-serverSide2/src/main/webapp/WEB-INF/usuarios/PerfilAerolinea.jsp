<%@page import="logica.DTAerolinea"%>
<%@page import="logica.DTCliente"%>

<%@page import="logica.EstadoSesion"%>
<%@page import="java.util.List"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
 <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Volando.uy - Perfil de Usuario</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet" href="media/styles/styles.css">
</head>
<body>
<%switch((EstadoSesion) request.getSession().getAttribute("estado_sesion")){
		case NO_LOGIN:%>
				<jsp:include page="/WEB-INF/template/barrasDeNavegacion/barraVisitante.jsp"/>	
				<%break;
		case LOGIN_CORRECTO:
			if(request.getSession().getAttribute("user") instanceof DTCliente){%>
			<jsp:include page="/WEB-INF/template/barrasDeNavegacion/barraCliente.jsp"/>
		<%}else if(request.getSession().getAttribute("user") instanceof DTAerolinea){%>
			<jsp:include page="/WEB-INF/template/barrasDeNavegacion/barraAerolinea.jsp"/>
		<% }
			break;
		case LOGIN_INCORRECTO:
			break;
			
		}%>
<% DTAerolinea a = (DTAerolinea) request.getAttribute("perfil");
	String fotoPerfil = null;
	if(a.getFoto()!= null){
		fotoPerfil= a.getFoto();
	}else{
		fotoPerfil ="media/images/default.webp";
	}
	List<List<String>> ingresadas = (List<List<String>>) request.getAttribute("ingresadas");
	List<List<String>> confirmadas = (List<List<String>>) request.getAttribute("confirmadas");
	List<List<String>> rechazadas = (List<List<String>>) request.getAttribute("rechazadas");

	%>
	
	<div class="row">
	<%switch((EstadoSesion) request.getSession().getAttribute("estado_sesion")){
    case NO_LOGIN:
        System.out.println("no esta iniciado");%>
            <jsp:include page="/sideBarVisitante"/>
    <%break;
    case LOGIN_CORRECTO:
        System.out.println(" esta iniciado");
        if(request.getSession().getAttribute("user") instanceof DTCliente){%>
                <jsp:include page="/sideBarCliente"/>
        <%}else if(request.getSession().getAttribute("user") instanceof DTAerolinea){%>
                <jsp:include page="/sideBarAerolinea"/>
        <% }
        break;
    case LOGIN_INCORRECTO:
        break;
}%>
		<div class="col-md-10">
            <div class="container mt-5">
                <div class="row justify-content-center">
                  <!-- Columna para la información del perfil (más estrecha) -->
                <div class="col-md-8">
                    <div class="d-flex mb-3">
                        <img src="media/images/<%=fotoPerfil %>" alt="Foto Usuario1" class="img-fluid rounded" style="width: 262px; height: 175px; object-fit: cover;">
                        <div class="ms-3">
                            <h2><%=a.getNick() %></h2>
                        </div>
                    </div>
                    <div class="details">
                        <p><strong>Nombre:</strong><%=a.getName() %></p>
                        <p><strong>Correo:</strong> <%=a.getCorreo() %></p>
                        <p><strong>Descripción:</strong> <%=a.getDescr() %></p>
                        <p><strong>Link:</strong> <a href="<%=a.getLink()%>" target="_blank"><%=a.getLink() %></a></p> 
                    </div>
                </div>
                  
                  <!-- Columna para las rutas de vuelo confirmadas (más estrecha) -->
                  <div class="col-md-3">
                    <h4>Rutas de Vuelo Confirmadas:</h4>
                    <div class="row">
                    <%if(confirmadas!=null && !confirmadas.isEmpty()){
                    	for(List<String> Ruta : confirmadas){
                    
                        	%>
                        	<div class="col-12 mb-3">
                                        <div class="card">
                                            <h5 class="ms-3">
                                               <img src="media/images/<%= Ruta.get(1) %>" alt="Fotorut" class="img-square img-fluid">
												<a id="link-route" href="rutaDeVuelo?route=<%= Ruta.get(0) %>"><%= Ruta.get(0) %></a>

                                            </h5>
                                        </div>
                           </div>
                        <% }
                    }else{%>
                    	<div class="col-12 mb-3">
                        <div class="card">
                            <h5 class="ms-3">
                                <p> Esta aerolinea no tiene rutas confirmadas</p>
                                
                               
                            </h5>
                        </div>
           </div>
                   <% }%>
                    
                    </div>
                            
                                
    
                            
                    <div id="info-privada" style="display: none;">
                        <h4>Rutas de Vuelo Ingresadas</h4>
                        <div class="row">
                    <%if(ingresadas!=null && !ingresadas.isEmpty()){
                    	for(List<String> Ruta : ingresadas){
                        	%>
                        	<div class="col-12 mb-3">
                                        <div class="card">
                                            <h5 class="ms-3">
                                               <img src="media/images/<%= Ruta.get(1) %>" alt="Fotorut" class="img-square img-fluid">
												<a id="link-route" href="rutaDeVuelo?route=<%= Ruta.get(0) %>"><%= Ruta.get(0) %></a>

                                            </h5>
                                        </div>
                           </div>
                        <% }
                    }else{%>
                    	<div class="col-12 mb-3">
                        <div class="card">
                            <h5 class="ms-3">
                                <p> Esta aerolinea no tiene rutas ingresadas o todas han sido rechazadas o confirmadas</p>
                                
                               
                            </h5>
                        </div>
           </div>
                   <% }%>
                        </div>
                        <h4>Rutas de Vuelo Rechazadas</h4>
                        <div class="row">
                    <%if(rechazadas!=null && !rechazadas.isEmpty()){
                    	for(List<String> Ruta : rechazadas){
                        	%>
                        	<div class="col-12 mb-3">
                                        <div class="card">
                                            <h5 class="ms-3">
                                               <img src="media/images/<%= Ruta.get(1) %>" alt="Fotorut" class="img-square img-fluid">
												<a id="link-route" href="rutaDeVuelo?route=<%= Ruta.get(0) %>"><%= Ruta.get(0) %></a>

                                            </h5>
                                        </div>
                           </div>
                        <% }
                    }else{%>
                    	<div class="col-12 mb-3">
                        <div class="card">
                            <h5 class="ms-3">
                                <p> Esta aerolinea no tiene rutas rechazadas</p>
                                
                               
                            </h5>
                        </div>
           </div>
                   <% }%>
                        </div>
                    </div>
                    
                    </div>
                  </div>
                </div>
              </div>
	</div>
 		
              
        
</body>
</html>