package Models;

import java.util.List;
import java.util.UUID;

public class Transaction {
    private String Id;
    private String Title;
    private List<Complaint> complaints;

    public Transaction(String title, List<Complaint> complaints) {
        Title = title;
        this.complaints = complaints;
        this.Id = UUID.randomUUID().toString();
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public String getId() {
        return Id;
    }

    public Boolean addComplaints(Complaint complaint){
        complaints.add(complaint);
        return true;
    }
    public Boolean removeComplaints(Complaint complaint){
        Boolean done = false;
        for(Complaint comp : complaints){
            if(comp.getId() == complaint.getId()){
                done = true;
                complaints.remove(comp);
            }
        }
        return done;
    }
}
