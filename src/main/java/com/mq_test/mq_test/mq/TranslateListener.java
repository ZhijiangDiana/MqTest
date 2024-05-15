package com.mq_test.mq_test.mq;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.filter.PascalNameFilter;
import com.google.common.util.concurrent.RateLimiter;
import com.mq_test.mq_test.pojo.dto.TranslateDTO;
import com.mq_test.mq_test.utils.Sender;
import com.mq_test.mq_test.ws.ReturnWs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class TranslateListener {

    @Value("${url.translate}")
    private String url;

    @Autowired
    private Sender sender;

    @Autowired
    private ReturnWs returnWs;
    private final RateLimiter rateLimiter = RateLimiter.create(1);

    @RabbitListener(queues = "translate.queue", concurrency = "1")
    public void translateListener(String message) throws IOException {
        // 检查令牌是否可用，如果不可用则阻塞直到获取到令牌
        rateLimiter.acquire();
        try {
            TranslateDTO translateDTO = JSON.parseObject(message, TranslateDTO.class);
            String bodyJson = JSON.toJSONString(translateDTO, new PascalNameFilter());
            String s = sender.sendPost(url, bodyJson);
            String res = (String) ((JSONObject) JSONObject.parse(s).get("Data")).get("Translated");
            log.info("{} -> {}", translateDTO.getSourceText(), res);
            returnWs.returnStringResult(translateDTO.getCid(), res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
