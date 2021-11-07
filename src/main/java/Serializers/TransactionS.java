package Serializers;

import Models.Complaint;
import Models.Transaction;
import com.google.gson.*;

import java.lang.reflect.Type;

public class TransactionS implements JsonSerializer<Models.Transaction> {
    @Override
    public JsonElement serialize(Models.Transaction transaction, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject TransactionJson = new JsonObject();
        TransactionJson.addProperty("Id",transaction.getId());
        TransactionJson.addProperty("title",transaction.getTitle());

        JsonArray complaintsJsonArray = new JsonArray();
        for(Complaint complaint: transaction.getComplaints()){
            complaintsJsonArray.add(jsonSerializationContext.serialize(complaint));
        }
        TransactionJson.add("complaints",complaintsJsonArray);
        return TransactionJson;
    }
}
