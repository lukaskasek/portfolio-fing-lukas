#include <iostream> // Para std::cin y std::getline
#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <memory>
#include <sstream>
#include <set>
#include "Promocion.h"
#include "TFecha.h"
#include "Producto.h"

// Constructor
Promocion::Promocion(std::string nombre, std::string descripcion, TFecha* fechaVenc, int descuento){
    this->nombre = nombre;
    this->descripcion= descripcion;                                                          
    this->fechaVenc =fechaVenc;
    this->descuento = descuento;   
};

// Getters
std::string Promocion::getNombre() {
    return nombre;
}

std::string Promocion::getDescripcion() {
    return descripcion;
}

TFecha* Promocion::getFechaVenc() {
    return fechaVenc;
}

int Promocion::getDescuento() {
    return descuento;
}

std::set<Producto*> Promocion::getProductos() {
    return productos;
}

// Setters
void Promocion::setNombre(const std::string& nom) {
    nombre = nom;
}

void Promocion::setDescripcion(const std::string& desc) {
    descripcion = desc;
}

void Promocion::setFechaVenc( TFecha* fVenc) {
    fechaVenc = fVenc;
}

void Promocion::setDescuento(int descu) {
    descuento = descu;
}

void Promocion::agregarProdAPromo(Producto* producto) {
    productos.insert(producto);
};


// void Promocion::agregarProdAPromoCantMin(Producto* producto,int cantMin) { //no se cual de las dos esta bien
//     ProductosEnPromo* p=new ProductosEnPromo(producto,cantMin);
//     promProductos.insert(p);
// };
//la de abajo creo que es

void Promocion::agregarProdAPromoCantMin(Producto* producto, int cantMin) {
    ProductosEnPromo* p = new ProductosEnPromo(producto,cantMin);
    promProductos[producto->getCodigo()] = p;
}

void Promocion::devolverDatosProdsPromo(){
    std::set<Producto*>::iterator it;
    std::set<Producto*> prods = this->getProductos();
    for (it= prods.begin(); it != prods.end(); ++it){
        //imprimir datos prod
        (*it)->imprimirProducto();
        //imprimir datos vendedor
        Vendedor* vnd = (*it)->getVendAsociado();
        vnd->imprimirUsuario();
    }
};

void Promocion::imprimirPromocion(){
     std::cout << "Nombre: " << getNombre() << std::endl;
     std::cout << "Descripción: " << getDescripcion() << std::endl;
     std::cout << "Fecha de Vencimiento: ";
     getFechaVenc()->imprimirFecha();
     std::cout << std::endl;
     std::cout << "Descuento: " << getDescuento() << "%" << std::endl;
};

std::map<int, ProductosEnPromo*> Promocion::getPromProductos(){
    return this->promProductos;
};

void Promocion::asociarVendedor(Vendedor* vend){
    vendedorAsociado=vend;
}

ProductosEnPromo* Promocion::buscarProdPromoXCod(int codProd){
    return promProductos[codProd];
}