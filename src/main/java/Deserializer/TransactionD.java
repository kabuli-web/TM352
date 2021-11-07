package Deserializer;

import Models.Complaint;
import Models.Transaction;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TransactionD implements JsonDeserializer<Transaction> {
    @Override
    public Transaction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Transaction transaction = new Transaction();
        JsonObject transactionJson =  jsonElement.getAsJsonObject();
        transaction.setId(transactionJson.get("Id").getAsString());
        transaction.setTitle(transactionJson.get("title").getAsString());

        JsonArray jsonTransactionComplaints = transactionJson.get("complaints").getAsJsonArray();
        ArrayList<Complaint> TransactionComplaints = new ArrayList<Complaint>();
        for(JsonElement jsonComplaint:jsonTransactionComplaints){
            TransactionComplaints.add(jsonDeserializationContext.<Complaint>deserialize(jsonComplaint,Complaint.class));
        }
        transaction.setComplaints(TransactionComplaints);
        return transaction;
    }
};
