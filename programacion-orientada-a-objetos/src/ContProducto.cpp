#include "ContProducto.h"
#include "Promocion.h"
#include "TFechaActual.h"
#include "CompraProd.h"

#include <cstdio>
#include <iostream>
#include <utility> // Para std::pair
#include <string>


ContProducto * ContProducto::instanciaContProd = NULL;

ContProducto::ContProducto(){
    codigoProducto = 0;
};

ContProducto* ContProducto::getInstanciaContProd() {
    if (instanciaContProd == nullptr) {
        instanciaContProd = new ContProducto();
    }
    return instanciaContProd;
};


// std::map<int, Producto*> ContProducto::getColProductos() {
//     return this->colProductos;
// };

int ContProducto::getCodigoProducto(){
    return codigoProducto;
}

// std::set<Compra*> ContProducto::getColCompra(){
//     return colCompra;
// };

void ContProducto::insertarProducto(Producto* prod){
    int codigoo = prod->getCodigo();
    colProductos[codigoo] = prod;
}


Promocion* ContProducto::findPromocion(std::string string){
    return colPromocion[string];
}

void ContProducto::agregarPromocion(Promocion* promo){
    std::string nombre = promo->getNombre();
    colPromocion[nombre] = promo;
}

void ContProducto::listarPromosVigentes(){
     std::set<Promocion*> promosVigentes;
     TFechaActual* fecha;
     fecha = TFechaActual::getInstanciaFecha(1,1,1);
     std::map<std::string, Promocion *>::iterator it;
     for (it= colPromocion.begin(); it != colPromocion.end(); ++it){
         if (it->second->getFechaVenc()->mayoroIgual(fecha)){
             promosVigentes.insert(it->second);
             it->second->imprimirPromocion();
          }
     };
 };


void ContProducto::sumarCodigoProducto(){
    ++codigoProducto;
}


Promocion* ContProducto::buscarPromoPorNombre(std::string promo){
    return colPromocion[promo];
};

void ContProducto::listarProductos()  {
    // for (auto pair : colProductos) {
    //     printf("Código: %d, Producto: %s\n", pair.first, pair.second->getNombre());
    // }
    std::map<int, Producto *>::iterator it;
    for (it= colProductos.begin(); it != colProductos.end(); ++it){
            printf("Código: %d, Producto:\n", it->first);
                std::cout << it->second->getNombre() <<"\n"<< std::endl;
        }
};

// void ContProducto::listarProductosDisp() {
//     for ( auto pair : colProductos ) {
//         if (pair.second->getStock() > 0) {
//             printf("Código: %d, Producto: %s\n", pair.first, pair.second->getNombre());
//         }
//     }
// }; //ESTA DOS VECES PERO FUNCIONAN DISTINTO


Producto* ContProducto::buscarProducto(int clave){
    return colProductos[clave];
};


/*Producto* ContProducto::buscarProdPorNombre(std::string produ){     //v-creo que esta no sirve
    return colProductos[produ];
}*/


void ContProducto::listarProductosDisp(Vendedor* vendedor) {
    for ( auto pair : colProductos) {
        if (pair.second->getStock() > 0 && pair.second->getVendAsociado() == vendedor) {
            std::cout << "Código: " << pair.first << ", Producto: " << pair.second->getNombre() << std::endl;
        }
    }
};

bool ContProducto::estaProd(int codigoProd){
    return colProductos[codigoProd] !=NULL;
};

Producto* ContProducto::find(int codigo){
    return colProductos[codigo];
};

void ContProducto::imprimirComprasConProdPendiente(Producto* prod, std::set<int>& idsCompras){
    std::map<int, CompraProd*>::iterator it;
    std::map<int, CompraProd*> comProd = prod->getCompraProd(); //obtengo el mapa de compraprod
    for(it=comProd.begin(); it != comProd.end(); ++it){
            if(!it->second->getEnviado()){
                Compra* compra = it->second->getCompraAsociada();
                std::string nickCli = dynamic_cast<Usuario*>(compra->getClienteAsociado())->getNickname(); //obtengo el nick del cliente asociado a la compra asociada al compraProd
                int id = compra->getId();     
                TFechaActual* fecha = compra->getFechaCompra();
                std::cout << nickCli << "," << id << ","; 
                fecha->imprimirFecha();
                idsCompras.insert(id);//inserto el id para corroborar
            }
    }
};


void ContProducto::listarProductosDisp(){
    for (auto it = this->colProductos.begin(); it != this->colProductos.end(); it++) {
        if (it->second->getStock() > 0){
            it->second->imprimirProducto();
        }
    } 
};

