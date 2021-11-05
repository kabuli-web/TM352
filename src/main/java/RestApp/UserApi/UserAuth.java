package RestApp.UserApi;

import Models.User;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


public class UserAuth extends HttpServlet {




    private void GetUsers(HttpServletResponse response) throws IOException {
        ServletContext context = this.getServletContext();
        InputStream ins = context.getResourceAsStream("/WEB-INF/Data/AllUsers.json");
        System.out.println(context.getContextPath());

        JsonReader reader = new JsonReader(new InputStreamReader(ins));
        User[] users = new Gson().fromJson(reader,User[].class);
        for(User user: users){
            System.out.println(user.getUsername());
        }
        output(response,new Gson().toJson(users),200);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        GetUsers(response);
    }
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    GetUsers(response);
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
