package Deserializer;

import Models.User;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserD implements JsonDeserializer<User> {

    @Override
    public User deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        User user = new User();
        JsonObject UserJson =  jsonElement.getAsJsonObject();
        user.setId(UserJson.get("Id").getAsString());
        user.setUsername(UserJson.get("username").getAsString());
        user.setPassword(UserJson.get("password").getAsString());
        user.setName(UserJson.get("name").getAsString());
        JsonArray jsonUserTransactions = UserJson.get("transactions").getAsJsonArray();
        ArrayList<String> userTransactions = new ArrayList<String>();
        for(JsonElement jsonTransaction:jsonUserTransactions){
            userTransactions.add(jsonTransaction.getAsString());
        }
        user.setTransactions(userTransactions);
        return user;
    }
};