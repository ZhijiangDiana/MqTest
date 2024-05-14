package com.mq_test.mq_test.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Author 嘉然今天吃向晚
 * @Date 2024/5/14-10:56:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranslateDTO implements Serializable {
    private String cid;
    private String FormatType;
    private String SourceLanguage;
    private String TargetLanguage;
    private String SourceText;
    private String Scene;
}
