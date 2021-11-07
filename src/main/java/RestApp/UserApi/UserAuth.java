package RestApp.UserApi;

import Helpers.JwtAuth;
import Models.User;
import Services.UserRepo;
import Views.Response.UserLoginResponse;
import Views.UserLogin;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;


public class UserAuth extends HttpServlet {

    private JwtAuth jwtAuth = new JwtAuth();


    private void Authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        ServletContext context = this.getServletContext();
//        InputStream ins = context.getResourceAsStream("/WEB-INF/Data/AllUsers.json");
        BufferedReader RequestReader = request.getReader();
        Gson gson = new Gson();
        UserLogin userData = gson.fromJson(RequestReader,UserLogin.class);
        if(userData==null || userData.username==null || userData.username.isEmpty() || userData.password==null || userData.password.isEmpty()){
            UserLoginResponse loginResponse = new UserLoginResponse(false,"null");
            output(response,gson.toJson(loginResponse),400);
            return;
        }
//        JsonReader reader = new JsonReader(new InputStreamReader(ins));
//        User[] users = gson.fromJson(reader,User[].class);
        UserRepo userRepo = new UserRepo();
        User user = userRepo.getUserByUsername(userData.username,this.getServletContext());
        if(user==null) {
            UserLoginResponse loginResponse = new UserLoginResponse(false,"null");
            output(response,gson.toJson(loginResponse),404);
            return;
        }

        if(user.getPassword().equals(userData.password)){
            HashMap<String,Object> payload = new HashMap<String,Object>();
            payload.put("username",user.getUsername());
            payload.put("userId",user.getId());
            UserLoginResponse loginResponse = new UserLoginResponse(false,null);
            String token = jwtAuth.GenerateToken("UserLogin",payload);
            if(token==null || token.isEmpty()){
                output(response,gson.toJson(loginResponse),500);
            }
            loginResponse.success = true;
            loginResponse.token = token;
            response.addHeader("token",token);
            output(response,gson.toJson(loginResponse),200);
            return;
        }else {
            UserLoginResponse loginResponse = new UserLoginResponse(false,null);
            output(response,gson.toJson(loginResponse),401);
            return;
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        UserLogin userLoginView = new UserLogin();
        userLoginView.username = "this is where u write the username";
        userLoginView.password = "this is where u write the password";

        output(response,new Gson().toJson(userLoginView),200);
    }
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    Authenticate(request,response);
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
