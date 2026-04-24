#include <iostream> // Para std::cin y std::getline
#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <memory>
#include <sstream>
#include <set>
#include "IContUsuario.h"
#include "DataVendedor.h"


DataVendedor::DataVendedor(std::string nickname, std::string contrasena, TFecha fecha, std::string RUT): DataUsuario(nickname, contrasena, fecha){
    this->RUT = RUT;
};

std::string DataVendedor::getRut() {
    return this->RUT;
};

//HOLAAA
