package com.mq_test.mq_test.mq;

import com.alibaba.fastjson2.JSON;
import com.google.common.util.concurrent.RateLimiter;
import com.mq_test.mq_test.pojo.Result;
import com.mq_test.mq_test.pojo.dto.RecognitionDTO;
import com.mq_test.mq_test.pojo.vo.MealRecoVO;
import com.mq_test.mq_test.utils.RecognitionUtil;
import com.mq_test.mq_test.ws.ReturnWs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Description
 * @Author 嘉然今天吃向晚
 * @Date 2024/5/15-01:23:23
 */
@Component
@Slf4j
public class RecognitionListener {

    @Autowired
    private ReturnWs returnWs;

    @Autowired
    private RecognitionUtil recognitionUtil;
    private final RateLimiter mealRecoLimiter = RateLimiter.create(1);

    @RabbitListener(queues = "mealReco.queue", concurrency = "1")
    public void mealRecoListener(String message) throws IOException {
        // 检查令牌是否可用，如果不可用则阻塞直到获取到令牌
        mealRecoLimiter.acquire();
        try {
            RecognitionDTO recognitionDTO = JSON.parseObject(message, RecognitionDTO.class);
            MealRecoVO mealRecoVO = recognitionUtil.mealReco(recognitionDTO);
//            log.info(mealRecoVO.toString());

            returnWs.returnStringResult(recognitionDTO.getCid(), JSON.toJSONString(Result.success(mealRecoVO)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
