package com.asianwallets.isearch.service.impl;

import com.asianwallets.common.constant.AsianWalletConstant;
import com.asianwallets.common.entity.*;
import com.asianwallets.common.exception.BusinessException;
import com.asianwallets.common.response.EResultEnum;
import com.asianwallets.isearch.dao.InformationDao;
import com.asianwallets.isearch.dto.DeleteInformationDTO;
import com.asianwallets.isearch.dto.InformationDTO;
import com.asianwallets.isearch.dto.InformationHistoryDTO;
import com.asianwallets.isearch.service.BingService;
import com.asianwallets.isearch.service.InformationService;
import com.asianwallets.isearch.service.UserKeyWordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 新闻
 */
@Service
public class InformationServiceImpl implements InformationService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private InformationDao informationDao;

    @Autowired
    private UserKeyWordService userKeyWordService;

    @Autowired
    private BingService bingService;

    /**
     * 查询最新新闻
     *
     * @param informationDTO
     * @return
     */
    @Override
    public Page<Information> selectNewestInformation(InformationDTO informationDTO) {
        Information information = new Information();
        Example<Information> example = Example.of(information);
        Sort sort = new Sort(informationDTO.getOrder(), "createTime");
        PageRequest pageRequest = new PageRequest(informationDTO.getPageNum() - 1, informationDTO.getPageSize(), sort);
        return informationDao.findAll(example, pageRequest);
    }

    /**
     * 用户查询新闻
     *
     * @param informationDTO
     * @return
     */
    @Override
    public Page<Information> selectUserInformation(InformationDTO informationDTO) {
        //查询网站
        List<String> webNames = new ArrayList<>();
        //新增关键字
        if (!StringUtils.isBlank(informationDTO.getUserId())) {
            userKeyWordService.addUserKeyWord(informationDTO.getUserId(), informationDTO.getKeyWord());
            UserWebPool userWebPool = mongoTemplate.findOne(new Query(Criteria.where("userId").is(informationDTO.getUserId())), UserWebPool.class);
            webNames = userWebPool.getWebNames();
        } else {
            List<WebPool> webPoolLists = mongoTemplate.find(new Query(Criteria.where("enabled").is(true)), WebPool.class);
            for (WebPool webPoolList : webPoolLists) {
                webNames.add(webPoolList.getWebName());
            }
        }
        //搜索
        Query query = new Query(Criteria.where("title").regex(".*?" + informationDTO.getKeyWord() + ".*"));
        query.addCriteria(Criteria.where("website").in(webNames));
        Sort sort = new Sort(informationDTO.getOrder(), "createTime");
        PageRequest pageRequest = new PageRequest(informationDTO.getPageNum() - 1, informationDTO.getPageSize(), sort);
        query.with(pageRequest);
        int count = (int) mongoTemplate.count(query, Information.class, AsianWalletConstant.INFORMATION);
        List<Information> informationList = mongoTemplate.find(query, Information.class);
        if (informationList.size() == 0 || informationList.size() <= 10) {
            informationList.addAll(bingService.search(informationDTO.getKeyWord()));
        }
        return PageableExecutionUtils.getPage(informationList, pageRequest, () -> count);
    }

    /**
     * 新增用户点击过的新闻
     *
     * @param informationHistoryDTO
     */
    @Override
    public void addUserInformationHistory(InformationHistoryDTO informationHistoryDTO) {
        if (StringUtils.isBlank(informationHistoryDTO.getUserId())) {
            //输入参数不能为空
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        User user = mongoTemplate.findOne(new Query(Criteria.where("userId").is(informationHistoryDTO.getUserId())), User.class);
        if (user == null) {
            //用户不存在
            throw new BusinessException(EResultEnum.USER_DOES_NOT_EXIST.getCode());
        }
        Query query = new Query(Criteria.where("userId").is(informationHistoryDTO.getUserId()));
        InformationHistory informationHistory = mongoTemplate.findOne(query, InformationHistory.class);
        if (informationHistory != null) {
            List<Information> informationList = informationHistory.getInformationList();
            boolean flag = true;
            //防止重复新增历史记录
            for (int i = 0; i < informationList.size(); i++) {
                if (informationList.get(i).getMId().equals(informationHistoryDTO.getInformation().getMId())
                        || (informationList.get(i).getTitle().equals(informationHistoryDTO.getInformation().getTitle()) &&
                        informationList.get(i).getWebsite().equals(informationHistoryDTO.getInformation().getWebsite()))) {
                    //如果记录存在且list长度大于2，将历史记录提前一位
                    if (informationList.size() > 2 && i < informationList.size() - 1) {
                        Information information3 = informationList.get(i);
                        informationList.remove(i);
                        informationList.add(information3);
                        Query query1 = new Query(Criteria.where("userId").is(informationHistoryDTO.getUserId()));
                        Update u = new Update().set("informationList", informationList);
                        mongoTemplate.updateFirst(query1, u, AsianWalletConstant.INFORMATION_HISTORY);
                    }
                    flag = false;
                    break;
                }
            }
            if (flag) {
                informationList.add(informationHistoryDTO.getInformation());
                Query q = new Query(Criteria.where("userId").is(informationHistoryDTO.getUserId()));
                Update u = new Update().set("informationList", informationList);
                mongoTemplate.updateFirst(q, u, AsianWalletConstant.INFORMATION_HISTORY);
            }
        } else {
            InformationHistory newInformationHistory = new InformationHistory();
            newInformationHistory.setUserId(informationHistoryDTO.getUserId());
            ArrayList<Information> information = new ArrayList<>();
            information.add(informationHistoryDTO.getInformation());
            newInformationHistory.setInformationList(information);
            mongoTemplate.save(newInformationHistory);
        }
    }

    /**
     * 查询用户历史新闻 分页
     *
     * @param informationDTO
     * @return
     */
    @Override
    public Page<Information> selectUserInformationHistory(InformationDTO informationDTO) {
        if (StringUtils.isBlank(informationDTO.getUserId())) {
            //输入参数不能为空
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        User user = mongoTemplate.findOne(new Query(Criteria.where("userId").is(informationDTO.getUserId())), User.class);
        if (user == null) {
            //用户不存在
            throw new BusinessException(EResultEnum.USER_DOES_NOT_EXIST.getCode());
        }
        Query query = new Query(Criteria.where("userId").is(informationDTO.getUserId()));
        InformationHistory informationHistory = mongoTemplate.findOne(query, InformationHistory.class);
        if (informationHistory == null || informationHistory.getInformationList() == null) {
            return null;
        }
        //如果keyWord为空，则查询所有历史记录
        if (StringUtils.isBlank(informationDTO.getKeyWord())) {
            List<Information> informationList = informationHistory.getInformationList();
            Collections.reverse(informationList);
            Sort sort = new Sort(informationDTO.getOrder(), "createTime");
            PageRequest pageRequest = new PageRequest(informationDTO.getPageNum() - 1, informationDTO.getPageSize(), sort);
            return PageableExecutionUtils.getPage(informationList, pageRequest, informationList::size);
        } else {
            //不为空，模糊搜索
            //搜索
            Query q = new Query(Criteria.where("title").regex(".*?" + informationDTO.getKeyWord() + ".*"));
            Sort sort = new Sort(informationDTO.getOrder(), "createTime");
            PageRequest pageRequest = new PageRequest(informationDTO.getPageNum() - 1, informationDTO.getPageSize(), sort);
            q.with(pageRequest);
            int count = (int) mongoTemplate.count(q, Information.class, AsianWalletConstant.INFORMATION_HISTORY);
            List<Information> informationList = mongoTemplate.find(q, Information.class);
            Collections.reverse(informationList);
            return PageableExecutionUtils.getPage(informationList, pageRequest, () -> count);
        }
    }

    /**
     * 删除用户的历史记录
     *
     * @param deleteInformationDTO
     * @return
     */
    @Override
    public long deleteUserInformationHistory(DeleteInformationDTO deleteInformationDTO) {
        if (StringUtils.isBlank(deleteInformationDTO.getUserId())) {
            //输入参数不能为空
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        InformationHistory informationHistory = mongoTemplate.findOne(new Query(Criteria.where("userId").is(deleteInformationDTO.getUserId())), InformationHistory.class);
        if (informationHistory == null) {
            return 0;
        }
        List<Information> informationList = informationHistory.getInformationList();
        if (deleteInformationDTO.getMIds() == null || deleteInformationDTO.getMIds().size() == 0) {
            informationList = new ArrayList<Information>(1);
        } else {
            for (String mId : deleteInformationDTO.getMIds()) {
                informationList.removeIf(information -> information.getMId().equals(mId));
            }
        }
        Query q = new Query(Criteria.where("userId").is(deleteInformationDTO.getUserId()));
        Update u = new Update().set("informationList", informationList);
        return mongoTemplate.updateFirst(q, u, AsianWalletConstant.INFORMATION_HISTORY).getModifiedCount();
    }
}
