#ifndef TDIRECCION
#define TDIRECCION
#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <memory>
//#include <sstream>
#include "Fabrica.h"

class TDireccion{
private:
    std::string calle;
    int numero;
public:
    TDireccion(std::string, int);
    std::string getCalle();
    int getNumero();
};  
#include "ProductosEnPromo.h"

#endif