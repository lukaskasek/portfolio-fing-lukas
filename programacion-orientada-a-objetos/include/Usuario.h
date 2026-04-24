#ifndef USUARIO_H
#define USUARIO_H
#include <iostream>
#include <set>
#include <map>
#include <string>
#include <vector>
#include <memory>
#include <sstream>
// #include "IContUsuario.h"
// #include "TFecha.h"
//#include "Comentario.h"
// #include "TNotificacion.h"

// #include "Vendedor.h"
// #include "Cliente.h"
// #include "Compra.h"
// #include "TDireccion.h"
// #include "Producto.h"
// #include "Promocion.h"
// #include "DataVendedor.h"
// #include "DataCliente.h"
// #include "ISuscripciones.h"

#include "Fabrica.h"

class Comentario;

class Usuario{
    private:
        std::string nickname;
        std::string contrasena;
        TFecha fecha;
        std::map<int,Comentario*> comentarios;

    public: 
        Usuario(std::string,std::string,TFecha);
        void agregarComen(Comentario *);
        virtual~Usuario();
        virtual void imprimirUsuario()=0;
        void imprimirFecha();
        std::string getNickname();
        virtual bool esVendedor()=0;
            // virtual std::set<TComentario> ListarComentarios();
            // virtual void sacarComentario(int)= 0;
            // virtual void notificar(TNotificacion)=0;
            //void setnickname(std::string);
            //std::string getcontrasena();
            //void setcontrasena(std::string);
            //TFecha getfecha();
            //void setfecha(TFecha);
        bool estaComen(int);
        void imprimirComentarios();
        void eliminarRefCom(int);

};
#include "Cliente.h"

#endif
