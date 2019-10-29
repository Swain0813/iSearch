package com.asianwallets.isearch.service;

import com.asianwallets.isearch.dto.UserKeyWordDTO;

import java.util.List;

/**
 * 输入关键字服务
 */
public interface UserKeyWordService {

    /**
     * 新增输入关键字
     * @param userId
     * @param keyWords
     */
    void addUserKeyWord(String userId, String keyWords);

    /**
     * 查询用户的关键字
     *
     * @param userId
     * @return
     */
    List<String> selectUserKeyWord(String userId);

    /**
     * 删除用户的单个搜索关键字
     *
     * @param userKeyWordDTO
     * @return
     */
    long deleteUserKeyWord(UserKeyWordDTO userKeyWordDTO);

    /**
     * 删除用户的所有搜索关键字
     *
     * @param userId
     * @return
     */
    long deleteUserAllKeyWord(String userId);

    /**
     * 查询热词
     *
     * @return
     */
    List<String> queryHotWords();
}
