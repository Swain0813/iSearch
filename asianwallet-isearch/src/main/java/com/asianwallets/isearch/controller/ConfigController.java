package com.asianwallets.isearch.controller;
import com.asianwallets.common.response.BaseResponse;
import com.asianwallets.common.response.ResultUtil;
import com.asianwallets.isearch.dto.ConfigDTO;
import com.asianwallets.isearch.service.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: XuWenQi
 * @create: 2019-10-09 11:44
 **/
@RestController
@RequestMapping("/config")
@Api(description = "微信并购汪配置")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @PostMapping("/insertConfig")
    @ApiOperation(value = "添加微信并购汪配置")
    @CrossOrigin
    public BaseResponse insertConfig(@RequestBody ConfigDTO configDTO) {
        configService.insertConfig(configDTO);
        return ResultUtil.success();
    }

    @PostMapping("/updateConfig")
    @ApiOperation(value = "修改微信并购汪配置")
    @CrossOrigin
    public BaseResponse updateConfig(@RequestBody ConfigDTO configDTO) {
        configService.updateConfig(configDTO);
        return ResultUtil.success();
    }
}
