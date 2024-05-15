package com.mq_test.mq_test.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author 嘉然今天吃向晚
 * @Date 2024/5/15-11:07:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RubbishRecoVO {
    private Float categoryScore;
    private String rubbish;
    private Float rubbishScore;
    private String category;
}
