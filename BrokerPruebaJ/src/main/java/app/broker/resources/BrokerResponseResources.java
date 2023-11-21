package app.broker.resources;


import java.util.List;

import com.google.gson.JsonObject;

public class BrokerResponseResources {

    public JsonObject ejecutar(JsonObject responseServer){
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("servicio", "ejecutar");
        int respuestasServer = responseServer.get("respuestas").getAsInt();
        jsonObject.addProperty("respuestas:", respuestasServer+1);
        jsonObject.addProperty("respuesta1", "servicio");
        jsonObject.addProperty("valor1", responseServer.get("servicio").getAsString());
        for (int i = 1; i <= respuestasServer; i++){
            String respuestaCurrent = responseServer.get("respuesta" + i).getAsString();
            String valorcurrent = responseServer.get("valor" + i ).getAsString();
            jsonObject.addProperty("respuesta" + (i+1), respuestaCurrent);
            jsonObject.addProperty("valor" + (i+1), valorcurrent);
        }
        System.out.println("La respuesta preparada para el cliente es");
        System.out.println(jsonObject);
        return  jsonObject;
    }

    public JsonObject registrar(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("servicio", "registrar");
        jsonObject.addProperty("respuestas", 1);
        jsonObject.addProperty("respuesta1", "identificador");
        jsonObject.addProperty("valor1", 1);
        return jsonObject;
    }

    public JsonObject listarServers(List<JsonObject> servers){
        JsonObject resonseServers = new JsonObject();
        resonseServers.addProperty("servicio", "listar");
        resonseServers.addProperty("respuestas", servers.size());
        for (int i = 0; i < servers.size(); i++) {
            JsonObject currentServer = servers.get(i);
            resonseServers.addProperty("respuesta" + (i+1), currentServer.get("valor3").getAsString());
            resonseServers.addProperty("valor" + (i+1), currentServer.get("valor1").getAsString());
        }
        return resonseServers;
    }
}
