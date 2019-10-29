package com.asianwallets.isearch.service;

import com.asianwallets.common.entity.WebPool;
import com.asianwallets.isearch.dto.WebPoolDTO;
import org.springframework.data.domain.Page;

/**
 * 搜索网站信息管理服务
 */
public interface WebPoolService {
    /**
     * 新增搜索网站信息
     * @param webPoolDTO
     */
    void  addWebPool(WebPoolDTO webPoolDTO);

    /**
     * 修改搜索网站信息
     * @param webPoolDTO
     * @return
     */
   long updateWebPool(WebPoolDTO webPoolDTO);

    /**
     * 分页查询搜索网站信息
     * @param webPoolDTO
     * @return
     */
    Page<WebPool> selectWebPool(WebPoolDTO webPoolDTO);
}
