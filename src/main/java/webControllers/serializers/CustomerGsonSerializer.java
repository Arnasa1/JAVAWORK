package webControllers.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.kursinis.kursinis.model.User;

import java.lang.reflect.Type;

public class CustomerGsonSerializer implements JsonSerializer<User> {
    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", user.getId());
        jsonObject.addProperty("name", user.getName());
        jsonObject.addProperty("surname", user.getSurname());
        jsonObject.addProperty("login", user.getLogin());
        jsonObject.addProperty("address", user.getAddress());
        jsonObject.addProperty("cardNo", user.getCardNo());


        return jsonObject;
    }
}
