package testImpl;

import dto.Token;
import dto.User;
import org.apache.http.HttpResponse;

/**
 * Created by lux on 16.01.17.
 */
public class SharedData {
    private HttpResponse httpResponse;
    private String payload;

    private static User user;
    private static Token token;


    public static Token getToken() {
        return token;
    }

    public static void setToken(Token token) {
        SharedData.token = token;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User u) {
        user = u;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
