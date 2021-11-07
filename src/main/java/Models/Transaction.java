package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Transaction {
    private String Id;
    private String Title;
    private ArrayList<Complaint> complaints;

    public Transaction(String title, ArrayList<Complaint> complaints) {
        Title = title;
        this.complaints = complaints;
        this.Id = UUID.randomUUID().toString();
    }
    public  Transaction(){}
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public ArrayList<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(ArrayList<Complaint> complaints) {
        this.complaints = complaints;
    }

    public String getId() {
        return Id;
    }
    public void setId(String id){
        Id = id;
    }
    public Boolean addComplaints(Complaint complaint){
        complaints.add(complaint);
        return true;
    }
    public Boolean removeComplaint(Complaint complaint){
        Boolean done = false;
        for(Complaint comp : complaints){
            if(comp.getId().equals(complaint.getId()) ){
                done = true;
                complaints.remove(comp);
            }
        }
        return done;
    }
}
