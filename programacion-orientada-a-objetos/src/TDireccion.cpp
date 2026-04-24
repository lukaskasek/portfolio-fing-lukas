#include <iostream> // Para std::cin y std::getline
#include "TDireccion.h"

TDireccion::TDireccion(std::string calle, int numero){
    this->calle=calle;
    this->numero=numero;
};

std::string TDireccion::getCalle() {
    return this->calle;
};

int TDireccion::getNumero() {
    return this->numero;
};