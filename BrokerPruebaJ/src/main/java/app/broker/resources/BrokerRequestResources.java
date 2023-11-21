package app.broker.resources;

import com.google.gson.JsonObject;

public class BrokerRequestResources {
    public JsonObject contar(){
        JsonObject jsonObject  = new JsonObject();
        jsonObject.addProperty("servicio", "contar");
        jsonObject.addProperty("variables", 0);
        return jsonObject;
    }
    public JsonObject votar(Object request){
        return null;
    }

    public JsonObject registrar(JsonObject requestClient){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("servicio", "registrar");
        jsonObject.addProperty("variables", 2);
        jsonObject.addProperty("variable1", "evento");
        jsonObject.addProperty("valor1", 
        requestClient.get("valor2").getAsString()
        );
        jsonObject.addProperty("variable2", "fecha");
        jsonObject.addProperty("valor2", 
        requestClient.get("valor3").getAsString()
        );
        System.out.println("registrar del broker al servidor : ");
        System.out.println(jsonObject);
        return jsonObject;
    }

    public JsonObject listar(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("servicio", "listar");
        jsonObject.addProperty("variables", 0);
        return jsonObject;
    }
}
