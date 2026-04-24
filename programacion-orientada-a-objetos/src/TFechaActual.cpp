#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <memory>
#include <sstream>
#include "TFechaActual.h" 


TFechaActual* TFechaActual::instanciaFecha = NULL; 

/*//creador sin parametros
TFechaActual::TFechaActual(){
};*/

TFechaActual::TFechaActual(int dia, int mes, int anio){
    Dia=dia;
    Mes=mes;
    Anio=anio;
};

TFechaActual* TFechaActual::getInstanciaFecha(int dia, int mes, int anio) {
    if (instanciaFecha == NULL) {
        instanciaFecha = new TFechaActual(dia, mes, anio);
    }
    return instanciaFecha;
}

int TFechaActual::getDia() {
    return this->Dia;
};

int TFechaActual::getMes() {
    return this->Mes;
};

int TFechaActual::getAnio() {
    return this->Anio;
};


void TFechaActual::modificarFecha(int diaa, int mess, int anioo) {
    if (diaa > 0 && diaa <= 31 && mess > 0 && mess <= 12 && anioo > 0) {
        this->Dia = diaa;
        this->Mes = mess;
        this->Anio = anioo;
        std::cout << "Fecha modificada a: " << Dia << "/" << Mes << "/" << Anio << std::endl;
    } 
    else {
        std::cout << "Fecha no valida"<< std::endl; //cerr es para imprimir errores 
    }

};

void TFechaActual::imprimirFecha(){
    std::cout << "Fecha: " << Dia << "/" << Mes << "/" << Anio << std::endl;
};

