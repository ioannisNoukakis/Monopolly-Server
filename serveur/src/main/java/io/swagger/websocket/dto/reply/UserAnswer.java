package io.swagger.websocket.dto.reply;

import com.cristallium.api.dto.StatisticalQuestion;

/**
 * Created by lux on 17.01.17.
 */
public class UserAnswer extends BaseReply {
    private StatisticalQuestion statisticalQuestion;


    public UserAnswer(StatisticalQuestion statisticalQuestion) {
        super("UserAnswer");
        this.statisticalQuestion = statisticalQuestion;
    }

    public StatisticalQuestion getStatisticalQuestion() {
        return statisticalQuestion;
    }

    public void setStatisticalQuestion(StatisticalQuestion statisticalQuestion) {
        this.statisticalQuestion = statisticalQuestion;
    }
}
