package com.mq_test.mq_test.utils;

import com.aliyun.imageaudit20191230.models.ScanImageAdvanceRequest;
import com.aliyun.imageaudit20191230.models.ScanImageResponse;
import com.aliyun.imageaudit20191230.models.ScanImageResponseBody;
import com.aliyun.tea.TeaModel;
import com.mq_test.mq_test.pojo.dto.PictureJudgeDTO;
import com.mq_test.mq_test.pojo.vo.PictureJudgeVO;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author 嘉然今天吃向晚
 * @Date 2024/5/15-11:59:00
 */
@Component
public class PicJudgeUtil {

    @Value("${aliyun.access-key-id}")
    protected String accessKeyId;
    @Value("${aliyun.access-key-secret}")
    protected String accessKeySecret;
    @Value("${aliyun.recognition.endpoint}")
    protected String endpoint;
    @Value("${aliyun.recognition.region-id}")
    protected String regionId;

    protected static com.aliyun.imageaudit20191230.Client client;

    @PostConstruct
    public void initialize() throws Exception {
        /*
          初始化配置对象com.aliyun.teaopenapi.model.Config
          Config对象存放 AccessKeyId、AccessKeySecret、endpoint等配置
         */
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setEndpoint(endpoint)
                .setRegionId(regionId);
        client = new com.aliyun.imageaudit20191230.Client(config);
    }

    public List<PictureJudgeVO> picJudge(List<PictureJudgeDTO> dtos) throws Exception {
        // 创建AccessKey ID和AccessKey Secret，请参考https://help.aliyun.com/document_detail/175144.html
        // 如果您使用的是RAM用户的AccessKey，还需要为子账号授予权限AliyunVIAPIFullAccess，请参考https://help.aliyun.com/document_detail/145025.html
        List<ScanImageAdvanceRequest.ScanImageAdvanceRequestTask> taskList = new ArrayList<>();

        for (PictureJudgeDTO dto : dtos) {
            URL url = new URL(dto.getImageURL());
            InputStream inputStream = url.openConnection().getInputStream();
            ScanImageAdvanceRequest.ScanImageAdvanceRequestTask task = new ScanImageAdvanceRequest.ScanImageAdvanceRequestTask();
            task.setImageURLObject(inputStream);
            taskList.add(task);
        }

        List<String> sceneList = new ArrayList<>();
        sceneList.add("porn");
        com.aliyun.imageaudit20191230.models.ScanImageAdvanceRequest scanImageAdvanceRequest = new com.aliyun.imageaudit20191230.models.ScanImageAdvanceRequest()
                .setTask(taskList)
                .setScene(sceneList);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        ScanImageResponse scanImageResponse = client.scanImageAdvance(scanImageAdvanceRequest, runtime);
        // 获取整体结果
//        System.out.println(com.aliyun.teautil.Common.toJSONString(TeaModel.buildMap(scanImageResponse)));
        // 获取单个字段
        List<ScanImageResponseBody.ScanImageResponseBodyDataResults> res = scanImageResponse.getBody().getData().getResults();
//        System.out.println(res);

        List<PictureJudgeVO> ret = new ArrayList<>();
        for (ScanImageResponseBody.ScanImageResponseBodyDataResults e : res) {
            ScanImageResponseBody.ScanImageResponseBodyDataResultsSubResults subRes = e.getSubResults().get(0);
            ret.add(PictureJudgeVO.builder()
                    .url(e.getImageURL())
                    .suggestion(subRes.getSuggestion())
                    .label(subRes.getLabel())
                    .rate(subRes.getRate())
                    .build());
        }
        return ret;
    }
}
