package RestApp.Employee;

import Deserializer.TransactionD;
import Models.Complaint;
import Models.Employee;
import Models.Transaction;
import Serializers.TransactionS;
import Services.TransactionRepo;
import Views.UserLogin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class EmployeeComplaints extends HttpServlet {
    private TransactionRepo transactionRepo = new TransactionRepo();
    private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson gson ;
    public EmployeeComplaints(){
        gsonBuilder.registerTypeAdapter(Transaction.class,new TransactionS());
        gsonBuilder.registerTypeAdapter(Transaction.class,new TransactionD());
        gson = gsonBuilder.create();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        ArrayList<Transaction> transactions = transactionRepo.getAllTransactions(getServletContext());
        if(transactions==null || transactions.size()<1){
            response.sendError(HttpServletResponse.SC_NO_CONTENT,"no transaction available");
            return;
        }
        ArrayList<Complaint> complaints = new ArrayList<Complaint>();
        for(Transaction transaction:transactions){
            complaints.addAll(transaction.getComplaints());
        }
        if(complaints.size()<1){
            response.sendError(HttpServletResponse.SC_NO_CONTENT);
            return;
        }
        String complaintId = request.getParameter("complaintId");
        if(complaintId!=null && !complaintId.isEmpty()){
            Complaint complaint = complaints.stream().filter(comp->complaintId.equals(comp.getId())).findAny().orElse(null);
            if(complaint==null){
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            output(response,gson.toJson(complaint),200);
            return;
        }
        output(response,gson.toJson(complaints),200);
        return;
        }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        ArrayList<Transaction> transactions = transactionRepo.getAllTransactions(getServletContext());
        if(transactions==null || transactions.size()<1){
            response.sendError(HttpServletResponse.SC_NO_CONTENT,"no transaction available");
            return;
        }
        ArrayList<Complaint> complaints = new ArrayList<Complaint>();
        for(Transaction transaction:transactions){
            complaints.addAll(transaction.getComplaints());
        }
        if(complaints.size()<1){
            response.sendError(HttpServletResponse.SC_NO_CONTENT);
            return;
        }
        String complaintId = request.getParameter("complaintId");
        if(complaintId!=null && !complaintId.isEmpty()){
            Complaint complaint = complaints.stream().filter(comp->complaintId.equals(comp.getId())).findAny().orElse(null);
            if(complaint==null){
                response.sendError(HttpServletResponse.SC_NOT_FOUND,"Counldnt find complaint");
                return;
            }
            Complaint reqComplaint = gson.fromJson(request.getReader(),Complaint.class);
            Transaction complaintTransaction = transactions.stream().filter(transaction -> transaction.getComplaints().contains(complaint)).findFirst().orElse(null);
            if(complaintTransaction==null){
                response.sendError(HttpServletResponse.SC_NOT_FOUND,"complaints parent transaction could not be found");
                return;
            }
            reqComplaint.setId(complaintId);
            Boolean removed  = complaintTransaction.getComplaints().removeIf(complaint1 -> complaintId.equals(complaint1.getId()));
            if(removed){
                complaintTransaction.addComplaints(reqComplaint);
                Transaction updatedtransaction = transactionRepo.updateTransactionComplaints(complaintTransaction.getId(), complaintTransaction.getComplaints(), getServletContext());
                if(updatedtransaction==null){
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"couldnt update transaction");
                    return;
                }
                output(response,gson.toJson(reqComplaint),201);
                return;
            }
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Couldnt update complaint");
            return;
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST,"You have to add complaintId to header");
        return;
    }


    private void output(HttpServletResponse response,String payload,int status) throws IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        out.print(payload);
        out.flush();
    }



}
