<%@page import="logica.DTCliente"%>
<%@page import="logica.EstadoSesion"%>
<%@page import="logica.DTAerolinea"%>
<%@page import="java.util.List"%>



<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Volando.uy - Perfil de Usuario</title>
    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

    <link rel="stylesheet" href="media/styles/styles.css">
</head>
<body>
	<% DTCliente c =(DTCliente) request.getAttribute("perfil");
	String fotoPerfil = null;
	if(c.getFoto()!= null){
		
			fotoPerfil=c.getFoto();
		
		
		
	}else{
		fotoPerfil ="media/images/default.webp";
	}
	List<String> vuelosReservados = (List<String>) request.getAttribute("reservas");
	%>
	
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
                <!-- Card 1 -->
                <div class="col-md-8">
                    <div class="d-flex mb-3">
                  
                        <img src="media/images/<%=fotoPerfil%>" alt="Foto Usuario" class="img-fluid rounded" style="width: 262px; height: 175px; object-fit: cover;">
                        <div class="ms-3">
                            <h2><%=c.getNick() %></h2>
                        </div>
                    </div>
                    <div class="details">
                        
                            
                        <p><strong>Nombre: </strong><%=c.getName() %></p>
                        <p><strong>Apellido: </strong><%=c.getApellido()%></p>
                        <p><strong>Correo: </strong><%=c.getCorreo()%></p>
                        <p><strong>Fecha de Nacimiento: </strong><%=c.getFechaNac()%></p>
                        <p><strong>Nacionalidad: </strong><%=c.getNacion() %></p>
                        <p><strong>Tipo de Documento: </strong><%=c.getTDocumento() %></p>
                        <p><strong>Número de Documento: </strong><%=c.getNDocumento()%></p>
                        
                    </div>
                </div>
                
                
                <div  id="info-privada" class="col-md-4" style="display: none;" >
                        <h4>Reservas de Vuelo</h4>
                        <%if(vuelosReservados!=null){
                        	for(String vuelo: vuelosReservados){
                            	%>
                            	<div class="row">
                                <div class="col-12 mb-3">
                                    <div class="card">
                                        <h5 class="ms-3">
                                            
                                            <a href="MostrarVueloServlet?codigo=<%=vuelo%>"><%=vuelo%></a>
                                        </h5>
                                    </div>
                                </div>
                            </div>
                            <% }
                        }%>
                        
                        
                </div>
            </div>
        </div>
</div>

</div>	

</body>
</html>