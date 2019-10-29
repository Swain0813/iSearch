package com.asianwallets.isearch.service;

import com.asianwallets.common.entity.Information;

import java.util.List;

/**
 * @description: 必应业务接口
 * @author: XuWenQi
 * @create: 2019-09-29 11:27
 **/
public interface BingService {

    /**
     * 根据关键字调用必应API搜索
     *
     * @param key 关键字
     * @return informationList
     */
    List<Information> search(String key);
}
