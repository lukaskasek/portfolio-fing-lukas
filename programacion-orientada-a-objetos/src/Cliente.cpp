#include <iostream> // Para std::cin y std::getline
#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <memory>
#include <sstream>
#include <set>
#include "Fabrica.h"
#include "IContUsuario.h"
#include "ContUsuario.h" 
#include "Usuario.h"
#include "Cliente.h"
#include "CompraProd.h"
#include "TFechaActual.h"



#include <stack>

Cliente::~Cliente(){

}

Cliente::Cliente(std::string nickname, std::string contrasena , TFecha fecha, TDireccion direccion,std::string ciudad):Usuario(nickname, contrasena, fecha),ISuscripciones(), direccion(direccion){
    this->ciudad = ciudad;
};

Cliente::Cliente(DataCliente data):Usuario(data.getNickname(), data.getContrasena(), data.getFecha()), direccion(data.getDireccion()){
    this->ciudad = data.getCiudad();
};

std::string Cliente::getCiudad() {
    return this->ciudad;
};


TDireccion Cliente::getDireccion() {
    return this->direccion;
};

std::set<Compra*> Cliente::getCompras(){
    return compras;
}; 


/*
void Cliente::setDireccion():direccion(direccion){

};
*/

void Cliente::imprimirUsuario(){
    printf("\n_______________\n| ");
    std::string nombre = getNickname();
    std::cout << nombre << std::endl;
    printf("|\n|");
    imprimirFecha();
    printf("\n| Direccion:\n|    Ciudad: ");
    imprimirDireccion();
    printf("\n|___________\n\n");
};

void Cliente::imprimirDireccion(){
    std::string ciudadd = ciudad;
    std::cout << ciudadd << std::endl;
    printf("|    Calle:  ");
    std::string calle = direccion.getCalle();
    std::cout << calle << std::endl;
    printf("|    Numero: ");
    int numero = direccion.getNumero();
    printf("%d",numero);
}


void Cliente::listarComprasRealizadas() {
        for ( auto& compra : compras) {
            std::cout << "Fecha de compra: ";
            compra->getFechaCompra()->imprimirFecha(); //no entiendo que pinta con el tipo de fechsa
            std::cout << "Monto final: " << compra->getMontoFinal() << std::endl;
            std::cout << "Productos comprados:" << std::endl;
            std::map<int, CompraProd*> compraProd = compra->getCompraProducto();
            std::map<int, CompraProd*>::iterator it;
            for (it= compraProd.begin(); it != compraProd.end(); ++it) {
                Producto* producto = it->second->getProducto();
                int cantidad = it->second->getCantidad();
                std::cout << " - Producto: " << producto->getNombre() << ", Código: " << producto->getCodigo() << ", Cantidad: " << cantidad << std::endl;
            }
        }
    }




// void Cliente::agregarCompra(Compra* compra){
//     this->compras.insert(compra);
// };

void Cliente::agregarCompra(Compra* compra){
    this->compras.insert(compra);
    // printf("Para ver si quedo la compra asociada al cliente: ");
    // std::set<Compra*> compras = this->getCompras();
    // std::set<Compra*>::iterator it = compras.begin();
    // while((*it)->getId()!=compra->getId() || it != compras.end()){
    //     ++it;   
    // }
    // if(it != compras.end()){
    //     int id = (*it)->getId();
    //     std::cout << id << std::endl;
    // }else{
    //     printf("No se agrego la compra al cliente :(");
};

bool Cliente::esVendedor(){
    return false;
};

std::map<std::string,Vendedor*> Cliente::getColSuscripciones(){
    return this->colSuscripciones;
};

void Cliente::agregarSuscripcion(Vendedor* Vendedor){
    colSuscripciones[Vendedor->getNickname()]=Vendedor;
}

void Cliente::eliminarSuscripcion(Vendedor* Vendedor){
   colSuscripciones.erase(Vendedor->getNickname());
}

void Cliente::consultarNotificaciones(){
    while(!notificaciones.empty()){
        TNotificacion* noti=notificaciones.top();
        notificaciones.pop();
        std::cout << "Notificación de: " << noti->getNickVend() << std::endl;
        std::cout << "Promoción: " << noti->getNomPromocion() << std::endl;
        std::cout << "Productos: ";
        for ( auto producto : noti->getProductos()) {
            std::cout << producto->getNombre() << " ";
        }
        std::cout << std::endl;
    }
}

void Cliente::agregarNotificacion(TNotificacion* noti){
    notificaciones.push(noti);
}

bool Cliente::estaSuscrito(Vendedor* vend){
    return colSuscripciones.find(vend->getNickname()) != colSuscripciones.end();
}
