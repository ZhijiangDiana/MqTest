package com.mq_test.mq_test.controller;

import com.alibaba.fastjson2.JSON;
import com.mq_test.mq_test.pojo.Result;
import com.mq_test.mq_test.pojo.dto.RecognitionDTO;
import com.mq_test.mq_test.pojo.vo.MealRecoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @Description
 * @Author 嘉然今天吃向晚
 * @Date 2024/5/15-01:23:38
 */
@RestController
@RequestMapping("/recognition")
@Tag(name = "识别相关接口")
@Slf4j
public class CVController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/meal")
    @Operation(tags = "菜品识别接口")
    public Result<String> mealRecognition(@RequestBody RecognitionDTO recognitionDTO) {
        recognitionDTO.setAction("RecognizeFood");
        String name = "mealReco.exchange";
        Message message = MessageBuilder
                .withBody(JSON.toJSONString(recognitionDTO).getBytes(StandardCharsets.UTF_8))
                .build();
        rabbitTemplate.convertAndSend(name, "114514", message);

        return Result.success();
    }

    @PostMapping("/rubbish")
    @Operation(tags = "垃圾分类接口")
    public Result<String> rubbishRecognition(@RequestBody RecognitionDTO recognitionDTO) {
        recognitionDTO.setAction("ClassifyingRubbish");
        String name = "rubbishReco.exchange";
        Message message = MessageBuilder
                .withBody(JSON.toJSONString(recognitionDTO).getBytes(StandardCharsets.UTF_8))
                .build();
        rabbitTemplate.convertAndSend(name, "114514", message);

        return Result.success();
    }
}
