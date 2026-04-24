#include <iostream>
#include "Compra.h"
#include "Producto.h"
#include "ProductosEnPromo.h"
#include "TFechaActual.h"
#include "CompraProd.h"
//#include "Promocion.h"


//#include "Promocion.h"


//#include "TFecha.h"


Compra::Compra(TFechaActual* f, int monto, int id){
    this->fecha = f;
    this->MontoFinal = monto;
    this->id= id;
};

//getters
TFechaActual* Compra::getFechaCompra(){
    return this->fecha;
};

float Compra::getMontoFinal(){
    return this->MontoFinal;
};

std::map<int,Producto*> Compra::getProductos(){
    return this->productos;
};

std::map<int, CompraProd*> Compra::getCompraProducto(){
    return this->compraProducto;
};

Cliente* Compra::getClienteAsociado(){
    return clienteAsociado;
};

int Compra::getId(){
    return id;
}

//setters
/*void Compra::setFecha(const TFechaActual& f){
    this->fecha = f;
};*/

void Compra::setMontoFinal(int monto){
    this->MontoFinal = monto;
};

//void Compra::agregarProdACompra(Producto prod, int cod, int cant){}; creo que no hay que usar nunca

int Compra::aplicarDescuento(int precio, int cant, int codProd, Producto* prod){
    int precioNuevo = precio;
    Promocion* promo = prod->getPromo();
    // ProductosEnPromo* prodsProm = promo->buscarProdPromoXCod(codProd);
    if (promo != NULL && promo->buscarProdPromoXCod(codProd)->getCantidadMin() <= cant) { 
        int desc = promo->getDescuento();
        desc = desc * precio * 0.01;
        precioNuevo = precioNuevo - desc;
    }
    printf("\nEl precio del producto es: ");
    std::cout << precioNuevo << "\n" << std::endl;
    return precioNuevo;
};

void Compra::sumarAlMonto(int pre){
    this->MontoFinal += pre;
};

void Compra::asociarCompraProd(CompraProd* cp, int cod){
    this->compraProducto[cod] = cp;
};


void Compra::imprimirCompraCompleto(){
    printf("\nResumen Compra:\n");
    int id = this->getId();
    printf("\nId: %d\n", id);
    printf("\nFecha:");
    TFechaActual* fecha = this->fecha;
    fecha->imprimirFecha();
    int monto = this->MontoFinal;
    printf("\nMonto Final: $%d\n", monto);
    printf("\nDetalle productos:\n");
    std::map<int, Producto*>::iterator it;
    std::map<int, Producto*> prods = this->getProductos();
    for (it=prods.begin(); it!=prods.end(); it++) {
        it->second->imprimirProducto(); 
        int codP = it->second->getCodigo();
        CompraProd * compraPro = it->second->buscarCompraPro(codP);
        compraPro->imprimirCompraProd();
    }
};


void Compra::asociarCliente(Cliente* cli){
    clienteAsociado=cli;
}


void Compra::agregarProducto(Producto* prod, int cod){
    this->productos[cod] = prod; 
}
