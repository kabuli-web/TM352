package Deserializer;

import Models.Complaint;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ComplaintD implements JsonDeserializer<Complaint> {
    @Override
    public Complaint deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Complaint complaint = new Complaint();
        JsonObject jsonComplaint = jsonElement.getAsJsonObject();
        complaint.setId(jsonComplaint.get("Id").getAsString());
        complaint.setTitle(jsonComplaint.get("title").getAsString());
        complaint.setMessage(jsonComplaint.get("message").getAsString());
        complaint.setResolvedStatus(jsonComplaint.get("resolvedStatus").getAsBoolean());
        return complaint;
    }
}
