package app.mvc;

import app.mvc.resoruces.ResourcesClientes;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

public class Cliente {
    private ResourcesClientes resourcesClientes = new ResourcesClientes();
    private String ip;
    private int port;

    public Cliente(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public JsonObject sendMessageVotar(String registro) {
        JsonObject response = null;
        try (Socket socket = new Socket(this.ip, this.port)) {
            /*
             * Este metodo solo manda el servicio directo al server
             */
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            JsonObject message = this.resourcesClientes.votar(registro);
            System.out.println("json votar: " + message);
            Gson gson = new Gson();
            out.println(gson.toJson(message));
            // AQUI ES EL PROBLEMAAA
            String respuestaServidor = in.readLine();

            System.out.println("Respuesta del broker al cliente para votar");
            System.out.println(respuestaServidor);
            response = gson.fromJson(respuestaServidor, JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String contarProductos() {
        
        try (Socket socket = new Socket(this.ip, this.port)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            JsonObject message = this.resourcesClientes.contar();
            Gson gson = new Gson();
            out.println(gson.toJson(message));
            String respuestaServidor = in.readLine();

            System.out.println("RESPUESTA DEL BROKER AL CLIENTE");
            System.out.println(respuestaServidor);
            JsonObject response = gson.fromJson(respuestaServidor, JsonObject.class);
            System.out.println("Json :" + response.toString());
            in.close();
            out.close();
            socket.close();
            return respuestaServidor;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    public void listarVotos(){
        JsonObject response = null;
        try (Socket socket = new Socket(this.ip, this.port)) {
            /*
             * Este metodo solo manda el servicio directo al server
             */
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Gson gson = new Gson();

            out.println(gson.toJson(this.resourcesClientes.listar()));
            // AQUI ES EL PROBLEMAAA
            String respuestaBroker = in.readLine();

            System.out.println("Respuesta del broker al cliente para registrar");
            System.out.println(respuestaBroker);
            response = gson.fromJson(respuestaBroker, JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void listarServers(){
        JsonObject response = null;
        try (Socket socket = new Socket(this.ip, this.port)) {
            /*
             * Este metodo solo manda el servicio directo al server
             */
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Gson gson = new Gson();

            out.println(gson.toJson(this.resourcesClientes.listarServers()));
            // AQUI ES EL PROBLEMAAA
            String respuestaBroker = in.readLine();

            System.out.println("Respuesta del broker al cliente para listar servers");
            System.out.println(respuestaBroker);
            response = gson.fromJson(respuestaBroker, JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public JsonObject registrar(String nomProd){
        JsonObject registrarVotojson = new JsonObject();
        registrarVotojson.addProperty("servicio", "ejecutar");
        registrarVotojson.addProperty("variables", 3);
        registrarVotojson.addProperty("variable1", "servicio");
        registrarVotojson.addProperty("valor1", "registrar");
        registrarVotojson.addProperty("variable2", "evento");
        registrarVotojson.addProperty("valor2", "Se registro un voto para: " + nomProd);
        registrarVotojson.addProperty("variable3", "fecha");
        registrarVotojson.addProperty("valor3", LocalDateTime.now().toString());

        JsonObject response = null;
        try (Socket socket = new Socket(this.ip, this.port)) {
            /*
             * Este metodo solo manda el servicio directo al server
             */
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Gson gson = new Gson();

            out.println(gson.toJson(registrarVotojson));
            // AQUI ES EL PROBLEMAAA
            String respuestaBroker = in.readLine();

            System.out.println("Respuesta del broker al cliente para registrar");
            System.out.println(respuestaBroker);
            response = gson.fromJson(respuestaBroker, JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente("127.0.0.1", 7777);
        cliente.listarServers();
    }
}
