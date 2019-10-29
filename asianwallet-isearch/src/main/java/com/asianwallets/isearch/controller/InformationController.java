package com.asianwallets.isearch.controller;
import com.asianwallets.common.response.BaseResponse;
import com.asianwallets.common.response.ResultUtil;
import com.asianwallets.isearch.dto.DeleteInformationDTO;
import com.asianwallets.isearch.dto.InformationDTO;
import com.asianwallets.isearch.dto.InformationHistoryDTO;
import com.asianwallets.isearch.service.InformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: shenxinran
 * @create: 2019年9月29日10:10:01
 **/
@RestController
@RequestMapping("/information")
@Api(description = "新闻")
public class InformationController {

    @Autowired
    private InformationService informationService;

    @PostMapping("/selectNewestInformation")
    @ApiOperation(value = "查询最新新闻")
    @CrossOrigin
    public BaseResponse selectNewestInformation(@RequestBody InformationDTO informationDTO) {
        return ResultUtil.success(informationService.selectNewestInformation(informationDTO));
    }

    @PostMapping("/selectUserInformation")
    @ApiOperation(value = "根据用户的关键字查询新闻")
    @CrossOrigin
    public BaseResponse selectUserInformation(@RequestBody InformationDTO informationDTO) {
        return ResultUtil.success(informationService.selectUserInformation(informationDTO));
    }

    @PostMapping("/addUserInformationHistory")
    @ApiOperation(value = "新增用户查询的新闻")
    @CrossOrigin
    public BaseResponse addUserInformationHistory(@RequestBody InformationHistoryDTO informationHistory) {
        informationService.addUserInformationHistory(informationHistory);
        return ResultUtil.success();
    }

    @PostMapping("/selectUserInformationHistory")
    @ApiOperation(value = "查询用户历史新闻")
    @CrossOrigin
    public BaseResponse selectUserInformationHistory(@RequestBody InformationDTO informationDTO) {
        return ResultUtil.success(informationService.selectUserInformationHistory(informationDTO));
    }

    @PostMapping("deleteUserInformationHistory")
    @ApiOperation(value = "删除用户历史新闻")
    @CrossOrigin
    public BaseResponse deleteUserInformationHistory(@RequestBody DeleteInformationDTO deleteInformationDTO) {
        return ResultUtil.success(informationService.deleteUserInformationHistory(deleteInformationDTO));
    }
}
