package app.mvc.resoruces;

import com.google.gson.JsonObject;

public class ResourcesClientes {

    public JsonObject votar(String registro){
        JsonObject jsonObject  = new JsonObject();
        jsonObject.addProperty("servicio", "ejecutar");
        jsonObject.addProperty("variables", 2);
        jsonObject.addProperty("variable1", "servicio");
        jsonObject.addProperty("valor1", "votar");
        jsonObject.addProperty("variable2", registro);
        jsonObject.addProperty("valor2", "1");
        return jsonObject;
    }

    public JsonObject contar(){
        JsonObject jsonObject  = new JsonObject();
        jsonObject.addProperty("servicio", "ejecutar");
        jsonObject.addProperty("variables", 1);
        jsonObject.addProperty("variable1", "servicio");
        jsonObject.addProperty("valor1", "contar");
        return jsonObject;
    }

    public JsonObject listar(){
         JsonObject jsonObject  = new JsonObject();
        jsonObject.addProperty("servicio", "ejecutar");
        jsonObject.addProperty("variables", 1);
        jsonObject.addProperty("variable1", "servicio");
        jsonObject.addProperty("valor1", "listar");
        return jsonObject;
    }
}
