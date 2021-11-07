package RestApp.Employee;

import Helpers.JwtAuth;
import Models.Employee;
import Models.User;
import Services.EmployeeRepo;
import Services.UserRepo;
import Views.Response.UserLoginResponse;
import Views.UserLogin;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


public class EmployeeAuth extends HttpServlet {

    private JwtAuth jwtAuth = new JwtAuth();


    private void Authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException {

        BufferedReader RequestReader = request.getReader();
        Gson gson = new Gson();
        UserLogin employeeReqData = gson.fromJson(RequestReader,UserLogin.class);
        if(employeeReqData==null || employeeReqData.username==null || employeeReqData.username.isEmpty() || employeeReqData.password==null || employeeReqData.password.isEmpty()){
            UserLoginResponse loginResponse = new UserLoginResponse(false,"null");
            output(response,gson.toJson(loginResponse),400);
            return;
        }

        EmployeeRepo employeeRepo = new EmployeeRepo();
        Employee employee = employeeRepo.getUserByUsername(employeeReqData.username,this.getServletContext());
        if(employee==null) {
            UserLoginResponse loginResponse = new UserLoginResponse(false,"null");
            output(response,gson.toJson(loginResponse),404);
            return;
        }

        if(employee.getPassword().equals(employeeReqData.password)){
            HashMap<String,Object> payload = new HashMap<String,Object>();
            payload.put("username",employee.getUsername());
            payload.put("employeeId",employee.getId());
            UserLoginResponse loginResponse = new UserLoginResponse(false,null);
            String token = jwtAuth.GenerateToken("EmployeeLogin",payload);
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
