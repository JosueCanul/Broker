package app.server.resources;

import com.google.gson.JsonObject;

public class JsonRequestsBroker {
    //Poner los datos actuales a la hora de una ejecución
    public static JsonObject registerRequest(String ipSever){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("servicio", "registrar");
        jsonObject.addProperty("variables", 4);
        jsonObject.addProperty("variable1", "servidor");
        jsonObject.addProperty("valor1", ipSever);
        jsonObject.addProperty("variable2", "puerto");
        jsonObject.addProperty("valor2", 1013);
        jsonObject.addProperty("variable3", "servicio");
        jsonObject.addProperty("valor3", "contar");
        jsonObject.addProperty("variable4", "parametros");
        jsonObject.addProperty("valor4", 1);
        return jsonObject;
    }
}
