package com.mq_test.mq_test.controller;

import com.mq_test.mq_test.pojo.Result;
import com.mq_test.mq_test.utils.AliOssUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @Description
 * @Author 嘉然今天吃向晚
 * @Date 2024/4/28-22:55:43
 */
@RestController
@RequestMapping("/common")
@Tag(name = "公共接口")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @GetMapping("/dict/{dictName}")
    @Operation(summary = "获取字典键值对")
    public Result<List> getDictByDictName(@PathVariable String dictName) {
        // TODO 继续写
        return null;
    }

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public Result<String> uploadFile(@RequestBody MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String storageFileName = UUID.randomUUID() + ext;
        String url = aliOssUtil.upload(file.getBytes(), storageFileName);
        return Result.success(url);
    }
}
