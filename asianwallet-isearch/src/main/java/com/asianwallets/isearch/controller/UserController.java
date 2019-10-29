package com.asianwallets.isearch.controller;
import com.asianwallets.common.response.BaseResponse;
import com.asianwallets.common.response.ResultUtil;
import com.asianwallets.isearch.dto.UserDTO;
import com.asianwallets.isearch.dto.UserWebPoolDTO;
import com.asianwallets.isearch.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: YangXu
 * @create: 2019-09-25 15:02
 **/
@RestController
@RequestMapping("/user")
@Api(description = "用户管理")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/select")
    @ApiOperation(value = "分页查询用户信息")
    @CrossOrigin
    public BaseResponse select(@RequestBody UserDTO userDTO) {
        return ResultUtil.success(userService.select(userDTO));
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册用户")
    @CrossOrigin
    public BaseResponse register(@RequestBody UserDTO userDTO) {
        userService.register(userDTO);
        return ResultUtil.success();
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    @CrossOrigin
    public BaseResponse login(@RequestBody UserDTO userDTO) {
        return ResultUtil.success(userService.login(userDTO));
    }

    @PostMapping("/updateUser")
    @ApiOperation(value = "更新用户信息")
    @CrossOrigin
    public BaseResponse updateUser(@RequestBody UserDTO userDTO) {
        return ResultUtil.success(userService.updateUser(userDTO));
    }

    @PostMapping("/addUserUrl")
    @ApiOperation(value = "用户新增网站")
    @CrossOrigin
    public BaseResponse addUrl(@RequestBody UserWebPoolDTO userWebPoolDTO) {
        return ResultUtil.success(userService.addUrl(userWebPoolDTO));
    }

    @PostMapping("/selectUserUrl")
    @ApiOperation(value = "查询用户开通网站")
    @CrossOrigin
    public BaseResponse selectUserUrl(@RequestBody UserWebPoolDTO userWebPoolDTO) {
        return ResultUtil.success(userService.selectUserUrl(userWebPoolDTO));
    }

    @PostMapping("/updateUserUrl")
    @ApiOperation(value = "更新用户开通的网站")
    @CrossOrigin
    public BaseResponse updateUserUrl(@RequestBody UserWebPoolDTO userWebPoolDTO) {
        return ResultUtil.success(userService.updateUserUrl(userWebPoolDTO));
    }

    @PostMapping("/deleteUserUrl")
    @ApiOperation(value = "删除用户开通的网站")
    @CrossOrigin
    public BaseResponse deleteUserUrl(@RequestBody UserWebPoolDTO userWebPoolDTO) {
        return ResultUtil.success(userService.deleteUserUrl(userWebPoolDTO));
    }

    @PostMapping("/updatepwd")
    @ApiOperation(value = "修改密码")
    @CrossOrigin
    public BaseResponse updatepwd(@RequestBody UserDTO userDTO) {
        return ResultUtil.success(userService.updatepwd(userDTO));
    }

}
