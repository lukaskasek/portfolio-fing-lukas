#ifndef VENDEDOR_H
#define VENDEDOR_H
#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <memory>
#include <map>
#include <sstream>

// class TNotificacion;
#include "Fabrica.h"
#include "Usuario.h"
#include "ISuscripciones.h"
class Vendedor:public Usuario, public ISuscripciones{
    private:std::string RUT;
        std::set<ISuscripciones*> suscriptores;
        //void notificar(ISuscripciones *); //en el teorico dice que es privada
        std::set<Producto*> productos;// 
        std::set<Promocion*> promociones;
        //std::map<std::string, Producto*> productos; //mapa mejor
    public:
    
        Vendedor(std::string  ,std::string ,TFecha , std::string );
        Vendedor(DataVendedor);
        virtual void imprimirUsuario();
        std::string getRut();
        std::set<Producto*> getProductos();
        void agregarProducto(Producto* p);



        virtual~Vendedor();
           
        virtual bool esVendedor();
           
           std::string getRUT();
           std::set<std::string> getsuscriptores();
           

           
           virtual void agregarSuscriptor(Cliente *);
           virtual void eliminarSuscriptor(Cliente *);
           virtual void notificarClientes(TNotificacion* ); 
           void insertarProducto(Producto*);
           void imprimirProdsVendedorCodNom();
           void imprimirProdsConCompraPendDeEnvio(bool& vacio, std::set<int>& codsProds);
           void listarProductosEnVenta(); 
           void listarPromocionesVigentes();
           void agregarPromocion(Promocion*p);
           //ISUSCRIPCIONES CLIENTE
           virtual void agregarNotificacion(TNotificacion*){};
            virtual void consultarNotificaciones(){};
            virtual void agregarSuscripcion(Vendedor*){};
            virtual void eliminarSuscripcion(Vendedor*){};
           //ISUSCRIPCIONES DE CLIENTE
        //    void agregarNotificacion(TNotificacion*);
        //    void consultarNotificaciones();
        //    void agregarSuscripcion(Vendedor*);
        //    void eliminarSuscripcion(Vendedor*);
        //    void setsuscriptores(std::set<std::string>);
        //    bool estaSuscrito(std::string);
        //    void setRUT(std::string);
        //    void eliminar(ISuscripciones *);


};

#include "DataUsuario.h" 

#endif
