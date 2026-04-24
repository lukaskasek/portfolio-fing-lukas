#ifndef TFECHA
#define TFECHA
#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <memory>
#include <sstream>
#include "Fabrica.h"

class TFecha {
    private:
        int Dia, Mes, Anio;

    public:
        ~TFecha();
        TFecha(int,int,int);
        int getDia();
        int getMes();
        int getAnio();
        void modificarFecha(int dia, int mes, int anio);
        void imprimirFecha();
        bool mayoroIgual(TFechaActual*); 
};
#include "TDireccion.h"
#endif