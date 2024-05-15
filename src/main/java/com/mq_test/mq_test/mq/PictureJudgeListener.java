package com.mq_test.mq_test.mq;

import com.alibaba.fastjson2.JSON;
import com.google.common.util.concurrent.RateLimiter;
import com.mq_test.mq_test.pojo.Result;
import com.mq_test.mq_test.pojo.dto.PictureJudgeDTO;
import com.mq_test.mq_test.pojo.dto.RecognitionDTO;
import com.mq_test.mq_test.pojo.vo.MealRecoVO;
import com.mq_test.mq_test.pojo.vo.PictureJudgeVO;
import com.mq_test.mq_test.utils.PicJudgeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * @Description
 * @Author 嘉然今天吃向晚
 * @Date 2024/5/15-17:38:00
 */
@Component
@Slf4j
public class PictureJudgeListener {

    @Autowired
    private PicJudgeUtil picJudgeUtil;

    private static final RateLimiter judgeLimiter = RateLimiter.create(0.1);

    private static final List<PictureJudgeDTO> batch = new Vector<>();

    private static final LocalDateTime previousJudgeTime = LocalDateTime.now();

    @RabbitListener(queues = "pictureJudge.queue", concurrency = "1")
    public void pictureJudgeListener(String message) throws IOException {
        judgeLimiter.acquire();

        try {
            PictureJudgeDTO pictureJudgeDTO = JSON.parseObject(message, PictureJudgeDTO.class);
            batch.add(pictureJudgeDTO);
            if (batch.size() == 10 || LocalDateTime.now().isAfter(previousJudgeTime.plusSeconds(1))) {
                List<PictureJudgeVO> vos = picJudgeUtil.picJudge(batch);
                log.info(vos.toString());
            }

//            returnWs.returnStringResult(recognitionDTO.getCid(), JSON.toJSONString(Result.success(mealRecoVO)));
        } catch (com.aliyun.tea.TeaException teaException) {
            // 获取整体报错信息
            System.out.println(com.aliyun.teautil.Common.toJSONString(teaException));
            // 获取单个字段
            System.out.println(teaException.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
