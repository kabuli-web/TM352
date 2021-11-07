package Services;

import Deserializer.UserD;
import Models.Transaction;
import Models.User;
import Serializers.UserS;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserRepo {
    private GsonBuilder gsonBuilder = new GsonBuilder();
    private JsonSerializer<User> serializer;
    private JsonDeserializer<User> deserializer;
    private  Gson gson;
    private TransactionRepo transactionRepo = new TransactionRepo();
    public UserRepo(){
        serializer = new UserS();
        deserializer  = new UserD();

        gsonBuilder.registerTypeAdapter(User.class,serializer);
        gsonBuilder.registerTypeAdapter(User.class,deserializer);
        gson = gsonBuilder.create();
    }
    public User getUserById(String Id, ServletContext context){
        InputStream ins = context.getResourceAsStream("/WEB-INF/Data/AllUsers.json");
        JsonReader reader = new JsonReader(new InputStreamReader(ins));
        List<User> users = gson.fromJson(reader,new TypeToken<List<User>>(){}.getType());

        for(User user:users){
            if(user.getId().equals(Id)){
                return user;
            }
        }
        return  null;
    }
    public User getUserByUsername(String username, ServletContext context){
        InputStream ins = context.getResourceAsStream("/WEB-INF/Data/AllUsers.json");
        JsonReader reader = new JsonReader(new InputStreamReader(ins));
        User[] users = gson.fromJson(reader,User[].class);
        for(User user:users){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return  null;
    }
    public ArrayList<Transaction> getUserTransactions(String userId,ServletContext context){
        User user = getUserById( userId,context);
        if(user==null) return  null;
        ArrayList<Transaction> transactionArrayList = new ArrayList<Transaction>();
        for(String transactionId: user.getTransactions()){
            Transaction transaction = transactionRepo.getTransactionById(transactionId,context);

            if(transaction!=null){
                transactionArrayList.add(transaction);
            }
        }
        if(transactionArrayList.size()>0){
            return transactionArrayList;
        }
        return null;
    }
}
