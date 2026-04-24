#include <iostream>
#include <string>
#include <sstream>
#include <vector>
#include <termios.h>
#include <unistd.h>
#include "leer.h"

std::vector<std::string> simulatedInputs;
unsigned int inputIndex = 0;  // Cambiado a unsigned int

char leerUnaTecla() {
    // Si todavía hay entradas simuladas disponibles
    if (inputIndex < simulatedInputs.size()) {
        std::string entrada = simulatedInputs[inputIndex++];
        return entrada[0]; // Devolvemos la primera letra de la entrada simulada
    } else {
        return getch(); // Leer una tecla desde la entrada estándar
    }
}

char getch() {
    char buf = 0;
    struct termios old = {0};
    if (tcgetattr(0, &old) < 0)
        perror("tcsetattr()");
    old.c_lflag &= ~ICANON;
    old.c_lflag &= ~ECHO;
    old.c_cc[VMIN] = 1;
    old.c_cc[VTIME] = 0;
    if (tcsetattr(0, TCSANOW, &old) < 0)
        perror("tcsetattr ICANON");
    if (read(0, &buf, 1) < 0)
        perror("read()");
    old.c_lflag |= ICANON;
    old.c_lflag |= ECHO;
    if (tcsetattr(0, TCSADRAIN, &old) < 0)
        perror ("tcsetattr ~ICANON");
    return (buf);
}

int leerEntero() {
    if (inputIndex < simulatedInputs.size()) {
        std::string entrada = simulatedInputs[inputIndex++];
        
        // Intentar convertir la entrada a entero
        try {
            return std::stoi(entrada);
        } catch (const std::invalid_argument&) {
            std::cerr << "Error: Entrada no válida para entero: " << entrada << std::endl;
            throw;
        } catch (const std::out_of_range&) {
            std::cerr << "Error: Entrada fuera de rango para entero: " << entrada << std::endl;
            throw;
        }
    } else {
        std::string entrada;
        int numero;
        while (true) {
            std::getline(std::cin, entrada);

            std::istringstream flujoEntrada(entrada);

            if (flujoEntrada >> numero) {
                char resto;
                if (!(flujoEntrada >> resto)) {
                    if (numero>=0){
                    return numero;
                }}
            }
            std::cout << "Entrada no válida. Intente de nuevo." << std::endl;
        }
    }
}

std::string leerCadena() {
    if (inputIndex < simulatedInputs.size()) {
        return simulatedInputs[inputIndex++]; // Devolver la entrada simulada
    } else {
        std::string entrada;

        while (true) {
            std::getline(std::cin, entrada);

            if (!entrada.empty()) {
                return entrada; // Entrada válida
            }

            std::cout << "Entrada no válida. Intente de nuevo." << std::endl;
        }
    }
}