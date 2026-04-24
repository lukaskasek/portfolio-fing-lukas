<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.List" %>
            <!-- Sidebar para Categorías -->
            <div class="col-md-2">
                <nav id="sidebar" class="p-2">
        <h5>Categorías</h5>
        <ul class="nav flex-column">
            <% 
            List<String> categorias = (List<String>)request.getAttribute("categorias");
            if(categorias != null) {
                for(String categoria : categorias) {
            %>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/rutaDeVuelo?categoria=<%=categoria%>"><%=categoria%></a>
                </li>
            <% 
                }
            }
            %>
        </ul>
    </nav>
                <!-- Gestión de Vuelos -->
                <nav id="gestion" class="gestion vuelos p-3 mt-4">
                    <h5>Mi Perfil</h5>
                    <ul class="nav flex-column">
                        <li class="nav-item">
						    <a class="nav-link" href="${pageContext.request.contextPath}/reservarVuelo">Reservar Vuelo</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">Comprar Paquete</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="ConsultaDeReservaDeVuelo.html?from=cli">Reservas</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/usuarios">Usuarios</a>
                        </li>
                    </ul>
                </nav>
            </div>