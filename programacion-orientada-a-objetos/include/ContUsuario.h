#ifndef CONTUSUARIO_H
#define CONTUSUARIO_H
#include <set>
#include <string>
#include <vector>
#include <memory>
#include <sstream> 
#include <map>
// #include "Fabrica.h"
// #include "IContUsuario.h" 
// //#include "Comentario.h"
// #include "Usuario.h"
// #include "Vendedor.h"
// #include "Cliente.h"
// //#include "TNotificacion.h"

//#include "DataUsuario.h"
#include "Fabrica.h"
#include "IContUsuario.h"

class ContUsuario : public IContUsuario {
private:
    ContUsuario();
    static ContUsuario * instanciaContUsu;

    std::map<int, Comentario *> colComentario;
    std::map<std::string, Usuario *> colUsuarios;
    std::map<std::string, Vendedor*> colVendedores; 
    std::map<std::string, Cliente*> colClientes;
    int cantComen;
    
public:
    static ContUsuario* getInstanciaContUsu();
    //otros metodos
    int getCantComen();
    void sumarComentario();
    int sizeCol();
    void imprimirUsuarios(); //casos: a,

    //funciones del MAPA Usuario
    Usuario* find(std::string);
    
    void ingresarDatosVendedor(DataVendedor data); //agrega un vendedor
    void ingresarDatosCliente(DataCliente data); //agrega un cliente 
    Vendedor* findVend(std::string);
    bool esVaciaVendedor();
    //funciones del MAPA Coment
    Comentario* findComen(int ID);
    virtual bool estaUsuario(std::string);

    virtual bool estaComen(int);
    virtual void agregarComentario(Comentario* coment);
    void imprimirVendedores();
    void imprimirClientes();
    virtual void imprimirNicknamesUsu();
    virtual void imprimirNickVendedores();
    virtual void imprimirNickClientes();
    std::map<std::string, Usuario *> getUsuarios(); // ESTO TE DEVUELVE UNA COPIA, SOLO SIRVE PARA VER SI HAY COSAS, NO PARA AGREGAR NI SACAR!!(soy el pelado jeje)
    void listarVendedoresNoSubsXCliente(Cliente* cli);
    void listarVendSuscripto(Cliente* cli);
    // std::map<std::string, Cliente*> getColClientes();
    // std::map<std::string, Vendedor*> getColVendedores();
    Cliente* buscarClientePorNombre(std::string cli);
    Vendedor* buscarVendPorNombre(std::string vend);
    Usuario* buscarPorNombre(std::string);
    virtual ~ContUsuario(){}; //destructor 
    
};
#include "ContProducto.h"


#endif
