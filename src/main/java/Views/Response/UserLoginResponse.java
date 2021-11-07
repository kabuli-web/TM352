package Views.Response;



public class UserLoginResponse {
    public Boolean success;
    public String token;

    public UserLoginResponse(Boolean success,String token){
        this.success = success;
        this.token = token;
    }
}
