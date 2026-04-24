#include <iostream> // Para std::cin y std::getline
#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <memory>
#include <sstream>
#include <set>
#include "IContUsuario.h"
#include "DataCliente.h"


DataCliente::DataCliente(std::string nickname, std::string contraseña, TFecha fecha, TDireccion direccion, std::string ciudad): DataUsuario(nickname, contraseña, fecha), direccion(direccion){
    this->ciudad = ciudad;
};

std::string DataCliente::getCiudad() {
    return this->ciudad;
};

TDireccion DataCliente::getDireccion() {
    return this->direccion;
};


