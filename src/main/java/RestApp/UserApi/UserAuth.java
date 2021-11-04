package RestApp.UserApi;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class UserAuth extends HttpServlet {

@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    String jsonResponse = "{\"message\" : \"User End Point\"}";
    this.output(response,jsonResponse,200);
    }
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    Gson gson = new Gson();


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
