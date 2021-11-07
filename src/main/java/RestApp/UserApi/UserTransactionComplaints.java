package RestApp.UserApi;

import Deserializer.TransactionD;
import Models.Complaint;
import Models.Transaction;
import Serializers.TransactionS;
import Services.TransactionRepo;
import Services.UserRepo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class UserTransactionComplaints extends HttpServlet {
private UserRepo userRepo = new UserRepo();
private  TransactionRepo transactionRepo = new TransactionRepo();
private GsonBuilder gsonBuilder = new GsonBuilder();
private Gson gson;
public UserTransactionComplaints(){
    gsonBuilder.registerTypeAdapter(Transaction.class,new TransactionS());
    gsonBuilder.registerTypeAdapter(Transaction.class,new TransactionD());
    gson = gsonBuilder.create();
}
private Transaction ValidateTransactionRequest(Models.User user,HttpServletResponse response,HttpServletRequest request) throws IOException {

    String transactionId = request.getParameter("transactionId");
    if(transactionId==null || transactionId.isEmpty()){
        response.sendError(HttpServletResponse.SC_BAD_REQUEST,"no transaction id provided");
        return null;
    }

    Transaction transaction = transactionRepo.getTransactionById(transactionId,getServletContext());
    if(transaction==null){
        response.sendError(HttpServletResponse.SC_NOT_FOUND,"transaction not found");
        return null;
    }
    String ExistsInUserTransactions = user.getTransactions().stream().filter(trans->transactionId.equals(trans)).findAny().orElse(null);
    if(ExistsInUserTransactions==null){
        response.sendError(HttpServletResponse.SC_FORBIDDEN,"Your not the owner");
        return null;
    }
    return  transaction;
}
@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    Models.User user  = userRepo.getUserById(response.getHeader("userId"),this.getServletContext());
    if(user==null){
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return ;
    }
    Transaction transaction = ValidateTransactionRequest(user,response,request);
    if(transaction==null){
//        response.sendError(HttpServletResponse.SC_BAD_REQUEST,"transaction validation failed");
        return ;
    }
    String ComplaintId = request.getParameter("complaintId");
    if(ComplaintId==null || ComplaintId.isEmpty()){
        returnAllUserTransactionComplaints(transaction.getId(),response, user);
    }
    Complaint complaint  = transaction.getComplaints().stream().filter(complaint1 -> ComplaintId.equals(complaint1.getId())).findAny().orElse(null);
    if(complaint==null){
        response.sendError(HttpServletResponse.SC_NOT_FOUND,"complaint  not found");
        return;
    }
    output(response,gson.toJson(complaint),200);
    return;

}

    private void returnAllUserTransactionComplaints(String transactionId,HttpServletResponse response, Models.User user) throws IOException {
        ArrayList<Complaint> complaints = transactionRepo.getTransactionById(transactionId,this.getServletContext()).getComplaints();
        if(complaints==null || complaints.size()==0) {
            response.sendError(HttpServletResponse.SC_NO_CONTENT);
        }

        output(response,gson.toJson(complaints),200);
        return;
    }

    @Override
protected void doPut(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        Models.User user  = userRepo.getUserById(response.getHeader("userId"),this.getServletContext());
        if(user==null){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return ;
        }
        Transaction transaction = ValidateTransactionRequest(user,response,request);
        if(transaction==null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"transaction validation failed");
            return ;
        }
        String ComplaintId = request.getParameter("complaintId");
        if(ComplaintId==null || ComplaintId.isEmpty()){
            returnAllUserTransactionComplaints(transaction.getId(),response, user);
        }
        int found = -1;
        for(int i=0;i<transaction.getComplaints().size();i++){
            if(transaction.getComplaints().get(i).getId().equals(ComplaintId)){
                found = i;
            }
        }
        if(found==-1){
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"complaint  not found");
            return;
        }
        Complaint reqComplaint = gson.fromJson(request.getReader(),Complaint.class);
        if(reqComplaint==null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"error serializing body");
            return;
        }
        transaction.getComplaints().get(found).setTitle(reqComplaint.getTitle());
        transaction.getComplaints().get(found).setMessage(reqComplaint.getMessage());
        Transaction updated = transactionRepo.updateTransactionComplaints(transaction.getId(), transaction.getComplaints(),this.getServletContext());
        if(updated==null){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"couldnt update");
            return;
        }
        output(response,gson.toJson(updated.getComplaints().get(found)),201);
        return;

}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        Models.User user  = userRepo.getUserById(response.getHeader("userId"),this.getServletContext());
        if(user==null){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return ;
        }
        Transaction transaction = ValidateTransactionRequest(user,response,request);
        if(transaction==null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"transaction validation failed");
            return ;
        }
        if(transaction.getComplaints().size()>0){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"a complaint already exists");
            return;
        }
        Complaint reqComplaint = gson.fromJson(request.getReader(),Complaint.class);
        if(reqComplaint==null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"error serializing body");
            return;
        }
        Complaint newComplaint = new Complaint(reqComplaint.getTitle(), reqComplaint.getMessage());
        transaction.addComplaints(newComplaint);
        Transaction updated = transactionRepo.updateTransactionComplaints(transaction.getId(), transaction.getComplaints(),this.getServletContext());
        if(updated==null){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"couldnt update");
            return;
        }
        output(response,gson.toJson(newComplaint),201);
        return;

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Models.User user  = userRepo.getUserById(response.getHeader("userId"),this.getServletContext());
        if(user==null){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return ;
        }
        Transaction transaction = ValidateTransactionRequest(user,response,request);
        if(transaction==null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"transaction validation failed");
            return ;
        }
        String ComplaintId = request.getParameter("complaintId");
        if(ComplaintId==null || ComplaintId.isEmpty()){
            returnAllUserTransactionComplaints(transaction.getId(),response, user);
        }
        int found = -1;
        for(int i=0;i<transaction.getComplaints().size();i++){
            if(transaction.getComplaints().get(i).getId().equals(ComplaintId)){
                found = i;
            }
        }
        if(found==-1){
            response.sendError(HttpServletResponse.SC_NOT_FOUND,"complaint  not found");
            return;
        }
        Complaint reqComplaint = gson.fromJson(request.getReader(),Complaint.class);
        if(reqComplaint==null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"error serializing body");
            return;
        }
        transaction.getComplaints().remove(found);
        Transaction updated = transactionRepo.updateTransactionComplaints(transaction.getId(), transaction.getComplaints(),this.getServletContext());
        if(updated==null){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"couldnt update");
            return;
        }
        output(response,"done", 201);
        return;
    }

    private void output(HttpServletResponse response, String payload, int status) throws IOException {

    PrintWriter out = response.getWriter();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(status);
    out.print(payload);
    out.flush();
}


}
