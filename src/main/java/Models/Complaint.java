package Models;

import java.util.UUID;

public class Complaint {
    private String Id;
    private String title;
    private String message;
    private Boolean resolvedStatus = false;
    public Complaint(String title, String message) {
        this.title = title;
        this.message = message;
        Id = UUID.randomUUID().toString();
    }
    public Complaint(){}
    public void setId(String id) {
        Id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return Id;
    }

    public Boolean getResolvedStatus() {
        return resolvedStatus;
    }

    public void setResolvedStatus(Boolean resolvedStatus) {
        this.resolvedStatus = resolvedStatus;
    }
}
