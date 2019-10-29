package com.asianwallets.isearch.service.impl;

import com.asianwallets.common.constant.AsianWalletConstant;
import com.asianwallets.common.entity.Information;
import com.asianwallets.common.entity.WebPool;
import com.asianwallets.common.exception.BusinessException;
import com.asianwallets.common.response.EResultEnum;
import com.asianwallets.common.utils.BeanToMapUtil;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.isearch.dto.WebPoolDTO;
import com.asianwallets.isearch.service.WebPoolService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 搜索网站信息管理服务的实现类
 */
@Service
public class WebPoolServiceImpl implements WebPoolService {

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 分页查询搜索网站信息
     *
     * @param webPoolDTO
     * @return
     */
    @Override
    public Page<WebPool> selectWebPool(WebPoolDTO webPoolDTO) {
        Query query = new Query(Criteria.where("webName").regex(".*?" + webPoolDTO.getWebName() + ".*"));
        //查询时 条件仅为 true 的 ，才能被查询到
        query.addCriteria(Criteria.where("enabled").is(true));
        Sort sort = new Sort(webPoolDTO.getOrder(), "createTime");
        PageRequest pageRequest = new PageRequest(webPoolDTO.getPageNum() - 1, webPoolDTO.getPageSize(), sort);
        query.with(pageRequest);
        int count = (int) mongoTemplate.count(query, Information.class, AsianWalletConstant.INFORMATION);
        List<WebPool> webPoolList = mongoTemplate.find(query, WebPool.class);
        return PageableExecutionUtils.getPage(webPoolList, pageRequest, () -> count);
    }

    /**
     * 新增搜索网站信息
     *
     * @param webPoolDTO
     */
    @Override
    public void addWebPool(WebPoolDTO webPoolDTO) {
        if (StringUtils.isBlank(webPoolDTO.getWebName()) || StringUtils.isBlank(webPoolDTO.getWebAddress())) {
            //输入参数不能为空
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        if (mongoTemplate.findOne(new Query(Criteria.where("webName").is(webPoolDTO.getWebName())), WebPool.class) != null) {
            //网站名称已存在
            throw new BusinessException(EResultEnum.WEB_NAME_IS_EXIST.getCode());
        }
        if (mongoTemplate.findOne(new Query(Criteria.where("webAddress").is(webPoolDTO.getWebAddress())), WebPool.class) != null) {
            //网站链接地址已存在
            throw new BusinessException(EResultEnum.WEB_ADDRESS_IS_EXIST.getCode());
        }
        //创建新增搜索网站信息对象
        WebPool webPool = new WebPool();
        BeanUtils.copyProperties(webPoolDTO, webPool);
        //网站id
        webPool.setWebId(IDS.uniqueID().toString());
        //创建时间
        webPool.setCreateTime(new Date());
        //默认是禁用新建的网站
        webPool.setEnabled(true);
        //新增新增搜索网站信息
        mongoTemplate.save(webPool);
    }

    /**
     * 修改搜索网站信息
     *
     * @param webPoolDTO
     * @return
     */
    @Override
    public long updateWebPool(WebPoolDTO webPoolDTO) {
        if (StringUtils.isBlank(webPoolDTO.getWebId())) {
            //输入参数不能为空
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        if (mongoTemplate.findOne(new Query(Criteria.where("webName").is(webPoolDTO.getWebName())), WebPool.class) != null) {
            //网站名称已存在
            throw new BusinessException(EResultEnum.WEB_NAME_IS_EXIST.getCode());
        }
        if (mongoTemplate.findOne(new Query(Criteria.where("webAddress").is(webPoolDTO.getWebAddress())), WebPool.class) != null) {
            //网站链接地址已存在
            throw new BusinessException(EResultEnum.WEB_ADDRESS_IS_EXIST.getCode());
        }
        //创建新增搜索网站信息对象
        WebPool webPool = new WebPool();
        BeanUtils.copyProperties(webPoolDTO, webPool);
        Query query = new Query(Criteria.where("webId").is(webPoolDTO.getWebId()));
        Update update = new Update();
        HashMap<String, Object> map = BeanToMapUtil.beanToMapWithoutNull(webPool);
        map.forEach(update::set);
        return mongoTemplate.updateFirst(query, update, AsianWalletConstant.WEB_POOL).getModifiedCount();
    }
}
