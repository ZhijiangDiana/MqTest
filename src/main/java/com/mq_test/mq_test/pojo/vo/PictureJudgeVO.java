package com.mq_test.mq_test.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author 嘉然今天吃向晚
 * @Date 2024/5/15-17:07:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PictureJudgeVO {
    private String url;
    private String suggestion;
    private Float rate;
    private String label;
}
