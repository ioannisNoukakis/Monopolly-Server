package io.swagger.websocket.dto.reply;

import com.cristallium.api.dto.CompleteAsnwer;
import com.cristallium.api.dto.User;

/**
 * Created by lux on 17.01.17.
 */
public class UserAnswer extends BaseReply {
    private Long userid;
    private String username;
    private CompleteAsnwer completeAnswer;

    public UserAnswer(Long userid, String username, CompleteAsnwer completeAnswer) {
        super("UserAnswer");
        this.userid = userid;
        this.username = username;
        this.completeAnswer = completeAnswer;
    }

    public UserAnswer() {
        super("UserAnswer");
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CompleteAsnwer getCompleteAnswer() {
        return completeAnswer;
    }

    public void setCompleteAnswer(CompleteAsnwer completeAnswer) {
        this.completeAnswer = completeAnswer;
    }
}
