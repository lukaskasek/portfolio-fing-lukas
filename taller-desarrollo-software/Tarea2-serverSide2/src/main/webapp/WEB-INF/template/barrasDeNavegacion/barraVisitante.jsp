<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/home">Volando.uy</a>
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
                <!-- Opción común para visitantes, clientes y aerolíneas -->
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/usuarios">Usuarios</a>
                </li>
                
                <li class="nav-item" id="visitorOptions">
                   <a class="nav-link" href="${pageContext.request.contextPath}/loginUsuario">Login</a>
                </li>
                <li class="nav-item" id="visitorRegister">
                    <a class="nav-link btn btn-primary text-white" href="${pageContext.request.contextPath}/registroUsuario">Registrarse</a>
                </li>
            </ul>
        </div>
    </nav>