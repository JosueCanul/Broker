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

}
