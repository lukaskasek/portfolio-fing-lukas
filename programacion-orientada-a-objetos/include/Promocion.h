#ifndef PROMOCION
#define PROMOCION
#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <memory>
#include <sstream>
//#include "TFecha.h"
#include <map>
// #include "Usuario.h"
//#include "Vendedor.h"
//#include "Producto.h"
//#include "ProductosEnPromo.h"
#include "Fabrica.h"

class Promocion {
private:
    std::string nombre;
    std::string descripcion;
    TFecha* fechaVenc;
    int descuento;
    std::map<int, ProductosEnPromo*> promProductos; //la clave es el codigo del producto
    std::set<Producto*> productos;
    Vendedor* vendedorAsociado; //tiene que ser relacionada a un vendedor no?

public:
    // Constructor
    Promocion(std::string nom, std::string desc, TFecha* fVenc, int descu);

    // Getters

    std::string getNombre();
    std::string getDescripcion(); 
    TFecha* getFechaVenc();
    int getDescuento();
    std::set<Producto*> getProductos();
    std::map<int, ProductosEnPromo*> getPromProductos();
    


    // Setters
    void setNombre(const std::string& nom);
    void setDescripcion(const std::string& desc);
    void setFechaVenc(TFecha* fVenc);
    void setDescuento(int descu);

    // Métodos
    void agregarProdAPromo(Producto* producto);
    void agregarProdAPromoCantMin(Producto* producto,int cantMin);
    void aplicarDescuento(int idProducto, int descuento);
    void devolverDatosProdsPromo();
    void imprimirPromocion();
    void asociarVendedor(Vendedor* vend);
    ProductosEnPromo* buscarProdPromoXCod(int codProd);
};
#include "TNotificacion.h"


#endif