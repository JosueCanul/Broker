package app.mvc;

import app.mvc.resoruces.ResourcesClientes;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    private ResourcesClientes resourcesClientes = new ResourcesClientes();
    private String ip;
    private int port;
    public Cliente(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public JsonObject sendMessageVotar(String registro) {
        /*AtomicReference<JsonObject> respuestaBroker = null;
        SwingUtilities.invokeLater(() -> {


            JsonObject message = this.resourcesClientes.votar(registro);
            Gson gson = new Gson();
            out.println(gson.toJson(message));

            try {
                String respuestaServidor = in.readLine();
                System.out.println(respuestaServidor);
                respuestaBroker.set(gson.fromJson(respuestaServidor, JsonObject.class));
                clientSocket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                //try {
                    //if (clientSocket != null && !clientSocket.isClosed()) {
                      //  clientSocket.close();
                    //}
                //} catch (IOException e) {
                    //System.out.println(e.getMessage());
                //}
            }
        });
        return respuestaBroker.get();
        */
        return null;
    }

    public String contarProductos() {
            try (Socket socket = new Socket(this.ip, this.port)){
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                JsonObject message = this.resourcesClientes.contar();
                Gson gson = new Gson();
                out.println(gson.toJson(message));
                String respuestaServidor = in.readLine();

                System.out.println("Respuesta del broker al cliente ");
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
}