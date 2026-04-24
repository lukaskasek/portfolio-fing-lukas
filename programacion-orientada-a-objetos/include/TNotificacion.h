#ifndef TNOTIFICACION
#define TNOTIFICACION
#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <memory>
//#include <sstream>
//#include "Producto.h"
//#include "Usuario.h"
#include "Fabrica.h"

class TNotificacion {
private:
    std::string nickVend;
    std::string nomPromocion;
    std::set<Producto*> prodNoti;
public:
    //constructor
    TNotificacion(std::string, std::string,  std::set<Producto*>);

    //getters
    std::string getNickVend();
    std::string getNomPromocion();
    std::set<Producto*> getProductos();
};
#include "ISuscripciones.h"

#endif