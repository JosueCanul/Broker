package app.broker;

import app.broker.resources.BrokerRequestResources;
import app.broker.resources.BrokerResponseResources;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.Socket;
import java.util.List;


public class ThreadEchoHandlerBroker implements Runnable{

    BrokerRequestResources brResources = new BrokerRequestResources();
    BrokerResponseResources brResponse = new BrokerResponseResources();
    private Socket sockerServer;
    private List<JsonObject> serversServices;
    private JsonObject sever;
    public ThreadEchoHandlerBroker(Socket socketServer, List<JsonObject> serversServices, JsonObject sever) {
        this.serversServices = serversServices;
        this.sockerServer = socketServer;
        this.sever = sever;
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(this.sockerServer.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(this.sockerServer.getInputStream()));
            String request = in.readLine();
            System.out.println(request);
            Gson gson = new Gson();
            JsonObject requestJsonFromClient = gson.fromJson(request, JsonObject.class);

            /*int portServer = 1016;
            String ipServer = "127.0.0.1";
            if (!this.sever.isEmpty()){
                ipServer = this.sever.get("valor1").getAsString();
                portServer = this.sever.get("valor2").getAsInt();
            }*/

            System.out.println("Llegó la peticion del cliente al broker");
            System.out.println("Esta fue :" + request);

            /*
            * Vemos el servicio que solicita el usuario
            * */
            String typeService = requestJsonFromClient.get("servicio").getAsString();
            System.out.println("El servicio del cliente fue : " + typeService);

            typeService = processRequest(typeService, requestJsonFromClient);
            System.out.println("Procesado = " + typeService);
            /*
             * Menu del broker
             */
            if(typeService.equals("registrar")){

                this.serversServices.add(requestJsonFromClient);
                System.out.println("Se realizo con exito el registro");
                out.println(brResponse.registrar());

            } else if (typeService.equals("votar")) {
                System.out.println("Preparando proceso de votar por parte del broker....");
                // Aquí hacer la transformación para el cliente
                TEHBrokerServerRequest tehBrokerServerRequest =
                        new TEHBrokerServerRequest(this.sever.get("valor2").getAsInt(),
                                this.sever.get("valor1").getAsString(), requestJsonFromClient);
                System.out.println("...");
                /*
                Aquí se espera a que el hilo broker-server termine
                * */
                Thread thread = new Thread(tehBrokerServerRequest);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                out.println(tehBrokerServerRequest.getResponse());
            } else if (typeService.equals("contar")) {
                System.out.println("Preparando la petición de contar del Broker para mandarlo al server");
                System.out.println("ey");
                TEHBrokerServerRequest tehBrokerServerRequestContar =
                        new TEHBrokerServerRequest(this.sever.get("valor2").getAsInt(),
                                this.sever.get("valor1").getAsString(),
                                brResources.contar());
                Thread thread = new Thread(tehBrokerServerRequestContar);
                System.out.println("Iniciando Hilo");
                thread.start();
                try {
                    thread.join();
                }catch (Exception e){
                    System.out.println("Algo paso en el hilo");
                }
                System.out.println("Esta es la respuesta del servidor");
                String respuestaServer = tehBrokerServerRequestContar.getResponse();
                System.out.println(respuestaServer);

                JsonObject respuestaBrokerCliente = brResponse.ejecutar(
                        gson.fromJson(respuestaServer, JsonObject.class)
                );
                System.out.println("Respuesta para el cliente" + respuestaBrokerCliente);
                out.println(respuestaBrokerCliente);
                thread.interrupt();
            }

            if (!this.serversServices.isEmpty()) this.sever = serversServices.get(0);

            this.sockerServer.close();
            in.close();
            out.close();
        }catch (Exception e) {

        }finally {

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
