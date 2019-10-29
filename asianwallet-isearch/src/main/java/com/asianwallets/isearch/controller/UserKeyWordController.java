package com.asianwallets.isearch.controller;
import com.asianwallets.common.response.BaseResponse;
import com.asianwallets.common.response.ResultUtil;
import com.asianwallets.isearch.dto.UserKeyWordDTO;
import com.asianwallets.isearch.service.UserKeyWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: shenxinran
 * @create: 2019-09-25 15:02
 **/
@RestController
@RequestMapping("/userKeyWord")
@Api(description = "用户搜索关键字")
public class UserKeyWordController {

    @Autowired
    private UserKeyWordService userKeyWordService;

    @GetMapping("/selectUserKeyWord")
    @ApiOperation(value = "查询用户搜索关键字")
    @CrossOrigin
    public BaseResponse selectUserKeyWord(@RequestParam("userId") String userId) {
        return ResultUtil.success(userKeyWordService.selectUserKeyWord(userId));
    }

    @PostMapping("/deleteUserKeyWord")
    @ApiOperation(value = "删除用户搜索的单个关键字")
    @CrossOrigin
    public BaseResponse deleteUserKeyWord(@RequestBody UserKeyWordDTO userKeyWordDTO) {
        return ResultUtil.success(userKeyWordService.deleteUserKeyWord(userKeyWordDTO));
    }

    @GetMapping("/deleteUserAllKeyWord")
    @ApiOperation(value = "删除用户搜索的所有关键字")
    @CrossOrigin
    public BaseResponse deleteUserAllKeyWord(@RequestParam("userId") String userId) {
        return ResultUtil.success(userKeyWordService.deleteUserAllKeyWord(userId));
    }

    @GetMapping("/queryHotWords")
    @ApiOperation(value = "查询热词")
    @CrossOrigin
    public BaseResponse queryHotWords() {
        return ResultUtil.success(userKeyWordService.queryHotWords());
    }
}
