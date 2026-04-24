#ifndef FABRICA_H
#define FABRICA_H

#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <memory>
#include <sstream>
//#include "IContUsuario.h"
//#include "IContProducto.h"
//#include "ContUsuario.h"
//#include "ContProducto.h"
#include "Fabrica.h"

class IContUsuario;
class IContProducto;
class TFecha;
class TDireccion;
class TFechaActual;

class Fabrica {
private:
    static Fabrica* instancia;
    IContUsuario* contUsuario;
    IContProducto* contProducto;
    Fabrica();
public:
    static Fabrica* getInstancia();
    IContUsuario* getIContUsuario();
    IContProducto* getIContProducto();

    ~Fabrica();
};
#include "IContUsuario.h"
#endif