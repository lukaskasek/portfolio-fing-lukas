#ifndef LEER_H
#define LEER_H
#include <termios.h>
#include <unistd.h>
#include <string>
#include <vector> // Asegúrate de incluir la biblioteca <vector>

extern std::vector<std::string> simulatedInputs;
extern unsigned int inputIndex;  // Cambiado a unsigned int

char leerUnaTecla();
int leerEntero();
std::string leerCadena( );
char getch();

#endif