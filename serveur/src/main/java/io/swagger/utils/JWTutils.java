package io.swagger.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;

import java.io.UnsupportedEncodingException;

/**
 * Created by lux on 12.12.16.
 */
public class JWTutils {

    public static String createToken(String id)
    {
        String token;
        try {
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("id", id) //on passe le nom de l'app dans le Json web Token
                    //TODO CONFIGURER LE SECRET EN UNE CLE RSA
                    .sign(Algorithm.HMAC256("sdfghjkjhgfert6789opè753."));
        } catch (JWTCreationException exception){
            throw new RuntimeException("You need to enable Algorithm.HMAC256");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
        return token;
    }

    /**
     * To be parsed as Long later...
     * @param token
     * @return
     */
    public static long parseToken(String token) throws JWTDecodeException
    {
        JWT jwt = JWT.decode(token);
        String id = jwt.getClaim("id").asString();
        return Long.parseLong(id);
    }
}
