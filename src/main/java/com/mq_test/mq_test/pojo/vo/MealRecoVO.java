package com.mq_test.mq_test.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Author 嘉然今天吃向晚
 * @Date 2024/5/15-01:28:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MealRecoVO implements Serializable {
    private String category;
    private String score;
    private String calorie;
}
