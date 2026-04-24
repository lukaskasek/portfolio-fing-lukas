package logica;


public enum EstadoSesion {
    NO_LOGIN,           // nunca intentó iniciar sesión
    LOGIN_CORRECTO,     // tiene la sesión iniciada
    LOGIN_INCORRECTO    // le erro a la sesión al menos una vez
}