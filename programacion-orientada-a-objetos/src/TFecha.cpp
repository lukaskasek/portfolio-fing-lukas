#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <memory>
#include <sstream>
#include "TFecha.h" 

TFecha::~TFecha(){}

TFecha::TFecha(int Dia, int Mes, int Anio){
    this->Dia=Dia;
    this->Mes=Mes;
    this->Anio=Anio;
};

int TFecha::getDia() {
    return this->Dia;
};

int TFecha::getMes() {
    return this->Mes;
};

int TFecha::getAnio() {
    return this->Anio;
};


void TFecha::modificarFecha(int dia, int mes, int anio) {
    if (dia > 0 && dia <= 31 && mes > 0 && mes <= 12 && anio > 0) {
        this->Dia = dia;
        this->Mes = mes;
        this->Anio = anio;
        std::cout << "Fecha modificada a: " << dia << "/" << mes << "/" << anio << std::endl;
    } else {
        std::cerr << "Fecha no válida." << std::endl; //cerr es para imprimir errores 
    }

};

void TFecha::imprimirFecha(){
    std::cout << Dia << "/" << Mes << "/" << Anio << std::endl;
};

bool TFecha::mayoroIgual(TFechaActual* Fecha) {
    if (this->Anio > Fecha->getAnio()){
        return true;
    }
    else if (this->Anio == Fecha->getAnio()){
        if(this->Mes > Fecha->getMes()){
            return true;
        }
        else if (this->Mes == Fecha->getMes()){
            if(this->Dia >= Fecha->getMes()){
                return true;
            }
            else return false;
        }
        else return false;
    }
    else return false;
}