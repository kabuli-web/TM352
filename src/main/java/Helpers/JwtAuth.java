package Helpers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.HashMap;

public class JwtAuth {

private Algorithm algorithm =  Algorithm.HMAC256("fearless");

public String GenerateToken(String Issuer, HashMap<String,Object> claims){
    try{
        String token = JWT.create()
                .withIssuer(Issuer)
                .withPayload(claims)
                .sign(this.algorithm);
        return token;
    }catch (JWTCreationException e){
        return null;
    }
}
public DecodedJWT decodedJWT(String token,String issuer){
    try {

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build(); //Reusable verifier instance
        DecodedJWT jwt = verifier.verify(token);
        return jwt;
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        return null;
    }
}

}
