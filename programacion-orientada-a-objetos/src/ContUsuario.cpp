#include "IContUsuario.h"
#include "ContUsuario.h"
#include "Usuario.h"
#include "Vendedor.h"
#include "DataCliente.h"
#include "Cliente.h"

#include "DataVendedor.h" //puse estos includes que faltaban

ContUsuario* ContUsuario::instanciaContUsu = NULL;

ContUsuario::ContUsuario():IContUsuario(){
        cantComen = 0;
};

void ContUsuario::sumarComentario(){
        ++cantComen;
}

ContUsuario* ContUsuario::getInstanciaContUsu() {
    if (instanciaContUsu == nullptr) {
        instanciaContUsu = new ContUsuario();
    }
    return instanciaContUsu;
}

std::map<std::string, Usuario *> ContUsuario::getUsuarios(){
        return colUsuarios;  // Retorna una copia del mapa

}

int ContUsuario::getCantComen(){
        return cantComen;
}

void ContUsuario::ingresarDatosCliente(DataCliente data){
        Usuario* usuario = new Cliente(data);
        colUsuarios[data.getNickname()] = usuario;
        Cliente* cliente = dynamic_cast<Cliente*>(usuario);
        colClientes[data.getNickname()] = cliente;
};

bool ContUsuario::estaComen(int id){
        if (colComentario[id] == NULL){return false;}
        else {return true;}
}

void ContUsuario::ingresarDatosVendedor(DataVendedor data){
        Usuario* usuario = new Vendedor(data);
        colUsuarios[data.getNickname()] = usuario;
        Vendedor* vendedor = dynamic_cast<Vendedor*>(usuario);
        colVendedores[data.getNickname()] = vendedor;
};

void ContUsuario::agregarComentario(Comentario* coment){
        int ID = coment->getID();
        colComentario[ID] = coment;
}

int ContUsuario::sizeCol(){
    return colUsuarios.size();
};

bool ContUsuario::estaUsuario(std::string nick){
        return colUsuarios[nick] != NULL;
        
}

Usuario* ContUsuario::find(std::string nick){
        return colUsuarios[nick];
}

Comentario* ContUsuario::findComen(int ID){
        return colComentario[ID];
}

Vendedor* ContUsuario::findVend(std::string nick){
        return colVendedores[nick];
}

bool ContUsuario::esVaciaVendedor(){
        return colVendedores.empty();
}


void ContUsuario::imprimirUsuarios(){ //casos:a,
        std::map<std::string, Usuario *>::iterator it;
        for (it= colUsuarios.begin(); it != colUsuarios.end(); ++it){
                Usuario* usr = it->second;
                usr->imprimirUsuario();
        }
}

void ContUsuario::imprimirNicknamesUsu(){
        std::map<std::string, Usuario *>::iterator it;
        for (it= colUsuarios.begin(); it != colUsuarios.end(); ++it){
                Usuario* usr = it->second;
                std::string nombre = usr->getNickname();
                std::cout << nombre << "\n" << std::endl;
        }
}

void ContUsuario::imprimirVendedores(){
        std::map<std::string, Usuario *>::iterator it;
        for (it= colUsuarios.begin(); it != colUsuarios.end(); ++it){
                Usuario* usr = it->second;
                if (usr->esVendedor()){
                        usr->imprimirUsuario();
                }
        }
};

void ContUsuario::imprimirNickVendedores(){
        std::map<std::string, Usuario *>::iterator it;
        for (it= colUsuarios.begin(); it != colUsuarios.end(); ++it){
                Usuario* usr = it->second;
                if (usr->esVendedor()){
                        std::string nombre = usr->getNickname();
                        std::cout << nombre << "\n" << std::endl;
                }
        }
};

void ContUsuario::imprimirNickClientes(){
        std::map<std::string, Usuario *>::iterator it;
        for (it= colUsuarios.begin(); it != colUsuarios.end(); ++it){
                Usuario* usr = it->second;
                if (!(usr->esVendedor())){
                        std::string nombre = usr->getNickname();
                        std::cout << nombre << "\n" << std::endl;
                }
        }
};


Usuario* ContUsuario::buscarPorNombre(std::string usuario){
        return colUsuarios[usuario];    
}
Vendedor* ContUsuario::buscarVendPorNombre(std::string vend){ //aca decia Usuario*, puse Vendedor*
        return colVendedores[vend];
};

Cliente* ContUsuario::buscarClientePorNombre(std::string cli){ //aca decia Usuario*, puse Cliente*
        return colClientes[cli];
};


void ContUsuario::imprimirClientes(){
        std::map<std::string, Usuario *>::iterator it;
        for (it= colUsuarios.begin(); it != colUsuarios.end(); ++it){
                Usuario* usr = it->second;
                if (!(usr->esVendedor())){
                        usr->imprimirUsuario();
                }
        }
};


// std::map<std::string, Vendedor*> ContUsuario::getColVendedores(){
//         return this->colVendedores;
// };

// std::map<std::string, Cliente*> ContUsuario::getColClientes(){
//         return this->colClientes;
// };

void ContUsuario::listarVendedoresNoSubsXCliente(Cliente* cli){
     std::map<std::string, Usuario *>::iterator it; //se puede iterar aca o sobre la coleccion de Vendedores
     for (it= colUsuarios.begin(); it != colUsuarios.end(); ++it){
                Usuario* usr = it->second;
                if (usr->esVendedor()){
                        // Vendedor* vnd=dynamic_cast<Vendedor*>(usr);
                        if(cli->getColSuscripciones().find(usr->getNickname())==cli->getColSuscripciones().end()){
                                std::cout <<usr->getNickname() <<std::endl;
                        }
                }
                
        }
};

void ContUsuario::listarVendSuscripto(Cliente* cli){
     std::map<std::string, Usuario *>::iterator it; //se puede iterar aca o sobre la coleccion de Vendedores
     for (it= colUsuarios.begin(); it != colUsuarios.end(); ++it){
                Usuario* usr = it->second;
                if (usr->esVendedor()){
                        Vendedor* vnd=dynamic_cast<Vendedor*>(usr);
                        if(cli->estaSuscrito(vnd)){
                                std::cout <<usr->getNickname() <<std::endl;
                        }
                }
                
        }
};

//     ContUsuario::~ContUsuario() {
//     // Implementación del destructor (si es necesario)
// };
