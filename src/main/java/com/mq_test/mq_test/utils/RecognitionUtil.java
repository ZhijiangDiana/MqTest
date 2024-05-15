package com.mq_test.mq_test.utils;

import com.aliyun.imagerecog20190930.models.ClassifyingRubbishResponse;
import com.aliyun.imagerecog20190930.models.ClassifyingRubbishResponseBody;
import com.aliyun.imagerecog20190930.models.RecognizeFoodResponse;
import com.aliyun.imagerecog20190930.models.RecognizeFoodResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.TeaModel;
import com.mq_test.mq_test.pojo.dto.RecognitionDTO;
import com.mq_test.mq_test.pojo.vo.MealRecoVO;
import com.mq_test.mq_test.pojo.vo.RubbishRecoVO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author 嘉然今天吃向晚
 * @Date 2024/5/15-10:10:07
 */
@Component
@Slf4j
public class RecognitionUtil {

    @Value("${aliyun.access-key-id}")
    private String accessKeyId;
    @Value("${aliyun.access-key-secret}")
    private String accessKeySecret;
    @Value("${aliyun.recognition.endpoint}")
    private String endpoint;
    @Value("${aliyun.recognition.region-id}")
    private String regionId;

    private static com.aliyun.imagerecog20190930.Client client;

    @PostConstruct
    public void initialize() throws Exception {
        /*
          初始化配置对象com.aliyun.teaopenapi.models.Config
          Config对象存放 AccessKeyId、AccessKeySecret、endpoint等配置
         */
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setEndpoint(endpoint)
                .setRegionId(regionId);
        client = new com.aliyun.imagerecog20190930.Client(config);
    }

    public MealRecoVO mealReco(RecognitionDTO recognitionDTO) throws Exception {
        // 创建AccessKey ID和AccessKey Secret，请参考https://help.aliyun.com/document_detail/175144.html。
        // 如果您使用的是RAM用户的AccessKey，还需要为子账号授予权限AliyunVIAPIFullAccess，请参考https://help.aliyun.com/document_detail/145025.html
        com.aliyun.imagerecog20190930.models.RecognizeFoodRequest recognizeFoodRequest = new com.aliyun.imagerecog20190930.models.RecognizeFoodRequest()
                .setImageURL(recognitionDTO.getImageURL());
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        RecognizeFoodResponse recognizeFoodResponse = client.recognizeFoodWithOptions(recognizeFoodRequest, runtime);
        // 获取单个字段
        RecognizeFoodResponseBody.RecognizeFoodResponseBodyDataTopFives mostProb = recognizeFoodResponse.getBody().getData().getTopFives().get(0);

        return MealRecoVO.builder()
                .category(mostProb.getCategory())
                .calorie(mostProb.getCalorie())
                .score(String.valueOf(mostProb.getScore()))
                .build();
    }

    public RubbishRecoVO rubbishReco(RecognitionDTO recognitionDTO) throws Exception {
        // 创建AccessKey ID和AccessKey Secret，请参考https://help.aliyun.com/document_detail/175144.html
        // 如果您使用的是RAM用户的AccessKey，还需要为子账号授予权限AliyunVIAPIFullAccess，请参考https://help.aliyun.com/document_detail/145025.html
        com.aliyun.imagerecog20190930.models.ClassifyingRubbishRequest classifyingRubbishRequest = new com.aliyun.imagerecog20190930.models.ClassifyingRubbishRequest()
                .setImageURL(recognitionDTO.getImageURL());
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        ClassifyingRubbishResponse classifyingRubbishResponse = client.classifyingRubbishWithOptions(classifyingRubbishRequest, runtime);
        // 获取单个字段
        ClassifyingRubbishResponseBody.ClassifyingRubbishResponseBodyDataElements mostProb = classifyingRubbishResponse.getBody().data.elements.get(0);

        RubbishRecoVO rubbishRecoVO = new RubbishRecoVO();
        BeanUtils.copyProperties(mostProb, rubbishRecoVO);
        return rubbishRecoVO;
//        return RubbishRecoVO.builder()
//                .rubbish(mostProb.rubbish)
//                .rubbishScore(mostProb.rubbishScore)
//                .category(mostProb.category)
//                .categoryScore(mostProb.categoryScore)
//                .build();
    }
}
