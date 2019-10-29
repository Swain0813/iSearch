package com.asianwallets.isearch.controller;
import com.asianwallets.common.response.BaseResponse;
import com.asianwallets.common.response.ResultUtil;
import com.asianwallets.isearch.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 图片上传
 */
@RestController
@Api(description ="图片上传接口")
@RequestMapping("/upload")
public class FileUploadController{

    @Autowired
    private FileUploadService fileUploadService;


    @Value("${file.tmpfile}")
    private String tmpfile;//springboot启动的临时文件存放

    @ApiOperation(value = "图片上传")
    @PostMapping("/image")
    @CrossOrigin
    public BaseResponse uploadImage(@RequestParam(value = "file",required = false) @ApiParam MultipartFile file){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(tmpfile);//指定临时文件路径，这个路径可以随便写
        factory.createMultipartConfig();
        return ResultUtil.success(fileUploadService.uploadImage(file));
    }
}
