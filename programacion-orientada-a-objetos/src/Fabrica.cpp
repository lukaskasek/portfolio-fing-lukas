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
#include "ContProducto.h"

 Fabrica* Fabrica::instancia = nullptr;

Fabrica::Fabrica(){
    contUsuario = ContUsuario::getInstanciaContUsu();
    contProducto = ContProducto::getInstanciaContProd();
}


Fabrica* Fabrica::getInstancia() {
    if (instancia == nullptr) {
        instancia = new Fabrica();
    }
    return instancia;
}

IContUsuario* Fabrica::getIContUsuario() {
    return ContUsuario::getInstanciaContUsu();
};

IContProducto* Fabrica::getIContProducto() {
    return ContProducto::getInstanciaContProd(); 
;}


Fabrica::~Fabrica() {
    // delete contUsuario;
    // delete contProducto;
}