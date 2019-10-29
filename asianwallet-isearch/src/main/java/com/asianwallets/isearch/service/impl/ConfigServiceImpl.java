package com.asianwallets.isearch.service.impl;

import com.asianwallets.common.constant.AsianWalletConstant;
import com.asianwallets.isearch.dto.ConfigDTO;
import com.asianwallets.isearch.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;


@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 修改配置
     *
     * @param configDTO configDTO
     * @return int
     */
    @Override
    public void updateConfig(ConfigDTO configDTO) {
        Query query = new Query(Criteria.where("configId").is(configDTO.getConfigId()));
        Update update = new Update();
        update.set("token", configDTO.getToken());
        update.set("cookie", configDTO.getCookie());
        mongoTemplate.updateFirst(query, update, AsianWalletConstant.CONFIG);
    }


    /**
     * 添加配置
     *
     * @param configDTO configDTO
     * @return int
     */
    @Override
    public void insertConfig(ConfigDTO configDTO) {
        mongoTemplate.save(configDTO, AsianWalletConstant.CONFIG);
    }

}
