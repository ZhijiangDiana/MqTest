package com.mq_test.mq_test.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description
 * @Author 嘉然今天吃向晚
 * @Date 2024/5/15-12:14:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PictureJudgeDTO {
    private String cid;
    private String ImageURL;
    private List<String> scene;
}
