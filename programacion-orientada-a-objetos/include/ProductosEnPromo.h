#ifndef PRODUCTOSENPROMO
#define PRODUCTOSENPROMO
#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <memory>
#include <sstream>
//#include "Producto.h"
#include "Fabrica.h"

class ProductosEnPromo{
private:
    int cantidadMin;
    Producto* producto;
public:
    //constructor
    ProductosEnPromo(Producto* producto, int cantidadMin); // y esto?


    //metodos de acceso
    int getCantidadMin();

    //destructor
    virtual ~ProductosEnPromo(){};
    
};
#include "Promocion.h"

#endif