package Middlewares;

import Helpers.JwtAuth;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class EmployeeJwtVerification implements Filter {
    JwtAuth jwtAuth;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.jwtAuth = new JwtAuth();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = request.getHeader("token");

        if ( token==null || token.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"no token was provided");
            return;
        }
        DecodedJWT decodedJWT = jwtAuth.decodedJWT(token,"EmployeeLogin");
        if ( decodedJWT==null ){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"Something wrong with the token");
            return;
        }
        Map<String, Claim> claims = decodedJWT.getClaims();
        String username  = claims.get("username").asString();
        String employeeId  = claims.get("employeeId").asString();
        response.addHeader("username",username);
        response.addHeader("employeeId",employeeId);
        filterChain.doFilter(servletRequest,servletResponse);
    }


}
