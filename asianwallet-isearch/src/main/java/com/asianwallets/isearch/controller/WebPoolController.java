package com.asianwallets.isearch.controller;
import com.asianwallets.common.response.BaseResponse;
import com.asianwallets.common.response.ResultUtil;
import com.asianwallets.isearch.dto.WebPoolDTO;
import com.asianwallets.isearch.service.WebPoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 搜索网站信息管理
 */
@RestController
@RequestMapping("/pool")
@Api(description = "搜索网站信息管理")
public class WebPoolController {

    @Autowired
    private WebPoolService webPoolService;


    @PostMapping("/selectWebPool")
    @ApiOperation(value = "分页查询搜索网站信息")
    @CrossOrigin
    public BaseResponse selectWebPool(@RequestBody WebPoolDTO webPoolDTO) {
        return ResultUtil.success(webPoolService.selectWebPool(webPoolDTO));
    }

    @PostMapping("/addWebPool")
    @ApiOperation(value = "新增搜索网站信息")
    @CrossOrigin
    public BaseResponse addWebPool(@RequestBody WebPoolDTO webPoolDTO) {
        webPoolService.addWebPool(webPoolDTO);
        return ResultUtil.success();
    }

    @PostMapping("/updateWebPool")
    @ApiOperation(value = "修改搜索网站信息")
    @CrossOrigin
    public BaseResponse updateWebPool(@RequestBody WebPoolDTO webPoolDTO) {
        webPoolService.updateWebPool(webPoolDTO);
        return ResultUtil.success();
    }

}
