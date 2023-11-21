package app.server;


import app.mvc.model.Bitacora;
import app.server.controllerServer.ControllerServer;
import app.server.model.Producto;
import app.server.model.bitacora.BitacoraDAO;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class ThreadEchoHandlerServer implements Runnable {
    private Socket sockerServer;
    private BitacoraDAO bitacoraDAO = new BitacoraDAO();

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
                String productoParaVotar = requestBroker.get("variable1").getAsString();
                    System.out.println(productoParaVotar);
                    controllerServer.agregarVotoProducto(productoParaVotar);
                    responseToBroker.addProperty("servicio", "votar");
                    responseToBroker.addProperty("respuestas", 1);
                    responseToBroker.addProperty("respuesta1", productoParaVotar);
                    responseToBroker.addProperty("valor1", controllerServer.contarVotosProducto(productoParaVotar));


                    System.out.println(responseToBroker);
                    break;
                case "registrar":
                System.out.println("Entraste a registrar tu voto");
                bitacoraDAO.escribirBitadora(requestBroker);


                responseToBroker.addProperty("servicio", "registrar");
                responseToBroker.addProperty("respuestas", 1);
                responseToBroker.addProperty("respuesta1", "eventos");
                responseToBroker.addProperty("valor1", bitacoraDAO.contarBitadora());
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
