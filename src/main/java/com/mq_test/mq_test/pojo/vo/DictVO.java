package com.mq_test.mq_test.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author 嘉然今天吃向晚
 * @Date 2024/5/15-19:33:43
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DictVO {
    private String key;
    private String value;
}
