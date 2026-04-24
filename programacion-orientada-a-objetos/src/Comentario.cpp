#include "Comentario.h"
#include "IContUsuario.h"
#include "TFechaActual.h"
#include "TFecha.h"
#include "ContUsuario.h"
#include "Producto.h"
#include "Cliente.h"
#include "Vendedor.h"
#include "Usuario.h"

Comentario::Comentario(std::string txt){
    texto = txt;
    ContUsuario* contUsuari = ContUsuario::getInstanciaContUsu();
    TFechaActual* fechaAct = TFechaActual::getInstanciaFecha(1,1,1);
    int dia = fechaAct->getDia();
    int mes = fechaAct->getMes();
    int anio = fechaAct->getAnio();
    TFecha* fechaDef = new TFecha(dia,mes,anio);
    fecha = fechaDef;
    
    contUsuari->sumarComentario();
    ID =  contUsuari->getCantComen();
    ComenPadre = NULL;
    ProdPadre = NULL;
    usuario = NULL;
    tieneComPadre = false;
    
}
Comentario::~Comentario(){

}
void Comentario::CrearHijo(std::string txt,Usuario* usu){
    Comentario* NuevoComen = new Comentario(txt);
    NuevoComen->tieneComPadre = true;
    NuevoComen->ComenPadre = this;
    NuevoComen->usuario = usu;
    
    
    //NuevoComen->setProdPadre(this->ProdPadre);

    int ID = NuevoComen->getID();
    this->respuestas[ID] = NuevoComen;
    ContUsuario* contUsuari = ContUsuario::getInstanciaContUsu();
    contUsuari->agregarComentario(NuevoComen);
    usu->agregarComen(NuevoComen);
    int nuevoNiv = nivelDeHijo +1;
    NuevoComen->setNivelDeHijo(nuevoNiv);
}

void Comentario::setUsuario(Usuario* usu){
    this->usuario = usu;
}

void Comentario::setNivelDeHijo(int num){
    nivelDeHijo = num;
}

void Comentario::setProdPadre(Producto* prod){
    ProdPadre = prod;
}

Comentario* Comentario::getComPadre(){
    return ComenPadre;
}

std::string Comentario::getTexto(){
    return texto;
};
TFecha* Comentario::getFecha(){
    return fecha;
};

int Comentario::getID(){
    return ID;
};

void Comentario::eliminarRefCom(int ID){
    respuestas.erase(ID);
}

Producto* Comentario::getProducto(){
    return ProdPadre;
}

void Comentario::imprimirComentario(){
    std::string nickk = usuario->getNickname();
    int dia = fecha->getDia();
    int mes = fecha->getMes();
    int anio = fecha->getAnio();
    for (int i = 0; (i != nivelDeHijo); ++i){
        printf("     ");
    }
    std::cout <<"| "<< nickk <<std::endl;
    for (int i = 0; (i != nivelDeHijo); ++i){
        printf("     ");
    }
    printf("| ID: %d       %d/%d/%d \n",ID,dia,mes,anio);
    for (int i = 0; (i != nivelDeHijo); ++i){
        printf("     ");
    }
    std::cout <<"| "<< texto << std::endl;
    for (int i = 0; (i != nivelDeHijo); ++i){
        printf("     ");
    }
    std::cout <<"-----\n"<< std::endl;
}

void Comentario::imprimirComentarioParaElim(){
    std::string nickk = usuario->getNickname();
    int dia = fecha->getDia();
    int mes = fecha->getMes();
    int anio = fecha->getAnio();
    std::cout <<"-----"<<std::endl;
    std::cout <<"| "<< nickk <<std::endl;
    printf("| ID: %d        %d/%d/%d \n",ID,dia,mes,anio);
    std::cout <<"| "<< texto << std::endl;
    std::cout <<"-----\n"<< std::endl;
}

void Comentario::imprimirComenYHijos(){
    imprimirComentario();
    std::map<int, Comentario *>::iterator it;
    for (it= respuestas.begin(); it != respuestas.end(); ++it){
        Comentario* comen = it->second;
        comen->imprimirComenYHijos();
    }
}

bool Comentario::getTieneComPadre(){
    return tieneComPadre;
}

void Comentario::EliminoComentarioYHijos(){
    std::map<int, Comentario *>::iterator it;
    for (it= respuestas.begin(); it != respuestas.end(); ++it){
        Comentario* comHijo = it->second;
        comHijo->EliminoComentarioYHijos();
    }
    usuario->eliminarRefCom(ID);
    respuestas.clear();
    delete this;
}
