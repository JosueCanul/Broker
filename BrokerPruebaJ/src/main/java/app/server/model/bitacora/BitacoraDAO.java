package app.server.model.bitacora;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import app.server.model.GestorFecha;
import app.server.resources.RegistrosRutaServer;

public class BitacoraDAO {
    public static final String url = "";

    public void escribirBitadora(JsonObject registro){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(
            RegistrosRutaServer.bitacora, true))) {
            String nuevoRegistro = 
                registro.get("valor1").getAsString() + " " +
                        registro.get("valor2").getAsString() + "\n";
            bw.write(nuevoRegistro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> leerBitacora(){
        List<String> bitacoraCompleta = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RegistrosRutaServer.bitacora))) {
            String linea;    
            while ((linea = br.readLine()) != null) {
                bitacoraCompleta.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitacoraCompleta;
    }

    public int contarBitadora()
    {
        StringBuilder contenido = new StringBuilder();
        int cantVotos = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(RegistrosRutaServer.bitacora))) {
            String linea;    
            while ((linea = br.readLine()) != null) {
                cantVotos++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cantVotos;
    }


}
