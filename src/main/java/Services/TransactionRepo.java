package Services;

import Deserializer.TransactionD;
import Models.Complaint;
import Models.Transaction;
import Serializers.TransactionS;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import javax.servlet.ServletContext;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepo {
    private GsonBuilder gsonBuilder = new GsonBuilder();
    private JsonSerializer<Transaction> serializer;
    private JsonDeserializer<Transaction> deserializer;
    private  Gson gson;
    public TransactionRepo(){
        serializer = new TransactionS();
        deserializer  = new TransactionD();

        gsonBuilder.registerTypeAdapter(Transaction.class,serializer);
        gsonBuilder.registerTypeAdapter(Transaction.class,deserializer);

        gson = gsonBuilder.setPrettyPrinting().create();
    }
    public Transaction getTransactionById(String Id, ServletContext context){
        InputStream ins = context.getResourceAsStream("/WEB-INF/Data/Transactions.json");
        JsonReader reader = new JsonReader(new InputStreamReader(ins));
        List<Transaction> transactions = gson.fromJson(reader,new TypeToken<List<Transaction>>(){}.getType());

        for(Transaction transaction:transactions){
            if(transaction.getId().equals(Id)){
                return transaction;
            }
        }
        return  null;
    }
    public ArrayList<Transaction> getAllTransactions(ServletContext context){
        InputStream ins = context.getResourceAsStream("/WEB-INF/Data/Transactions.json");
        JsonReader reader = new JsonReader(new InputStreamReader(ins));
        ArrayList<Transaction> transactions = gson.fromJson(reader,new TypeToken<ArrayList<Transaction>>(){}.getType());
        if(transactions==null || transactions.size()==0){
            return null;
        }
        return transactions;
    }
    public Transaction updateTransactionComplaints(String Id,ArrayList<Complaint> complaints,ServletContext context) throws IOException {
        InputStream ins = context.getResourceAsStream("/WEB-INF/Data/Transactions.json");
        JsonReader reader = new JsonReader(new InputStreamReader(ins));
        List<Transaction> transactions = gson.fromJson(reader,new TypeToken<List<Transaction>>(){}.getType());
        int found  =-1;
        for(int i=0; i <transactions.size();i++){
            if(transactions.get(i).getId().equals(Id)){
                found = i;
                transactions.get(i).setComplaints(complaints);
            }
        }
        if(found== -1){
            return null;
        }
//        File file = new File("Transactions.json");

        Writer writer = new FileWriter(context.getResource("/WEB-INF/Data/Transactions.json").getFile());
        gson.toJson(transactions,writer);
        writer.flush();
        writer.close();
        return transactions.get(found);
    }

}