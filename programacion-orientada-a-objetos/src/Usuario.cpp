#include <iostream> // Para std::cin y std::getline
#include "IContUsuario.h"
#include "Usuario.h"

Usuario::~Usuario(){}

Usuario::Usuario(std::string nickname ,std::string contrasena,TFecha fecha): fecha(fecha){
    this->nickname = nickname;
    this->contrasena = contrasena;
    //this->fecha(fecha.getDia(),fecha.getMes(),fecha.getAnio());//no se como asignarle a fecha su fecah
};

void Usuario::agregarComen(Comentario* coment){
    int id = coment->getID();
    comentarios[id] = coment;
}

void Usuario::imprimirFecha(){
    int dia = fecha.getDia();
    int mes = fecha.getMes();
    int ano = fecha.getAnio();
    printf("\n| Fecha:\n|          %d/%d/%d\n|",dia,mes,ano);
}


std::string Usuario::getNickname(){
    return this->nickname;
};

void Usuario::imprimirComentarios(){
    std::map<int, Comentario *>::iterator it;
    for (it= comentarios.begin(); it != comentarios.end(); ++it){
            Comentario* com = it->second;
            com->imprimirComentarioParaElim();
    }
}

bool Usuario::estaComen(int ID){
    return (comentarios[ID] != NULL);
}

bool Usuario::esVendedor(){
    return false;
};


void Usuario::eliminarRefCom(int ID){
    comentarios.erase(ID);
}
