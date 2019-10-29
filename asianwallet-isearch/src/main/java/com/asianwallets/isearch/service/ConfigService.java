package com.asianwallets.isearch.service;
import com.asianwallets.isearch.dto.ConfigDTO;

/**
 * @description: 必应业务接口
 * @author: XuWenQi
 * @create: 2019-09-29 11:27
 **/
public interface ConfigService {
    /**
     * 添加配置
     *
     * @param configDTO configDTO
     * @return int
     */
    void updateConfig(ConfigDTO configDTO);

    /**
     * 修改配置
     *
     * @param configDTO configDTO
     * @return int
     */
    void insertConfig(ConfigDTO configDTO);
}
