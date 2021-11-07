package RestApp.UserApi;

import Deserializer.TransactionD;
import Models.Complaint;
import Models.Transaction;
import Serializers.TransactionS;
import Services.TransactionRepo;
import Services.UserRepo;
import Views.UserLogin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class UserTransactions extends HttpServlet {
private UserRepo userRepo = new UserRepo();
private  TransactionRepo transactionRepo = new TransactionRepo();
private GsonBuilder gsonBuilder = new GsonBuilder();
private Gson gson;
public UserTransactions(){
    gsonBuilder.registerTypeAdapter(Transaction.class,new TransactionS());
    gsonBuilder.registerTypeAdapter(Transaction.class,new TransactionD());
    gson = gsonBuilder.create();
}
@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

    Models.User user  = userRepo.getUserById(response.getHeader("userId"),this.getServletContext());
    if(user==null){
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
    }
    final String transactionId = request.getParameter("Id");
    if(transactionId!=null){
        String ExistsInUserTransactions = user.getTransactions().stream().filter(trans->transactionId.equals(trans)).findAny().orElse(null);
        if(ExistsInUserTransactions==null){
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"Your not the owner");
            return;
        }
        Transaction transaction = transactionRepo.getTransactionById(transactionId,getServletContext());
        output(response,gson.toJson(transaction),200);
        return;
    }
    returnAllUserTransactions(response, user);
}

    private void returnAllUserTransactions(HttpServletResponse response, Models.User user) throws IOException {
        ArrayList<Transaction> transactions = userRepo.getUserTransactions(user.getId(),this.getServletContext());
        if(transactions==null || transactions.size()==0) {
            response.sendError(HttpServletResponse.SC_NO_CONTENT);
        }
        output(response,gson.toJson(transactions),200);
        return;
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        String transactionId = request.getParameter("Id");
        if(transactionId==null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Transaction transaction = transactionRepo.getTransactionById(transactionId,getServletContext());
        if(transaction==null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        BufferedReader RequestReader = request.getReader();

        ArrayList<Complaint> complaints = gson.fromJson(RequestReader, new TypeToken<ArrayList<Complaint>>(){}.getType());
        if(complaints==null) {

            response.sendError(500,"error converting json list of complaints to java list of complaints");
            return;
        }

        Transaction updatedTransaction = transactionRepo.updateTransactionComplaints(transaction.getId(),complaints,getServletContext());
        if(updatedTransaction==null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        output(response,gson.toJson(updatedTransaction),201);
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
