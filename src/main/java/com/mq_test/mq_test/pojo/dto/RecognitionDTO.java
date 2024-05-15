package com.mq_test.mq_test.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Author 嘉然今天吃向晚
 * @Date 2024/5/15-01:30:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecognitionDTO implements Serializable {
    private String cid;
    private String Action;
    private String ImageURL;
    private String Type;
}
