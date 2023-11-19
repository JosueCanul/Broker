package app.server;

import app.server.controllerServer.ControllerServer;
import app.server.model.Producto;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ThreadEchoHandlerServer implements Runnable {
    private Socket sockerServer;

    public ThreadEchoHandlerServer(Socket socketServer) {
        this.sockerServer = socketServer;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(this.sockerServer.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.sockerServer.getInputStream()));

            String request = in.readLine();
            System.out.println("A llegado la petición del Broker al Server");
            System.out.println("Esta fue :" + request);
            Gson gson = new Gson();
            JsonObject requestBroker = gson.fromJson(request, JsonObject.class);


            String typeServicio = requestBroker.get("servicio").getAsString();
            System.out.println(typeServicio);

            ControllerServer controllerServer = new ControllerServer();
            JsonObject responseToBroker = new JsonObject();
            switch (typeServicio) {
                case "contar":
                    Producto[] productos = controllerServer.countAll();

                    responseToBroker.addProperty("servicio", "contar");
                    responseToBroker.addProperty("respuestas", 3);
                    responseToBroker.addProperty("respuesta1",
                            productos[0].getNombre()
                    );
                    responseToBroker.addProperty("valor1",
                            productos[0].contarVotos()
                    );
                    responseToBroker.addProperty("respuesta2",
                            productos[1].getNombre()
                    );
                    responseToBroker.addProperty("valor2",
                            productos[1].contarVotos()
                    );
                    responseToBroker.addProperty("respuesta3",
                            productos[2].getNombre()
                    );
                    responseToBroker.addProperty("valor3",
                            productos[2].contarVotos()
                    );
                    System.out.println(responseToBroker);
                    break;
                case "votar":
                    break;
                case "registrar":
                    break;
                case "listar":

                    break;
                default:
                    break;
            }


            out.println(gson.toJson(responseToBroker));
            out.close();
            in.close();
            this.sockerServer.close();
        }catch (Exception e) {

        }
    }

    public String processRequest(String typeServicio, JsonObject requestBroker){
        if(typeServicio.equals("ejecutar")) {
            System.out.println(requestBroker.get("valor1").getAsString());
            return requestBroker.get("valor1").getAsString();
        }
        return  typeServicio;
    }

}
