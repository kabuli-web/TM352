package Serializers;

import Models.User;
import com.google.gson.*;

import java.lang.reflect.Type;

public class UserS implements JsonSerializer<User> {
    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject UserJson = new JsonObject();
        UserJson.addProperty("Id",user.getId());
        UserJson.addProperty("username",user.getUsername());
        UserJson.addProperty("password",user.getPassword());

        JsonArray transactionsJsonArray = new JsonArray();
        for(String transaction: user.getTransactions()){
            transactionsJsonArray.add(transaction);
        }
        UserJson.add("transactions",transactionsJsonArray);
        return UserJson;
    }
};