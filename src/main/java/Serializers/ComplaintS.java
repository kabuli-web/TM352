package Serializers;

import Models.Complaint;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ComplaintS implements JsonSerializer<Complaint> {
    @Override
    public JsonElement serialize(Complaint complaint, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonComplaint = new JsonObject();
        jsonComplaint.addProperty("Id",complaint.getId());
        jsonComplaint.addProperty("title",complaint.getTitle());
        jsonComplaint.addProperty("message",complaint.getMessage());
        jsonComplaint.addProperty("resolvedStatus",complaint.getResolvedStatus());

        return jsonComplaint;
    }
}
