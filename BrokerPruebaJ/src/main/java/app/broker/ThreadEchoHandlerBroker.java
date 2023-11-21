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

            System.out.println("Llegó la peticion del cliente al broker");
            System.out.println("Esta fue :" + request);

            String typeService = requestJsonFromClient.get("servicio").getAsString();
            System.out.println("El servicio del cliente fue : " + typeService);

            typeService = processRequest(typeService, requestJsonFromClient);
            System.out.println("Procesado = " + typeService);

            if(typeService.equals("registrar") &&  requestJsonFromClient.get("servicio").getAsString().equals("ejecutar")){
                System.out.println("Has solicitado al broker registrar una votación");
                System.out.println("Lo que le estoy pasando al hilo : ");
                System.out.println(requestJsonFromClient);
                TEHBrokerServerRequest tehBrokerServerRequestContar =
                        new TEHBrokerServerRequest(this.sever.get("valor2").getAsInt(),
                                this.sever.get("valor1").getAsString(),
                                brResources.registrar(requestJsonFromClient));
                
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
            else if(typeService.equals("registrar")){

                this.serversServices.add(requestJsonFromClient);
                System.out.println("Se realizo con exito el registro");
                out.println(brResponse.registrar());

            } else if (typeService.equals("votar")) { //VOTAR-------------------------------------------------------------------------------
                System.out.println("Preparando proceso de votar por parte del broker....");
                // Aquí hacer la transformación para el cliente
                JsonObject votarRequestJson = new JsonObject();
                votarRequestJson.addProperty("servicio", "votar");
                votarRequestJson.addProperty("variables", 1);
                votarRequestJson.addProperty("variable1", requestJsonFromClient.get("variable2").getAsString());
                votarRequestJson.addProperty("valor1", 1);
                System.out.println("la solicitud votar para el server es: " + votarRequestJson);

                TEHBrokerServerRequest tehBrokerServerRequest =
                        new TEHBrokerServerRequest(this.sever.get("valor2").getAsInt(),
                                this.sever.get("valor1").getAsString(), votarRequestJson);
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
                
                String serverRespuesta = tehBrokerServerRequest.getResponse();
                System.out.println("Esta es la respuesta del server para cuando has votado : ");
                System.out.println(serverRespuesta);
                JsonObject repuestaVotar = brResponse.ejecutar(
                    gson.fromJson(serverRespuesta, JsonObject.class)
                    );
                System.out.println(repuestaVotar);
                out.println(repuestaVotar);
                thread.interrupt();
            } else if (typeService.equals("contar")) {//CONTAR___________________________________________________________
                System.out.println("Preparando la petición de contar del Broker para mandarlo al server");

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
