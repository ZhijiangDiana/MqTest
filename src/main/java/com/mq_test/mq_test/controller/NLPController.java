package com.mq_test.mq_test.controller;

import com.alibaba.fastjson2.JSON;
import com.mq_test.mq_test.pojo.Result;
import com.mq_test.mq_test.pojo.dto.TranslateDTO;
import com.mq_test.mq_test.ws.TranslateWs;
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
 * @Date 2024/5/14-10:39:04
 */
@RestController
@RequestMapping("/NLP")
@Tag(name = "自然语言处理接口")
@Slf4j
public class NLPController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/translate")
    @Operation(summary = "文本翻译")
    public Result<String> translate(@RequestBody TranslateDTO translateDTO) {
        translateDTO.setFormatType("text");
        translateDTO.setScene("general");

        String name = "translate.exchange";
        Message message = MessageBuilder
                .withBody(JSON.toJSONString(translateDTO).getBytes(StandardCharsets.UTF_8))
                .build();
        rabbitTemplate.convertAndSend(name, "114514", message);

        return Result.success();
    }
}
