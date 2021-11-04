package Models;

import java.util.UUID;

public class Complaint {
    private String Id;
    private String Title;
    private String Message;

    public Complaint(String title, String message) {
        Title = title;
        Message = message;
        Id = UUID.randomUUID().toString();
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getId() {
        return Id;
    }
}
