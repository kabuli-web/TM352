package Models;

import java.util.List;
import java.util.UUID;

public class User {
    private String Id;
    private String name;
    private String username;
    private String password;
    private List<Transaction> transactions;
    public User(String name, String username, String password, List<Transaction> transactions){
        this.name = name;
        this.username = username;
        this.Id = UUID.randomUUID().toString();
        this.password = password;
        this.transactions = transactions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Boolean addTransactions(Transaction transaction){
        transactions.add(transaction);
        return true;
    }
    public Boolean removeTransactions(Transaction transaction){
        Boolean done = false;
        for(Transaction trans : transactions){
            if(trans.getId() == transaction.getId()){
                done = true;
                transactions.remove(trans);
            }
        }
        return done;
    }
}
