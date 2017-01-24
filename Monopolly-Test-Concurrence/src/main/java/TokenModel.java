/**
 * Created by lux on 18.01.17.
 */
public class TokenModel {
    private String token;
    private Long userId;

    public TokenModel() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
