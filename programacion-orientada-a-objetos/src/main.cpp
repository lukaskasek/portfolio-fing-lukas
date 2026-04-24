#include <cstdio>  // Para printf y scanf
#include <iostream> // Para std::cin y std::getline
#include <map>
#include <cstdlib>
#include <set>
#include "Fabrica.h"
#include "IContProducto.h"
#include "IContUsuario.h"
#include "Cliente.h"
#include "Usuario.h"
#include "Vendedor.h"
#include "DataUsuario.h"
#include "DataCliente.h"
#include "DataVendedor.h"
#include "ContProducto.h"
#include "ContUsuario.h"
#include "leer.h"
#include "Compra.h"
#include "ContProducto.h"
#include "ContUsuario.h"
#include "TFecha.h"
#include "Producto.h"
#include "TFechaActual.h"


int main(){
//asigno cosas iniciales, creo controladores e interfaces, agrego colecciones (diccionarios e interfaces)




//con fabrica
Fabrica* fabrica = Fabrica::getInstancia();
IContUsuario* contUsuu = fabrica->getIContUsuario();
IContProducto* contProduu = fabrica->getIContProducto();
ContUsuario* contUsu = dynamic_cast<ContUsuario*>(contUsuu);
ContProducto* contProdu = dynamic_cast<ContProducto*>(contProduu);

//ContUsuario* contUsu = ContUsuario::getInstanciaContUsu();
//ContProducto* contProdu = ContProducto::getInstanciaContProd();
//TFechaActual* fechaSist= TFechaActual::getInstanciaFecha();

TFechaActual* fechaSist = TFechaActual::getInstanciaFecha(28,06,2023); //la instancia de fecha actual

std::string nick;
std::string Contrasena;
TFecha* fecha;
bool e = true;


while(e) {
    std::system("clear");
    printf("      **MERCADO FING**\n");
    printf("            //MENU//\n\n");
    int TamañoColUsuarios = contUsu->sizeCol();
    printf("Cantidad usuarios: %d\n\n", TamañoColUsuarios);
    printf("presione:\n\n");
    printf("a: para acceder a los casos de uso\n");
    printf("b: para acceder a los casos de prueba precargados\n");
    printf("x: para salir\n");

    char tec = leerUnaTecla();
    switch(tec) {
    case 'a':{
    std::system("clear");
    printf(" --CASOS DE USO--\n\n");
    printf("presione:\n\n");
    printf("a: para dar de alta a un usuario\n");
    printf("b: Listado de usuarios\n");
    printf("c: Alta de producto\n");
    printf("d: Consultar producto\n");
    printf("e: Crear promoción\n");
    printf("f: Consultar promoción\n");
    printf("g: Realizar compra\n");
    printf("h: Dejar comentario\n");
    printf("i: Eliminar comentario\n");
    printf("j: Enviar producto\n");
    printf("k: Expediente de Usuario\n");
    printf("l: Suscribirse a notificaciones\n");
    printf("m: Consulta de notificaciones\n");
    printf("n: Eliminar suscripciones\n");
    printf("o: Modificar la fecha\n");
    printf("p: Obtener fecha del sistema\n");
    printf("x: para volver al menu\n");

    char tecla = leerUnaTecla();
    
    switch(tecla) {
        case 'a':{
            std::system("clear");
            printf("\nOpción 'a' seleccionada: Dar de alta a un usuario.\n");
            printf("\nPresione:\na: para ingresar un cliente\ncualquier otra tecla: para ingresar un vendedor\n");
            char venOcli = leerUnaTecla();
            printf("\nIngresar Nickname usuario\n");
            nick = leerCadena();
            std::system("clear");
            if (contUsu->estaUsuario(nick)){
                printf("\nError! Ese nombre de usuario ya existe\nPresione cualaquier tecla para volver al menu\n");
                leerUnaTecla();
                break;
            }
            printf("\nIngresar Contrasena usuario\n");
            Contrasena = leerCadena();
            std::system("clear");
            //std::cin.ignore();
            //std::getline(std::cin, Contrasena);
            printf("\nIngresar ano de nacimiento de usuario\n");
            int ano = leerEntero();
            std::system("clear");
            printf("\nIngresar mes de nacimiento de usuario\n");
            int mes = leerEntero();
            std::system("clear");
            printf("\nIngresar dia de nacimiento de usuario\n");
            int dia = leerEntero();
            std::system("clear");

            fecha = new TFecha(dia, mes, ano);
            if(venOcli == 'a'){
                printf("\nIngresar ciudad de cliente\n");
                std::string ciudad = leerCadena();
                std::system("clear");
                printf("\nIngresar calle de cliente\n");
                std::string calle  = leerCadena();
                std::system("clear");
                printf("\nIngresar numero de puerta de cliente\n");
                int numero = leerEntero();
                std::system("clear");
                TDireccion* direccion = new TDireccion(calle, numero);
                DataCliente* data = new DataCliente(nick,Contrasena,*fecha,*direccion,ciudad);
                contUsu->ingresarDatosCliente(*data);
            }
            else{
                printf("\nIngresar RUT de vendedor\n");
                std::string RUT = leerCadena();
                
                std::system("clear");
                DataVendedor* data = new DataVendedor(nick,Contrasena,*fecha,RUT);
                contUsu->ingresarDatosVendedor(*data);
            }
            
            break;
        }
        case 'b':{
            std::system("clear");
            printf("\nListado de usuarios:\n");
            contUsu->imprimirUsuarios();
            printf("\nPresione cualquier tecla para ir al menu\n");
            leerUnaTecla();
            break;
        }

        case 'c':{
            printf("\nOpción 'c' seleccionada: Alta de producto.\n");
            contUsu->imprimirNickVendedores();
            printf("\nIngrese el nickname del vendedor que desea seleccionar.\n");
            std::string nickVend = leerCadena();
            std::system("clear");
            std::map<std::string,Vendedor *>::iterator iter;
            if (contUsu->findVend(nickVend) == NULL) {
                printf("\nError: No existe un vendedor con dicho nickname\nPresione cualquier tecla para volver al menu\n");
                leerUnaTecla();
            } else {
                char catProd = 'z';
                printf("\nIngrese el nombre del producto:\n");
                std::string nomProd = leerCadena();
                std::system("clear");
                printf("\nIngrese el precio del producto:\n");
                int precioProd = leerEntero();
                std::system("clear");
                printf("\nIngrese la cantidad en stock del producto:\n");
                int stockProd = leerEntero();
                std::system("clear");
                printf("\nIngrese la descripcion del producto:\n");
                std::string descProd = leerCadena();
                std::system("clear");
                while (catProd != 'c' && catProd != 'b' && catProd != 'a'){
                printf("\nPresione la tecla correspondiente a la categoria es ropa, electrodomesticos, otros:\n");
                printf("\na: ropa");
                printf("\nb: electrodomestico");
                std::cout <<"c:otros"<< std::endl;
                catProd = leerUnaTecla();
                std::system("clear");
                }
                Producto* nuevoProd = new Producto(stockProd, precioProd, nomProd, descProd, catProd);
                Vendedor* vend=contUsu->buscarVendPorNombre(nickVend);
                vend->agregarProducto(nuevoProd);
                nuevoProd->asociarVendedor(vend);
                contProdu->insertarProducto(nuevoProd);
            };
         
            break;}
        case 'd':{ 
            printf("\nOpción 'd' seleccionada: Consultar producto.\n");
            std::system("clear");
            contProdu->listarProductos();
            printf("\nIngrese el codigo del producto a seleccionar:\n");
            Producto* prod = NULL;
            while (prod == NULL){
                int codProd = leerEntero();
                prod = contProdu->find(codProd);
            }
            std::system("clear");
            prod->imprimirProducto();
            leerUnaTecla();
            break;
         }
        case 'e':{
            printf("\nOpción 'e' seleccionada: Crear promoción.\n");
            std::string nom;
            std::cout << "Ingrese el nombre de la promoción: ";
            nom=leerCadena(); 
            std::string descrip;
            std::cout << "Ingrese la descripción de la promoción: ";
            descrip=leerCadena();
            printf("\nIngresar anio de vencimiento de la promocion\n");
            int a = leerEntero();
            printf("\nIngresar mes de vencimiento de la promocion\n");
            int m = leerEntero();
            printf("\nIngresar dia de vencimiento de la promocion\n");
            int d = leerEntero();
            TFecha* fech=new TFecha(d,m,a);
            int descu;
            std::cout << "Ingrese el porcentaje de descuento que se va a aplicar en la promocion ";
            descu=leerEntero();
            contUsu->imprimirNickVendedores();
            printf("\nIngrese el nombre del vendedor al que quiere asignar la promocion.\n");
            std::string vend;
            vend=leerCadena();
            Vendedor* vnd=contUsu->buscarVendPorNombre(vend);
            vnd->imprimirProdsVendedorCodNom();
            Promocion* p=new Promocion(nom,descrip,fech,descu);
            vnd->agregarPromocion(p);
            p->asociarVendedor(vnd);
            bool seguir=true;
            while(seguir){
                printf("\nIngrese el codigo de un producto que desea agregar a la promocion.\n");
                int c=leerEntero();
                Producto* prod=contProdu->buscarProducto(c);
                printf("\nIngrese la cantidad minima de este producto para aplicar la promocion.\n");
                int cantmin=leerEntero();
                p->agregarProdAPromo(prod);
                //asociar el producto a la promo
                prod->setPromo(p);
                p->agregarProdAPromoCantMin(prod,cantmin);
                printf("\n¿Desea seguir agregando productos? (s/n): .\n");
                char respuesta;
                respuesta=leerUnaTecla();
                seguir = (respuesta == 's' || respuesta == 'S');
                seguir = !(respuesta == 'n' || respuesta == 'N');
            };
            contProdu->agregarPromocion(p);
            TNotificacion* notificacion=new TNotificacion(vend,p->getNombre(),p->getProductos());
            vnd->notificarClientes(notificacion);
            break;
        }
        case 'f':{
            printf("\nOpción 'f' seleccionada: Consultar promoción.\n");
                      contProdu->listarPromosVigentes();
            printf("\nSi desea seleccionar una promoción ingrese 's', de lo contrario ingrese 'n'\n");
            char tec=leerUnaTecla();
            if(tec == 's' || tec == 'S'){
                printf("\nIngrese el nombre la promocion\n");
                std::string nom = leerCadena();
                Promocion* promo = contProdu->buscarPromoPorNombre(nom);
                promo->devolverDatosProdsPromo();
                printf("\nPulse cualquier tecla para continuar\n");
                leerUnaTecla();
            }
        break;
        }
        case 'g': {
            printf("\nOpción 'g' seleccionada: Realizar compra.\n");
            dynamic_cast<ContUsuario*>(contUsu)->imprimirNickClientes();//detalle
            printf("\nIngrese el nickname del cliente que desea seleccionar.\n");
            std::string nickCliente = leerCadena();
            auto iterC = dynamic_cast<ContUsuario*>(contUsu)->buscarClientePorNombre(nickCliente);
            if (iterC == NULL) {
                printf("\nError: No existe un cliente con dicho nickname\n");
                leerUnaTecla();
            } else {
                static int idCompra = 0;
                Compra* compra = new Compra(fechaSist, 0, ++idCompra);
                dynamic_cast<ContProducto*>(contProdu)->listarProductosDisp(); 
                printf("\nIngrese 0 si desea agregar productos a la compra, de lo contrario ingrese otro numero\n");
                int agregar = leerEntero();
                std::map<int, CompraProd*> comprasPro; //se busca por el codigo del producto que tiene
                while (agregar == 0){
                    printf("\nIngrese el codigo del producto a agregar\n");
                    int codP = leerEntero();
                    auto iterProd = contProdu->buscarProducto(codP);
                    if (iterProd == NULL) {
                        printf("\nError: No existe un producto con dicho codigo\n");
                        leerUnaTecla();
                    } else {
                        if (comprasPro.find(codP) != comprasPro.end()){ 
                            printf("\nEste producto ya fue ingresado\n");
                            leerUnaTecla();
                        } else {
                            printf("\nIngrese la cantidad que desea comprar\n");
                            int cantP = leerEntero();
                            if (cantP > 0) {
                                if (cantP <= iterProd->getStock()) {
                                    CompraProd* compraP = new CompraProd(cantP, false, iterProd);
                                    comprasPro[codP]= compraP; 
                                    iterProd->ingresarCompraProd(compraP); //asociar de prod a compraProd
                                    compra->asociarCompraProd(compraP, codP); //asociar de Compra a compraProd
                                    compraP->setCompraAsociada(compra); 
                                    int idCompra = compra->getId();
                                    iterProd->asociarCompraProd(compraP, idCompra); //asociar de Producto a compraProd
                                    //asociar el prod al map de productos de compra
                                    compra->agregarProducto(iterProd, codP);
                                    int precio = iterProd->getPrecio(); 
                                    precio = compra->aplicarDescuento(precio, cantP, codP, iterProd);
                                    precio = precio * cantP;
                                    compra->sumarAlMonto(precio);
                                    iterProd->setStock(iterProd->getStock()-cantP); //actualizo el stock del producto
                                } else {
                                    printf("\nError: Cantidad ingresada menor a cantidad en stock\n");
                                    leerUnaTecla();
                                }
                            } else {
                                printf("\nLa cantidad debe ser positiva\n");
                                leerUnaTecla();
                            }
                        }
                    }
                    printf("\nIngrese 0 si desea agregar mas productos a la compra, de lo contrario ingrese otro numero\n");
                    agregar = leerEntero();
                }
                compra->imprimirCompraCompleto();
                printf("\nPresiona 0 para confirmar la compra\n");
                int a = leerEntero();
                if (a == 0) {
                    Cliente* cli=contUsu->buscarClientePorNombre(nickCliente);
                    compra->asociarCliente(cli); //asocio el cliente con la compra
                    printf("El cliente asociado a la compra es: ");
                    std::string nombre = compra->getClienteAsociado()->getNickname();
                    std::cout << nombre << std::endl;
                    cli->agregarCompra(compra); //asocio la compra al cliente
                    leerUnaTecla();
                    //Hay que agregar la compra a la coleccion de compras no?
                    //contProdu->agregarCompra(compra); DA PROBLEMAS CON LOS INCLUDE, ME DA MIEDO
                } 
            }
            printf("Ingrese una tecla para volver al menu");
            leerUnaTecla();
            break;
        }
        case 'h':{
            std::system("clear");
            printf("\nOpción 'h' seleccionada: Dejar comentario.\n");
            contUsu->imprimirNicknamesUsu();
            printf("\n\nIngrese el nickname del usuario que desea seleccionar\n");
            std::string nickUsuario = leerCadena();
            std::system("clear");
            if (!contUsu->estaUsuario(nickUsuario)) {
                std::cout << "\nError: No existe un usuario con dicho nickname\n\n\n    ---precione cualquier tecla para ir al menu---" << std::endl;
                leerUnaTecla();
            } else {
              Usuario* usu = contUsu->find(nickUsuario);
              contProdu->listarProductos();//implementar
              std::cout << "\n\nAhora ingrese el codigo identificador del producto que desea seleccionar\n" << std::endl;
              int codigoProd = leerEntero();
              std::system("clear");
              if (!contProdu->estaProd(codigoProd)) {
                    std::cout << "\n\nError: No existe un producto con dicho codigo\n\n\n    ---precione cualquier tecla para ir al menu---" << std::endl;
                    leerUnaTecla();
                } else {
                    Producto* prod = contProdu->find(codigoProd);
                    bool boleano = true;
                    bool primeraVez = false;
                    while (boleano){
                        std::system("clear");
                        if (primeraVez){std::cout << "\nTecla no valida, intente nuevamente\n\n" << std::endl;;}
                        primeraVez = true;
                        printf("\n¿Desea hacer un comentario nuevo (N) o una respuesta (R)?\n");
                        char tipoComentario=leerUnaTecla();
                        if (tipoComentario == 'N' || tipoComentario == 'n') {
                            std::system("clear");
                            printf("\nIngrese el texto de su comentario:\n");
                            std::string textoComentario = leerCadena();
                            prod->crearComentario(textoComentario, usu);
                            printf("\nComentario agregado exitosamente.\n");
                            leerUnaTecla();
                            boleano = false;
                        } else if (tipoComentario == 'R' || tipoComentario == 'r') {
                            std::system("clear");
                            printf("\nComentarios del producto:\n");
                            prod->imprimirComDeProd(); 
                            printf("\nElija el ID del comentario al que desea responder:\n");
                            int IDComentario = leerEntero();
                            if (!prod->estaComen(IDComentario)){
                                std::cout << "\n\nError: No existe un comentario con dicho ID\n\n\n    ---precione cualquier tecla para ir al menu---" << std::endl;
                                leerUnaTecla();
                                }
                            else{
                                std::system("clear");
                                printf("\nIngrese el texto de su respuesta:\n");
                                std::string textoRespuesta = leerCadena();
                                Comentario* comenPadre = contUsu->findComen(IDComentario);
                                comenPadre->CrearHijo(textoRespuesta, usu);
                                leerUnaTecla();
                                boleano = false;
                            }
                        }
                    }
                }
             }
            break;}
        
        case 'i':{
            printf("\nOpción 'i' seleccionada: Eliminar comentario.\n");
            contUsu->imprimirNicknamesUsu();
            std::cout << "\n\nEscriba el nickName del usuario correspondiente al comentario a elminar" << std::endl;
            std::string nick = leerCadena();
            if (!contUsu->estaUsuario(nick)) {
                std::cout << "\nError: No existe un usuario con dicho nickname\n\n\n    ---precione cualquier tecla para ir al menu---" << std::endl;
                leerUnaTecla();
            }
            else{
                std::system("clear");
                Usuario* usu = contUsu->find(nick);
                usu->imprimirComentarios();
                printf("\n\nEscribe el ID del comentario a elminar (se eliminaran todas sus respuestas)\n");
                int ID = leerEntero();
                if (!usu->estaComen(ID)){
                    std::cout << "\nError: Este usuario no tiene ningun comentario con este ID\n\n\n    ---precione cualquier tecla para ir al menu---" << std::endl;
                    leerUnaTecla();
                }
                std::system("clear");
                Comentario* comen = contUsu->findComen(ID);
                bool tienePadre = comen->getTieneComPadre();
                if (tienePadre){
                    Comentario* comPadre = comen->getComPadre();
                    comPadre->eliminarRefCom(ID);
                }
                else {
                    Producto* prod = comen->getProducto();
                    prod->eliminarRefComen(ID);
                }
                comen->EliminoComentarioYHijos();
                }
            break;
        }
        case 'j': { 
            //ver casos borde
            printf("\nOpción 'j' seleccionada: Enviar producto.\n");

            contUsu->imprimirNickVendedores(); //solo los nicknames CON FUNCION DE GUANI
            printf("\nIngrese el nombre del vendedor que quiere seleccionar\n");
            std::string vend=leerCadena();  
            Vendedor* vnd=contUsu->buscarVendPorNombre(vend);
            while(vnd == NULL){
                std::cout << "No existe un vendedor con ese nombre, ingrese un nombre valido.\n" << std::endl;
                vend=leerCadena();  
                vnd=contUsu->buscarVendPorNombre(vend);
            }
            bool vacio = false;
            std::set<int> codsProds;
            vnd->imprimirProdsConCompraPendDeEnvio(vacio, codsProds);
            if (!vacio) {
                printf("\nIngrese el codigo del producto que quiere seleccionar\n");
                int prod = leerEntero();
                bool seguir = true;
                while(seguir){
                    if (codsProds.find(prod) != codsProds.end()){ 
                        Producto* produ = contProdu->buscarProducto(prod); 
                        std::set<int> idsCompras;
                        contProdu->imprimirComprasConProdPendiente(produ, idsCompras);
                        printf("\nIngrese el id de la compra que quiere seleccionar\n");
                        int id=leerEntero();
                        bool seg = true;
                        while(seg){
                            if(idsCompras.find(id) != idsCompras.end()){
                                produ->findCompraProd(id)->setEnviado(true);
                                std::cout << "El producto fue enviado correctamente.\n" << std::endl;
                                seguir = false;
                                seg = false;
                                leerUnaTecla();
                            }else{
                                printf("El id es incorrecto, ingrese uno que este en la lista\n");
                                id= leerEntero();
                            }
                        }
                        //corroborar qye el id sea uno de losque se muestra, y que no haya sido enviado ya
                    }else{
                        printf("El producto es incorrecto, ingrese uno que este en la lista\n");
                        prod= leerEntero();
                    }
                }
            } else {
                std::cout << "El vendedor no tiene productos con envios pendientes." << std::endl;
                leerUnaTecla();
            }
            break;            

        }
        case 'k': {
            printf("\nOpción 'k' seleccionada: Expediente de Usuario.\n");
            contUsu->imprimirUsuarios(); // Imprimir todos los usuarios
            printf("\nIngrese el nombre del usuario que quiere seleccionar\n");
            std::string nick;
            nick=leerCadena();
            Usuario* usr = contUsu->buscarPorNombre(nick);
            if (usr == nullptr) {
                printf("Error: No existe un usuario con ese nombre.\n");
                break;
            }
            printf("Información básica del usuario:\n");
            std::cout<<"Nickname:"<<usr->getNickname()<< std::endl;
            usr->imprimirFecha(); // info basica: nickname y fecha. sin contraseña ciudad ni nada de eso
            if (usr->esVendedor()) {
                Vendedor* vendedor = dynamic_cast<Vendedor*>(usr);
                printf("El usuario es un vendedor.\n");
                printf("Productos en venta:\n");
                vendedor->listarProductosEnVenta();
                printf("Promociones vigentes:\n");
                vendedor->listarPromocionesVigentes();
                printf("\nPulse cualquier tecla para continuar\n");
                leerUnaTecla(); 
            } else {
                Cliente* cliente = dynamic_cast<Cliente*>(usr);
                printf("El usuario es un cliente.\n");
                printf("Compras realizadas:\n");
                cliente->listarComprasRealizadas();
                printf("\nPulse cualquier tecla para continuar\n");
                leerUnaTecla();
            }
            break;
        }
        case 'l': {
            printf("\nOpción 'l' seleccionada: Suscribirse a notificaciones.\n");
            dynamic_cast<ContUsuario*>(contUsu)->imprimirNickClientes();
            printf("\nIngrese el nombre del Cliente que quiere seleccionar\n");
            std::string cli=leerCadena();
            //buscar el cliente y obtenerlo para usarlo de parametro
            Cliente* cliente=contUsu->buscarClientePorNombre(cli);
            //el Sistema devuelve la lista de todos los vendedores a los que no está suscrito el cliente
           
            bool seguir=true;
            while(seguir){
                contUsu->imprimirNickVendedores();
                printf("\nIngrese el nickname del vendedor al que desea suscribirse.\n");
                std::string c=leerCadena();
                Vendedor* vnd=contUsu->buscarVendPorNombre(c);
                cliente->agregarSuscripcion(vnd);
                vnd->agregarSuscriptor(cliente);
                printf("\n¿Desea agregar otra suscripcion? (s/n): .\n");
                char respuesta;
                respuesta=leerUnaTecla();
                seguir = (respuesta == 's' || respuesta == 'S');
            };
            break;
        }
        case 'm': {
            printf("\nOpción 'm' seleccionada: Consulta de notificaciones.\n");
            dynamic_cast<ContUsuario*>(contUsu)->imprimirNickClientes();
            printf("\nIngrese el nombre del Cliente que quiere seleccionar\n");
            std::string cli=leerCadena();
            Cliente* cliente = dynamic_cast<Cliente*>(contUsu->buscarPorNombre(cli));
            if (cliente == nullptr) {
                printf("Error: No existe un cliente con ese nombre.\n");
                break;
            }
            // Consultar y listar notificaciones del cliente
            cliente->consultarNotificaciones();
            printf("\nPulse cualquier tecla para continuar\n");
            leerUnaTecla();
            break;
        }
         case 'o':{
            printf("\nOpción 'o' seleccionada: Modificar fecha.\n");
            int anioo, mess, diaa;
                do {
                    printf("\nIngresar el año nuevo (mayor o igual a 0):\n");
                    anioo = leerEntero();
                    if (anioo < 0) {
                        printf("Error: Año inválido. Por favor ingrese un año mayor o igual a 0.\n");
                    }
                } while (anioo < 0);
                do {
                    printf("\nIngresar el mes nuevo (1-12):\n");
                    mess = leerEntero();
                    if (mess < 1 || mess > 12) {
                        printf("Error: Mes inválido. Por favor ingrese un mes entre 1 y 12.\n");
                    }
                } while (mess < 1 || mess > 12);
                do {
                    printf("\nIngresar el día nuevo (1-31):\n");
                    diaa = leerEntero();
                    if (diaa < 1 || diaa > 31) {
                        printf("Error: Día inválido. Por favor ingrese un día entre 1 y 31.\n");
                    }
                } while (diaa < 1 || diaa > 31);
                TFechaActual* fechaaa = TFechaActual::getInstanciaFecha(1,1,1);
                fechaaa->modificarFecha(diaa, mess, anioo);
                std::cout << "\n\n  --Precione cualquier tecla para volver al menu--" << std::endl;
                leerUnaTecla();
            break;
        } 
        case 'p':{
            printf("\nOpcion 'p' seleccionada:Obtener fecha del sistema\n");
            printf("\nLa fecha actual del sistema es");
            TFechaActual* fechaActual = TFechaActual::getInstanciaFecha(1,1,1);
            fechaActual->imprimirFecha();
            std::cout << "\n\n  --Precione cualquier tecla para volver al menu--" << std::endl;
            leerUnaTecla();
            break; 
        }
        case 'n': {
            printf("\nOpción 'n' seleccionada: Eliminar suscripciones.\n");
            dynamic_cast<ContUsuario*>(contUsu)->imprimirNickClientes();
            printf("\nIngrese el nombre del Cliente que quiere seleccionar\n");
            std::string cli=leerCadena();
            Cliente* cliente = dynamic_cast<Cliente*>(contUsu->buscarPorNombre(cli));
            contUsu->listarVendSuscripto(cliente);
            bool seguir=true;
            while(seguir){
                printf("\nIngrese el nickname del vendedor al que desea desuscribirse.\n");
                std::string c=leerCadena();
                Vendedor* vnd=contUsu->buscarVendPorNombre(c);
                cliente->eliminarSuscripcion(vnd);
                vnd->eliminarSuscriptor(cliente);
                printf("\n¿Desea eliminar otra suscripcion? (s/n): .\n");
                char respuesta;
                respuesta=leerUnaTecla();
                seguir = (respuesta == 's' || respuesta == 'S');
            };
            break;
        }
        case 'x':{
            break;}
        default:{
            printf("\nTecla no válida. Por favor, seleccione una opción válida.\n");
            break;}
    }

    break;}
    case 'b':{
        std::system("clear");
        printf(" --CASOS DE PRUEBA--\n\n");
        printf("presione:\n\n");
        printf("a: agregar usuarios\n");
        printf("b: agregar productos\n");
        printf("c: agregar Comentarios\n");
        printf("d: agregar promociones\n");
        printf("e: agregar compra\n");
        printf("x: para volver al menu\n");
        char casoPrueba = leerUnaTecla();
        switch(casoPrueba) {
            case 'a':{
            inputIndex = 0;
            simulatedInputs = {
                // Usuario 1
                "a", 
                "a",                // Opción 'a' seleccionada: Dar de alta a un usuario.
                "v",                // Tecla para seleccionar 'v' (vendedor).
                "ana23",            // Nickname
                "qwer1234",         // Contraseña
                "1988",             // Año
                "05",               // Mes
                "15",               // Día
                "212345678001",     // RUT

                // Usuario 2
                "a", 
                "a",                // Opción 'a' seleccionada: Dar de alta a un usuario.
                "v",                // Tecla para seleccionar 'v' (vendedor).
                "carlos78",         // Nickname
                "asdfghj",          // Contraseña
                "1986",             // Año
                "06",               // Mes
                "18",               // Día
                "356789012345",     // RUT

                // Usuario 3
                "a", 
                "a",                // Opción 'a' seleccionada: Dar de alta a un usuario.
                "v",                // Tecla para seleccionar 'v' (vendedor).
                "diegom",           // Nickname
                "zxcvbn",           // Contraseña
                "1993",             // Año
                "07",               // Mes
                "28",               // Día
                "190123456789",     // RUT

                // Usuario 4
                "a", 
                "a",                // Opción 'a' seleccionada: Dar de alta a un usuario.
                "a",                // Tecla para seleccionar 'a' (cliente).
                "juan87",           // Nickname
                "1qaz2wsx",         // Contraseña
                "1992",             // Año
                "10",               // Mes
                "20",               // Día
                "Melo",             // Ciudad
                "Av. 18 de Julio",  // Calle
                "456",              // Número de puerta

                // Usuario 5
                "a", 
                "a",                // Opción 'a' seleccionada: Dar de alta a un usuario.
                "a",                // Tecla para seleccionar 'a' (cliente).
                "laura",            // Nickname
                "3edc4rfv",         // Contraseña
                "1979",             // Año
                "09",               // Mes
                "22",               // Día
                "Montevideo",       // Ciudad
                "Rondeau",          // Calle
                "1617",             // Número de puerta

                // Usuario 6
                "a", 
                "a",                // Opción 'a' seleccionada: Dar de alta a un usuario.
                "v",                // Tecla para seleccionar 'v' (vendedor).
                "maria01",          // Nickname
                "5tgb6yhn",         // Contraseña
                "1985",             // Año
                "03",               // Mes
                "25",               // Día
                "321098765432",     // RUT

                // Usuario 7
                "a", 
                "a",                // Opción 'a' seleccionada: Dar de alta a un usuario.
                "a",                // Tecla para seleccionar 'a' (cliente).
                "natalia",          // Nickname
                "poiuyt",           // Contraseña
                "1982",             // Año
                "04",               // Mes
                "14",               // Día
                "Salto",            // Ciudad
                "Paysandú",         // Calle
                "2021",             // Número de puerta

                // Usuario 8
                "a", 
                "a",                // Opción 'a' seleccionada: Dar de alta a un usuario.
                "a",                // Tecla para seleccionar 'a' (cliente).
                "pablo10",          // Nickname
                "lkjhgv",           // Contraseña
                "1995",             // Año
                "11",               // Mes
                "30",               // Día
                "Mercedes",         // Ciudad
                "Av. Rivera",       // Calle
                "1819",             // Número de puerta

                // Usuario 9
                "a", 
                "a",                // Opción 'a' seleccionada: Dar de alta a un usuario.
                "a",                // Tecla para seleccionar 'a' (cliente).
                "roberto",          // Nickname
                "mnbvcx",           // Contraseña
                "1990",             // Año
                "08",               // Mes
                "12",               // Día
                "Montevideo",       // Ciudad
                "Av. Brasil",       // Calle
                "1011",             // Número de puerta

                // Usuario 10
                "a", 
                "a",                // Opción 'a' seleccionada: Dar de alta a un usuario.
                "v",                // Tecla para seleccionar 'v' (vendedor).
                "sofia25",          // Nickname
                "1234asdf",         // Contraseña
                "1983",             // Año
                "12",               // Mes
                "07",               // Día
                "445678901234"      // RUT
            };
        break;}
        case 'b':{
            inputIndex = 0;
            simulatedInputs = {
                "a",
                "c",
                "carlos78",
                "Camiseta Azul",
                "1400",
                "50",
                "Camiseta de poliester, color azul",
                "a",

                "a",
                "c",
                "ana23",
                "Televisor LED",
                "40500",
                "30",
                "Televisor LED 55 pulgadas",
                "b",

                "a",
                "c",
                "carlos78",
                "Chaqueta de Cuero",
                "699.99",
                "20",
                "Chaqueta de cuero, color negro",
                "a",

                "a",
                "c",
                "ana23",
                "Microondas Digital",
                "1199.99",
                "15",
                "Microondas digital, 30L",
                "b",

                "a",
                "c",
                "diegom",
                "Luz LED",
                "599.99",
                "40",
                "Luz Bluetooth LED",
                "c",

                "a",
                "c",
                "carlos78",
                "Pantalones Vaqueros",
                "60",
                "25",
                "Pantalones vaqueros, talla 32",
                "a",

                "a",
                "c",
                "diegom",
                "Auriculares Bluetooth",
                "199.99",
                "35",
                "Auriculares bluethooth para celular",
                "c",

                "a",
                "c",
                "ana23",
                "Refrigerador",
                "15499",
                "10",
                "Refrigerador de doble puerta",
                "b",

                "a",
                "c",
                "ana23",
                "Cafetera",
                "23000",
                "50",
                "Cafetera de goteo programable",
                "b",

                "a",
                "c",
                "carlos78",
                "Zapatillas Deportivas",
                "5500",
                "20",
                "Zapatillas para correr, talla 42",
                "a",

                "a",
                "c",
                "carlos78",
                "Mochila",
                "9000",
                "30",
                "Mochila de viaje, 40L",
                "c",

                "a",
                "c",
                "diegom",
                "Plancha de Ropa",
                "2534",
                "25",
                "Plancha a vapor, 1500W",
                "b",

                "a",
                "c",
                "sofia25",
                "Gorra",
                "200",
                "50",
                "Gorra para deportes, color rojo",
                "a",

                "a",
                "c",
                "diegom",
                "Tablet",
                "15000",
                "15",
                "Tablet Android de 10 pulgadas",
                "b",

                "a",
                "c",
                "sofia25",
                "Reloj de Pared",
                "150.50",
                "20",
                "Reloj de pared vintage",
                "c",

            };
            break;}
        case 'c':{
            TFechaActual* fecha = TFechaActual::getInstanciaFecha(1,1,1);
            fecha->modificarFecha(01,06,2024);
            inputIndex = 0;
            simulatedInputs = {
                "a",
                "h",
                "juan87",
                "1",
                "n",
                "¿La camiseta azul esta disponible en talla M?",
                "n",
                

                "a",
                "h",
                "carlos78",
                "1",
                "r",
                "1",
                "Si, tenemos la camiseta azul en talla M",
                "n",

                "b",
                "t",
            };
            break;}
        case 'e':{
            inputIndex = 0;
            simulatedInputs = {
                
                //CO1
                "a",
                "o",//modifica la fecha del sistema
                "2024",
                "5",
                "1", //termina de modificar fecha del sistema
                "a", //casos de uso
                "g", //realizar compra
                "juan87",
                "0",
                "2", //prod cod 2
                "2", //cant 2
                //TIENE QUE ESTAR ENVIADO
                "0",
                "4",
                "1",
                "0",
                "8",
                "1",
                "9", //no quiero agregar mas productos
                "0",
                "x",
                //ENVIO PRODUCTOSS
                "a",
                "j",
                "carlos78", //vendedor
                "2", //cod producto
                "1", //id compra
                "x", //leer una



                //CO2
                "a",
                "o",//modifica la fecha del sistema
                "2024",
                "5",
                "1", //termina de modificar fecha del sistema
                "a", //casos de uso
                "g", //realizar compra
                "juan87",
                "0",
                "5", //prod cod
                "1", //cant
                //TIENE QUE ESTAR ENVIADO
                "9", //no quiero agregar mas prod
                "0", //confirma la compra
                "x",
                //ENVIO PRODUCTOSS
                "a",
                "j",
                "diegom",
                "5",
                "2",
                "x",


                //CO3
                "a",
                "o",//modifica la fecha del sistema
                "2024",
                "5",
                "15", //termina de modificar fecha del sistema
                "a", //casos de uso
                "g", //realizar compra
                "laura",
                "0",
                "14", //prod cod
                "10", //cant 
                //TIENE QUE ESTAR ENVIADO
                "9", //no quiero mas prod
                "0",
                "x",
                //ENVIO PRODUCTOSS
                "a",
                "j",
                "diegom", //vendedor
                "14", //cod producto
                "3", //id compra
                "x",




                //CO4
                "a",
                "o",//modifica la fecha del sistema
                "2024",
                "4",
                "25", //termina de modificar fecha del sistema
                "a", //casos de uso
                "g", //realizar compra
                "natalia",
                "0",
                "11", //prod cod 
                "1", //cant 
                //TIENE QUE ESTAR ENVIADO
                "0",
                "12",
                "1",
                //TIENE QUE ESTAR ENVIADO
                "0",
                "13",
                "1",
                //TIENE QUE ESTAR ENVIADO
                "9", //no quiero agregar mas productos
                "0",
                "x",
                //ENVIO PRODUCTOSS
                "a",
                "j",
                "carlos78", //vendedor
                "11", //cod producto
                "4", //id compra
                "x",
                "a",
                "j",
                "diegom", //vendedor
                "12", //cod producto
                "4", //id compra
                "x",
                "a",
                "j",
                "sofia25", //vendedor
                "13", //cod producto
                "4", //id compra
                "x",




                //CO5
                "a",
                "o",//modifica la fecha del sistema
                "2024",
                "5",
                "20", //termina de modificar fecha del sistema
                "a", //casos de uso
                "g", //realizar compra
                "juan87",
                "0",
                "3", //prod cod 
                "2", //cant 
                "0",
                "6",
                "3",
                //TIENE QUE ESTAR ENVIADO
                "9", //no quiero agregar mas productos
                "0",
                "x",
                //ENVIO PRODUCTOSS
                "a",
                "j",
                "carlos78", //vendedor
                "6", //cod producto
                "5", //id compra
                "x",




                //CO6
                "a",
                "o",//modifica la fecha del sistema
                "2024",
                "5",
                "12", //termina de modificar fecha del sistema
                "a", //casos de uso
                "g", //realizar compra
                "natalia",
                "0",
                "1", //prod cod 
                "2", //cant 
                "9", //no quiero agregar mas productos
                "0",
                "x",
    




                //CO7
                "a",
                "o",//modifica la fecha del sistema
                "2024",
                "5",
                "13", //termina de modificar fecha del sistema
                "a", //casos de uso
                "g", //realizar compra
                "natalia",
                "0",
                "1", //prod cod 
                "3", //cant 
                //TIENE QUE ESTAR ENVIADO
                "9", //no quiero agregar mas productos
                "0",
                "x",
                //ENVIO PRODUCTOSS
                "a",
                "j",
                "carlos78", //vendedor
                "1", //cod producto
                "4", //id compra
                "x",




                //CO8
                "a",
                "o",//modifica la fecha del sistema
                "2024",
                "5",
                "14", //termina de modificar fecha del sistema
                "a", //casos de uso
                "g", //realizar compra
                "pablo10",
                "0",
                "1", //prod cod 
                "4", //cant 
                "9", //no quiero agregar mas productos
                "0",
                "x",

                
                //CO9
                "a",
                "o",//modifica la fecha del sistema
                "2024",
                "5",
                "15", //termina de modificar fecha del sistema
                "a", //casos de uso
                "g", //realizar compra
                "roberto",
                "0",
                "1", //prod cod 
                "5", //cant 
                "9", //no quiero agregar mas productos
                "0",
                "x",

            };
            break;}
            case 'd':{
            inputIndex = 0;
            simulatedInputs = {
                //Promocion 1
                "a",                //opcion a
                "e",                //opcione e crear promocion
                "Casa nueva",       //nombre de la promo
                "Para que puedas ahorrar en la casa nueva", //Descripcion de la promo
                "2024",             //anio de vencimiento de la promo
                "10",                //mes de vencimiento de la promo
                "25",                //dia de vencimiento de la promo
                "30",                //Porcentaje de Descuento
                "ana23",             //Nombre del vendedor
                "2",                 //codigo del primer producto
                "1",                 //Cantidad minima del producto
                "s",                 //deseo seguir agregando
                "4",                 //codigo del segundo producto   
                "1",                 //cantidad minima
                "s",                 //deseo seguir agregando
                "8",                 //codigo del tercer producto
                "1",                 //cantidad minima
                "n",                 //xq no deseo seguir agregando

                //Promocion 2
                "a",                //opcion a
                "e",                //opcione e crear promocion
                "Fiesta",           //nombre de la promo
                "Para que no te quedes sin ropa para las fiestas", //Descripcion de la promo
                "2024",             //anio de vencimiento de la promo
                "10",                //mes de vencimiento de la promo
                "26",                //dia de vencimiento de la promo
                "20",                //Porcentaje de Descuento
                "carlos78",          //nombre del Vendedor
                "3",
                "2",
                "s",
                "6",
                "3",
                "n",

                //Promocion 3
                "a",                //opcion a
                "e",                //opcione e crear promocion
                "Domotica",           //nombre de la promo
                "Para modernizar tu casa", //Descripcion de la promo
                "2024",             //anio de vencimiento de la promo
                "10",                //mes de vencimiento de la promo
                "26",                //dia de vencimiento de la promo
                "10",                //Porcentaje de Descuento
                "diegom",            //nombre del Vendedor
                "5",
                "2",
                "n",

                //Promocion 4
                "a",                //opcion a
                "e",                //opcione e crear promocion
                "Liquidacion",           //nombre de la promo
                "Hasta agotar stock ", //Descripcion de la promo
                "2024",             //anio de vencimiento de la promo
                "03",                //mes de vencimiento de la promo
                "26",                //dia de vencimiento de la promo
                "10",                //Porcentaje de Descuento
                "diegom",            //nombre del Vendedor
                "14",
                "1",
                "n",
                
            };
            break;
            }
        case 't':{
            TFechaActual* fecha = TFechaActual::getInstanciaFecha(1,1,1);
            fecha->modificarFecha(02,06,2024);
            inputIndex = 0;
            simulatedInputs = {
   
                "a",
                "h",
                "laura",
                "1",
                "r",
                "2",
                "¿Es de buen material? Me preocupa la durabilidad.",
                "n",

                "a",
                "h",
                "juan87",
                "1",
                "r",
                "3",
                "He comprado antes y la calidad es buena.",
                "n",
                
                "a",
                "h",
                "natalia",
                "1",
                "n",
                "¿Como es el ajuste? ¿Es ajustada o holgada?",
                "n",

                "a",
                "h",
                "laura",
                "2",
                "n",
                "¿Cual es la resolucion del Televisor LED?",
                "n",

                "a",
                "h",
                "ana23",
                "2",
                "r",
                "6",
                "El televisor LED tiene una resolucion de 4K UHD.",
                "n",

                "b",
                "z",
            };
            break;}
        case 'z':{
            TFechaActual* fecha = TFechaActual::getInstanciaFecha(1,1,1);
            fecha->modificarFecha(03,06,2024);
            inputIndex = 0;
            simulatedInputs = {

                "a",
                "h",
                "pablo10",
                "2",
                "n",
                "¿Tiene soporte para HDR10?",
                "n",

                "a",
                "h",
                "ana23",
                "2",
                "r",
                "8",
                "Si, soporta HDR10.",
                "n",

                "a",
                "h",
                "natalia",
                "3",
                "n",
                "¿La chaqueta de cuero es resistente al agua?",
                "n",

                "a",
                "h",
                "carlos78",
                "3",
                "r",
                "10",
                "No, la chaqueta de cuero no es resistente al agua.",
                "n",

                "b",
                "w",
            };
            break;}
        case 'w':{
            TFechaActual* fecha = TFechaActual::getInstanciaFecha(1,1,1);
            fecha->modificarFecha(04,06,2024);
            inputIndex = 0;
            simulatedInputs = {
                "a",
                "h",
                "laura",
                "3",
                "r",
                "10",
                "¿Viene en otros colores?",
                "n",

                "a",
                "h",
                "carlos78",
                "3",
                "r",
                "12",
                "Si, tambien esta disponible en marron.",
                "n",

                "a",
                "h",
                "roberto",
                "3",
                "r",
                "10",
                "¿Es adecuada para climas frios?",
                "n",

                "a",
                "h",
                "pablo10",
                "4",
                "n",
                "¿El microondas digital tiene funcion de descongelacion rapida?",
                "n",

                "a",
                "h",
                "ana23",
                "4",
                "r",
                "15",
                "Si, el microondas digital incluye una funcion de descongelacion rapida.",
                "n",

                "b",
                "y",
            };
            break;}
        case 'y':{
            TFechaActual* fecha = TFechaActual::getInstanciaFecha(1,1,1);
            fecha->modificarFecha(05,06,2024);
            inputIndex = 0;
            simulatedInputs = {
                "a",
                "h",
                "natalia",
                "4",
                "r",
                "15",
                "¿Cuantos niveles de potencia tiene?",
                "n",

                "a",
                "h",
                "ana23",
                "4",
                "r",
                "17",
                "Tiene 10 niveles de potencia.",
                "n",

                "a",
                "h",
                "roberto",
                "4",
                "r",
                "15",
                "¿Es facil de limpiar?",
                "n",

                "a",
                "h",
                "roberto",
                "5",
                "n",
                "¿La luz LED se puede controlar con una aplicacion movil?",
                "n",

                "a",
                "h",
                "diegom",
                "5",
                "r",
                "20",
                "Si, la luz LED se puede controlar a traves de una aplicacion movil.",
                "n",

                "b",
                "u",
            };
            break;}
        case 'u':{
            TFechaActual* fecha = TFechaActual::getInstanciaFecha(1,1,1);
            fecha->modificarFecha(06,06,2024);
            inputIndex = 0;
            simulatedInputs = {
                "a",
                "h",
                "pablo10",
                "5",
                "r",
                "20",
                "¿Es compatible con Alexa o Google Home?",
                "n",

                "a",
                "h",
                "diegom",
                "5",
                "r",
                "22",
                "Si, es compatible con ambos.",
                "n",

                "a",
                "h",
                "natalia",
                "5",
                "r",
                "20",
                "¿Cuanto dura la bateria?",
                "n",

                "b",
                "v",
            };
            break;}
        case 'v':{
            TFechaActual* fecha = TFechaActual::getInstanciaFecha(1,1,1);
            fecha->modificarFecha(07,06,2024);
            inputIndex = 0;
            simulatedInputs = {
                "a",
                "h",
                "pablo10",
                "5",
                "r",
                "20",
                "¿La aplicacion movil es facil de usar?",
                "n",


            };
            break;}
        case 'x':{
            break;}
        default:{
            break;}
        }
    break;}
    case 'x':{
            printf("\nOpción 'x' seleccionada: Salir del programa.\n");
            e=false;
            break;}
    default:{
            break;}
}

}
}
