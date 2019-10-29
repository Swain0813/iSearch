package com.asianwallets.isearch.service;

import com.asianwallets.common.entity.User;
import com.asianwallets.isearch.dto.UserDTO;
import com.asianwallets.isearch.dto.UserWebPoolDTO;
import com.asianwallets.isearch.vo.UserWebPoolVO;
import org.springframework.data.domain.Page;

public interface UserService {

    /**
     * 分页查询用户信息
     *
     * @param userDTO
     * @return
     */
    Page<User> select(UserDTO userDTO);

    /**
     * 用户注册
     * @param userDTO
     */
    void register(UserDTO userDTO);

    /**
     * 登录
     *
     * @param userDTO
     * @return
     */
    User login(UserDTO userDTO);

    /**
     * 更新用户信息
     *
     * @param userDTO
     * @return
     */
    long updateUser(UserDTO userDTO);

    /**
     * 用户新增网站
     *
     * @param userWebPoolDTO
     * @return
     */
    long addUrl(UserWebPoolDTO userWebPoolDTO);

    /**
     * 查询用户开通的网站
     *
     * @param userWebPoolDTO
     * @return
     */
    UserWebPoolVO selectUserUrl(UserWebPoolDTO userWebPoolDTO);

    /**
     * 修改用户开通的网站
     *
     * @param userWebPoolDTO
     * @return
     */
    long updateUserUrl(UserWebPoolDTO userWebPoolDTO);

    /**
     * 删除用户开通的网站
     *
     * @param userWebPoolDTO
     * @return
     */
    long deleteUserUrl(UserWebPoolDTO userWebPoolDTO);

    /**
     * 修改密码
     * @param userDTO
     * @return
     */
    long updatepwd(UserDTO userDTO);

}
