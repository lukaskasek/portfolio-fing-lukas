#include "CompraProd.h"
#include "Producto.h"


// Constructor
CompraProd::CompraProd(int cant, bool env, Producto* prod){
    this->cantidad = cant;
    this->enviado = env;
    this->producto = prod;
};

// Métodos de acceso
int CompraProd::getCantidad() {
    return this->cantidad;
};

bool CompraProd::getEnviado() {
    return this->enviado;
};

Compra* CompraProd::getCompraAsociada(){
    return compraAsociada;
};

Producto* CompraProd::getProducto(){
    return this->producto;
};

void CompraProd::setCantidad(int cant){
    cantidad = cant;
};

void CompraProd::setEnviado(bool env){
    enviado = env;
};

void CompraProd::setCompraAsociada(Compra* compra){
    this->compraAsociada = compra;
};

void CompraProd::imprimirCompraProd(){
    printf("\nCantidad: %d\n", this->cantidad);
    if (this->enviado) {
        printf("Enviado: SI\n\n");
    } else {
        printf("Enviado: NO\n\n");
    }
};
