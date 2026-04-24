#include <iostream> // Para std::cin y std::getline
#include "IContUsuario.h"
#include "Vendedor.h"
#include "Usuario.h"
#include "Cliente.h"
#include "CompraProd.h"
#include "TFechaActual.h"

class Usuario;

Vendedor::Vendedor(std::string nickname ,std::string contrasena,TFecha fecha, std::string RUT): Usuario(nickname, contrasena, fecha),ISuscripciones(){
    this->RUT = RUT;
}

Vendedor::Vendedor(DataVendedor data):Usuario(data.getNickname(), data.getContrasena(), data.getFecha()){
    this->RUT = data.getRut();
};

Vendedor::~Vendedor(){
}

std::set<Producto*> Vendedor::getProductos(){
    return this->productos;
}

std::string Vendedor::getRut(){
    return this->RUT;
}



void Vendedor::imprimirUsuario(){
    printf("\n_______________\n| ");
    std::string nombre = getNickname();
    std::cout << nombre << std::endl;
    printf("|\n|");
    imprimirFecha();
    printf("\n| Rut:\n|          ");
    std::string raut = getRut();
    std::cout << raut << std::endl;
    printf("\n|___________\n\n");
}

void Vendedor::insertarProducto(Producto* nuevoProducto){
    productos.insert(nuevoProducto); //lo cambiaron a set 
};


bool Vendedor::esVendedor(){
    return true;
}




void Vendedor::imprimirProdsVendedorCodNom(){
    std::set<Producto*>::iterator it;
    std::set<Producto*> prods=this->getProductos();
        for (it=prods.begin(); it != prods.end(); ++it){
                (*it)->imprimirProductoCodNom(); //segun chatgpt va asi el it
        }
}


void imprimirDatosVend(){
    
}


void Vendedor::imprimirProdsConCompraPendDeEnvio(bool& vacio, std::set<int>& codsProds) {
    std::set<Producto*>::iterator it;
    std::set<Producto*> prods = this->getProductos();
    vacio = true; // Asumimos inicialmente que está vacío
    if (prods.begin() != prods.end()) {
        for (it = prods.begin(); it != prods.end(); ++it) {
            std::map<int, CompraProd*> comprProd = (*it)->getCompraProd();
            std::map<int, CompraProd*>::iterator itComProd;
            for (itComProd = comprProd.begin(); itComProd != comprProd.end(); ++itComProd) {
                if (!itComProd->second->getEnviado()) {
                    (*it)->imprimirProducto();
                    vacio = false; // Si encontramos al menos un producto con compra pendiente, no está vacío
                    codsProds.insert((*it)->getCodigo());
                    break; // Podemos salir del bucle una vez que encontramos una compra pendiente
                }
            }
        }
    }
}

       
       /* std::map<std::string, CompraProd*> comprasProd = (*it)->getCompraAsociada()->getCompraProducto();
        std::map<std::string, CompraProd*>::iterator itComProd;
        for(itComProd=comprasProd.begin(); itComProd != comprasProd.end(); ++itComProd){
            if(!(*itComProd)->getEnviado){
                (*it)->imprimirProducto(); 

            }
        }
        if(!(*it)->getCompraAsociada()->getCompraProducto()->getEnviado()){
            (*it)->imprimirProducto(); 
       
        //if ((*it)->getCompraProducto()->getEnviado()){
            //(*it)->imprimirProducto(); 
        }
    }
}*/



void Vendedor::listarProductosEnVenta() {
    if (productos.empty()) {
        std::cout << "No hay productos en venta.\n";
    } else {
        for (const auto& prod : productos) {
            std::cout << "Producto: " << prod->getNombre() << std::endl;
        }
    }
}

void Vendedor::listarPromocionesVigentes() { ///en ContProducto hay una funcion muy parecida pero en general
        for (const auto& promo : promociones) {
            TFechaActual* fecha;
            fecha = TFechaActual::getInstanciaFecha(1,1,1);
            if (promo->getFechaVenc()->mayoroIgual(fecha))
            std::cout << "Promoción: " << promo->getNombre() << ", Descuento: " << promo->getDescuento() << "%" << std::endl;
        }
}


void Vendedor::agregarSuscriptor(Cliente* cliente){
    suscriptores.insert(cliente);
}


void Vendedor::eliminarSuscriptor(Cliente* cli){
    suscriptores.erase(cli);
}

void Vendedor::notificarClientes(TNotificacion* noti){
    for(ISuscripciones* cliente: suscriptores){
        cliente->agregarNotificacion(noti);
    }  
}

void Vendedor::agregarProducto(Producto* prod){
    productos.insert(prod);
}

void Vendedor::agregarPromocion(Promocion*p){
    promociones.insert(p);
}
