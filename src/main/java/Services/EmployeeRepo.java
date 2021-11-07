package Services;

import Deserializer.UserD;
import Models.Employee;
import Models.Transaction;
import Models.User;
import Serializers.UserS;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepo {

    private  Gson gson = new Gson();

    public EmployeeRepo(){
    }
    public Employee getEmployeeById(String Id, ServletContext context){
        InputStream ins = context.getResourceAsStream("/WEB-INF/Data/Employees.json");
        JsonReader reader = new JsonReader(new InputStreamReader(ins));
        List<Employee> employees = gson.fromJson(reader,new TypeToken<List<Employee>>(){}.getType());
        for(Employee employee:employees){
            if(employee.getId().equals(Id)){
                return employee;
            }
        }
        return  null;
    }
    public Employee getUserByUsername(String username, ServletContext context){
        InputStream ins = context.getResourceAsStream("/WEB-INF/Data/Employees.json");
        JsonReader reader = new JsonReader(new InputStreamReader(ins));
        List<Employee> employees = gson.fromJson(reader,new TypeToken<List<Employee>>(){}.getType());
        for(Employee employee:employees){
            if(employee.getUsername().equals(username)){
                return employee;
            }
        }
        return  null;
    }

}
