
<%@page import="java.util.List"%>
<%@page import="logica.EstadoSesion" %>
<%@page import="logica.DTCliente" %>
<%@page import="logica.DTAerolinea" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	
	<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Volando.uy - Usuarios</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet" href="media/styles/styles.css">
    
</head>
<body>
	<%switch((EstadoSesion) request.getSession().getAttribute("estado_sesion")){
    case NO_LOGIN:
        System.out.println("no esta iniciado");%>
        <jsp:include page="/WEB-INF/template/barrasDeNavegacion/barraVisitante.jsp"/>    
    <%break;
    case LOGIN_CORRECTO:
        System.out.println(" esta iniciado");
        if(request.getSession().getAttribute("user") instanceof DTCliente){%>
            <jsp:include page="/WEB-INF/template/barrasDeNavegacion/barraCliente.jsp"/>
        <%}else if(request.getSession().getAttribute("user") instanceof DTAerolinea){%>
            <jsp:include page="/WEB-INF/template/barrasDeNavegacion/barraAerolinea.jsp"/>
        <% }
        break;
    case LOGIN_INCORRECTO:
        break;
}%>

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
			<div class="container mt-5" >
           <%
            String error = (String) request.getAttribute("no-users");
            if (error != null) {
        %>
            <div class="alert alert-danger" role="alert">
                <%= error %>
            </div>
        <%
            }
        %>
                <div class="row justify-content-center">
                    <!--Aca va la lista de usuarios-->
                    	<% 
				List<String> usuarios = (List<String>)
						request.getAttribute("usuarios");
				if(usuarios != null){
					for(String usuario: usuarios){%>
						<div class = "col-md-12 d-flex align-items-center mb-3">
                        <h3 class ="ms-3">
                  
                            <a id="perfil-linka" href ="${pageContext.request.contextPath}/perfil?nick=<%=usuario%>" style="color: inherit; text-decoration: none;" ><%=usuario%></a>
                        </h3>
                    </div>
					<% }
				}
							%>
						
				
				
                        
                    
                        
        
        
                </div>
            </div>
		</div>
		
	</div>
	
	
</body>
</html>