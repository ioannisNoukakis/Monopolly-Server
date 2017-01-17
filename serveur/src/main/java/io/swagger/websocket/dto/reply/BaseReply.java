package io.swagger.websocket.dto.reply;

/**
 * Created by lux on 17.01.17.
 */
public class BaseReply {
    private String code;

    public BaseReply(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
