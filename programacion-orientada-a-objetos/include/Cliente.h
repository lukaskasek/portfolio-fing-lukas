#ifndef CLIENTE_H
#define CLIENTE_H

#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <memory>
#include <sstream>
#include <stack>
#include <map>
// #include "Usuario.h"
// #include "ISuscripciones.h"
// #include "TDireccion.h"
// #include "Compra.h"
// #include "TNotificacion.h"
// #include "Vendedor.h"
// #include "TFecha.h"

  // Asegúrate de incluir Usuario.h antes de usarlo


#include "Fabrica.h"
#include "ISuscripciones.h"

#include "Usuario.h"

class Cliente:public Usuario,public ISuscripciones {
    private: 
        TDireccion direccion;
        std::string ciudad;
        std::set<Compra*> compras; 
        std::stack<TNotificacion*> notificaciones;
        std::map<std::string,Vendedor*> colSuscripciones;
    public:
        // Constructor
        Cliente(std::string, std::string, TFecha, TDireccion,std::string); //no deja
        Cliente(DataCliente);
        virtual~Cliente();

        // Getters
        TDireccion getDireccion();
        std::string getCiudad();
        virtual void imprimirUsuario();
        void imprimirDireccion();
        std::set<TNotificacion> getNotificaciones();
        std::map<std::string,Vendedor*> getColSuscripciones();
        std::set<Compra*> getCompras(); 

        // Setters
        // void setCiudad();
        // void setDireccion();
        
        virtual void agregarSuscripcion(Vendedor* vendedor);
        void listarComprasRealizadas();


        // Métodos
        bool estaSuscrito(Vendedor* vend);
        virtual bool esVendedor();
        virtual void eliminarSuscripcion(Vendedor* vnd);
        virtual void consultarNotificaciones();
        // void notificar(TNotificacion);
        void agregarCompra(Compra* compra);
        virtual void agregarNotificacion(TNotificacion* noti);
        //ISUSCRIPCIONES VENDEDOR
        //void eliminarSuscriptor(Cliente*);
        //ISUSCRIPCIONES VENDEDOR
        virtual void notificarClientes(TNotificacion*){};
        virtual void agregarSuscriptor(Cliente*){};
        virtual void eliminarSuscriptor(Cliente*){};
};

#include "Vendedor.h"

#endif
