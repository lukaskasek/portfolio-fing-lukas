#ifndef TFECHAACTUAL
#define TFECHAACTUAL
#include <iostream>
#include <set>
#include <string>
#include <vector>
#include <memory>
#include <sstream>
#include "Fabrica.h"

class TFechaActual {
    private:
        int Dia;
        int Mes;
        int Anio;
        static TFechaActual * instanciaFecha;
        TFechaActual(int dia, int mes, int anio); //constructor privado pq es singleton
        ~TFechaActual(); //destructor

    public:
        static TFechaActual* getInstanciaFecha(int dia, int mes, int anio);
        int getDia();
        int getMes();
        int getAnio();
        void modificarFecha(int dia, int mes, int anio);
        void imprimirFecha();
};
#include "TFecha.h"
#endif