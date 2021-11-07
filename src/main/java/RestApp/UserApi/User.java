package RestApp.UserApi;

import Deserializer.TransactionD;
import Deserializer.UserD;
import Models.Transaction;
import Serializers.TransactionS;
import Serializers.UserS;
import Services.UserRepo;
import Views.Response.UserLoginResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class User extends HttpServlet {
private UserRepo userRepo = new UserRepo();
    private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson gson;
    public User(){
        gsonBuilder.registerTypeAdapter(Models.User.class,new UserS());
        gsonBuilder.registerTypeAdapter(Models.User.class,new UserD());
        gson = gsonBuilder.create();
    }
@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

    Models.User user  = userRepo.getUserById(response.getHeader("userId"),this.getServletContext());
    if(user==null){
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
    }
    user.setPassword("***********");
    output(response,gson.toJson(user),200);
    }
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    super.doPost(request,response);
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
