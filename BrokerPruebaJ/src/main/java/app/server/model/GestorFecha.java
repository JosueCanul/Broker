package app.server.model;

import java.time.LocalDateTime;

public class GestorFecha {
    public static String obtenerFechaYHora(){
        LocalDateTime actualidad = LocalDateTime.now();
        return actualidad.toString();
    }
}
