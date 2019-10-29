package com.asianwallets.isearch.service.impl;

import com.asianwallets.common.constant.AsianWalletConstant;
import com.asianwallets.common.entity.User;
import com.asianwallets.common.entity.UserWebPool;
import com.asianwallets.common.entity.WebPool;
import com.asianwallets.common.exception.BusinessException;
import com.asianwallets.common.response.EResultEnum;
import com.asianwallets.common.utils.BeanToMapUtil;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.common.utils.MD5Util;
import com.asianwallets.isearch.dao.UserDao;
import com.asianwallets.isearch.dto.AddUrlDTO;
import com.asianwallets.isearch.dto.UserDTO;
import com.asianwallets.isearch.dto.UserWebPoolDTO;
import com.asianwallets.isearch.service.UserService;
import com.asianwallets.isearch.vo.UserWebPoolVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @description:
 * @author: YangXu
 * @create: 2019-09-25 15:00
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 分页查询用户信息
     *
     * @param userDTO
     * @return
     */
    @Override
    public Page<User> select(UserDTO userDTO) {
        if (StringUtils.isBlank(userDTO.getUserId())) {
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        User u = mongoTemplate.findOne(new Query(Criteria.where("userId").is(userDTO.getUserId())), User.class);
        if (u == null) {
            //用户不存在
            throw new BusinessException(EResultEnum.USER_DOES_NOT_EXIST.getCode());
        }
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        Example example = Example.of(user);
        Sort sort = new Sort(userDTO.getOrder(), "createTime");
        Pageable pageable = new PageRequest(userDTO.getPageNum() - 1, userDTO.getPageSize(), sort);
        return userDao.findAll(example, pageable);
    }

    /**
     * 用户注册
     *
     * @param userDTO
     */
    @Override
    public void register(UserDTO userDTO) {
        if (StringUtils.isBlank(userDTO.getMobile()) || StringUtils.isBlank(userDTO.getPassword())) {
            //参数校验
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        if (mongoTemplate.findOne(new Query(Criteria.where("mobile").is(userDTO.getMobile())), User.class) != null) {
            //手机号码已被注册
            throw new BusinessException(EResultEnum.MOBILE_NUMBER_IS_REGISTERED.getCode());
        }
       /* if (mongoTemplate.findOne(new Query(Criteria.where("email").is(userDTO.getEmail())), User.class) != null) {
            //邮箱已被注册
            throw new BusinessException(EResultEnum.MAILBOX_IS_REGISTERED.getCode());
        }
        if (mongoTemplate.findOne(new Query(Criteria.where("userName").is(userDTO.getUserName())), User.class) != null) {
            //用户名已被注册
            throw new BusinessException(EResultEnum.USERNAME_HAS_BEEN_REGISTERED.getCode());
        }*/
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(MD5Util.getMD5String(userDTO.getPassword()));
        user.setCreateTime(new Date());
        user.setEnabled(true);
        user.setUserId(IDS.uniqueID().toString());
        //默认创建一个空的LinkedHashMap
        user.setUserKeyWord(new LinkedHashMap<>());
        //将网站池中的启用的网站开通给此用户
        List<WebPool> availableWebsites = mongoTemplate.find(new Query(Criteria.where("enabled").is(true)), WebPool.class);
        List<String> webNames = new ArrayList<>();
        availableWebsites.forEach(n -> {
            webNames.add(n.getWebName());
        });
        UserWebPool userWebPool = new UserWebPool();
        userWebPool.setId(IDS.uuid2());
        userWebPool.setUserId(user.getUserId());
        userWebPool.setWebNames(webNames);
        userWebPool.setCreateTime(new Date());
        userWebPool.setEnabled(true);
        userWebPool.setCreator("sys");
        mongoTemplate.save(userWebPool, AsianWalletConstant.USER_WEB_POOL);
        mongoTemplate.save(user, AsianWalletConstant.USER);
    }

    /**
     * 登录
     *
     * @param userDTO
     * @return
     */
    @Override
    public User login(UserDTO userDTO) {
        if (StringUtils.isBlank(userDTO.getPassword())) {
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        User user = null;
        if (!StringUtils.isBlank(userDTO.getMobile())) {
            user = mongoTemplate.findOne(new Query(Criteria.where("mobile").is(userDTO.getMobile())), User.class);
        } else if (!StringUtils.isBlank(userDTO.getUserName())) {
            user = mongoTemplate.findOne(new Query(Criteria.where("userName").is(userDTO.getUserName())), User.class);
        } else {
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        if (user == null) {
            //用户不存在
            throw new BusinessException(EResultEnum.USER_DOES_NOT_EXIST.getCode());
        } else if (!MD5Util.getMD5String(userDTO.getPassword()).equals(user.getPassword())) {
            //密码错误
            throw new BusinessException(EResultEnum.WRONG_PASSWORD.getCode());
        } else if (!user.getEnabled()) {
            //用户被禁用
            throw new BusinessException(EResultEnum.USER_NOT_ENABLE.getCode());
        }
        //更新最后登录时间
        user.setLastLoginTime(new Date());
        Update update = new Update();
        update.set("lastLoginTime", new Date());
        Query query = new Query(Criteria.where("userId").is(user.getUserId()));
        mongoTemplate.updateFirst(query, update, AsianWalletConstant.USER);
        user.setPassword(null);
        return user;
    }

    /**
     * 更新用户信息
     *
     * @param userDTO
     * @return
     */
    @Override
    public long updateUser(UserDTO userDTO) {
        if (StringUtils.isBlank(userDTO.getUserId())) {
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        User user = mongoTemplate.findOne(new Query(Criteria.where("userId").is(userDTO.getUserId())), User.class);
        if (user == null) {
            //用户不存在
            throw new BusinessException(EResultEnum.USER_DOES_NOT_EXIST.getCode());
        }
        BeanUtils.copyProperties(userDTO, user);
        user.setUpdateTime(new Date());
        Query query = new Query(Criteria.where("userId").is(user.getUserId()));
        Update update = new Update();
        HashMap<String, Object> map = BeanToMapUtil.beanToMapWithoutNull(user);
        map.forEach(update::set);
        return mongoTemplate.updateFirst(query, update, AsianWalletConstant.USER).getModifiedCount();
    }

    /**
     * 用户新增网站
     *
     * @param userWebPoolDTO
     * @return
     */
    @Override
    public long addUrl(UserWebPoolDTO userWebPoolDTO) {
        if (StringUtils.isBlank(userWebPoolDTO.getUserId())) {
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        UserWebPool userWebPool = mongoTemplate.findOne(new Query(Criteria.where("userId").is(userWebPoolDTO.getUserId())), UserWebPool.class);
        if (userWebPool == null) {
            //用户不存在
            throw new BusinessException(EResultEnum.USER_DOES_NOT_EXIST.getCode());
        }
        //重复网站移除
        List<AddUrlDTO> addUrlDTOs = userWebPoolDTO.getAddUrlDTOs();
        List<String> webNames = userWebPool.getWebNames();
        for (AddUrlDTO addUrlDTO : addUrlDTOs) {
            webNames.add(addUrlDTO.getWebName());
        }
        addUrlDTOs.removeIf(addUrlDTO -> mongoTemplate.findOne(new Query(Criteria.where("webName").is(addUrlDTO.getWebName())), WebPool.class) != null);
        Query query = new Query(Criteria.where("userId").is(userWebPoolDTO.getUserId()));
        Update update = new Update();
        update.set("webNames", webNames);
        //新增到webPool中
        WebPool webPool = new WebPool();
        addUrlDTOs.forEach(n -> {
            webPool.setWebId(IDS.uniqueID().toString());
            webPool.setCreateTime(new Date());
            webPool.setCreator(userWebPoolDTO.getUserId());
            //用户新增网站 默认为 false
            webPool.setEnabled(false);
            webPool.setWebName(n.getWebName());
            webPool.setWebAddress(n.getWebAddress());
            webPool.setRemark(n.getWebName());
            mongoTemplate.save(webPool);
        });
        return mongoTemplate.updateFirst(query, update, AsianWalletConstant.USER_WEB_POOL).getModifiedCount();
    }

    /**
     * 查询用户开通的网站
     *
     * @param userWebPoolDTO
     * @return
     */
    @Override
    public UserWebPoolVO selectUserUrl(UserWebPoolDTO userWebPoolDTO) {
        if (StringUtils.isBlank(userWebPoolDTO.getUserId())) {
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        Query query = new Query(Criteria.where("userId").is(userWebPoolDTO.getUserId()));
        UserWebPool userWebPool = mongoTemplate.findOne(query, UserWebPool.class);
        List<String> webNames = userWebPool.getWebNames();
        ArrayList<AddUrlDTO> addUrlDTOS = new ArrayList<>();
        List<WebPool> webPools = mongoTemplate.find(new Query(), WebPool.class);
        webPools.forEach(n -> {
            if (webNames.contains(n.getWebName())) {
                AddUrlDTO addUrlDTO = new AddUrlDTO();
                addUrlDTO.setWebAddress(n.getWebAddress());
                addUrlDTO.setWebName(n.getWebName());
                addUrlDTOS.add(addUrlDTO);
            }
        });
        UserWebPoolVO userWebPoolVO = new UserWebPoolVO();
        BeanUtils.copyProperties(userWebPool, userWebPoolVO);
        userWebPoolVO.setAddUrlDTOs(addUrlDTOS);
        return userWebPoolVO;
    }

    /**
     * 修改用户开通的网站
     *
     * @param userWebPoolDTO
     * @return
     */
    @Override
    public long updateUserUrl(UserWebPoolDTO userWebPoolDTO) {
        if (StringUtils.isBlank(userWebPoolDTO.getUserId())) {
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        UserWebPool userWebPool = mongoTemplate.findOne(new Query(Criteria.where("userId").is(userWebPoolDTO.getUserId())), UserWebPool.class);
        if (userWebPool == null) {
            //用户不存在
            throw new BusinessException(EResultEnum.USER_DOES_NOT_EXIST.getCode());
        }
        Query query = new Query(Criteria.where("userId").is(userWebPoolDTO.getUserId()));
        Update update = new Update();
        ArrayList<String> webNames = new ArrayList<>();
        for (AddUrlDTO addUrlDTO : userWebPoolDTO.getAddUrlDTOs()) {
            webNames.add(addUrlDTO.getWebName());
        }

        update.set("webNames", webNames);
        return mongoTemplate.updateFirst(query, update, AsianWalletConstant.USER_WEB_POOL).getModifiedCount();
    }


    /**
     * 删除用户开通的网站
     *
     * @param userWebPoolDTO
     * @return
     */
    @Override
    public long deleteUserUrl(UserWebPoolDTO userWebPoolDTO) {
        if (StringUtils.isBlank(userWebPoolDTO.getUserId())) {
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        UserWebPool userWebPool = mongoTemplate.findOne(new Query(Criteria.where("userId").is(userWebPoolDTO.getUserId())), UserWebPool.class);
        if (userWebPool == null) {
            //用户不存在
            throw new BusinessException(EResultEnum.USER_DOES_NOT_EXIST.getCode());
        }
        Query query = new Query(Criteria.where("userId").is(userWebPoolDTO.getUserId()));
        Update update = new Update();
        List<String> webNames = userWebPool.getWebNames();
        for (AddUrlDTO addUrlDTO : userWebPoolDTO.getAddUrlDTOs()) {
            webNames.remove(addUrlDTO.getWebName());
        }
        update.set("webNames", webNames);
        return mongoTemplate.updateFirst(query, update, AsianWalletConstant.USER_WEB_POOL).getModifiedCount();
    }

    /**
     * 修改密码
     *
     * @param userDTO
     * @return
     */
    @Override
    public long updatepwd(UserDTO userDTO) {
        //根据手机号修改用户密码
        if (StringUtils.isBlank(userDTO.getMobile()) || StringUtils.isBlank(userDTO.getPassword())) {
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        User user = mongoTemplate.findOne(new Query(Criteria.where("mobile").is(userDTO.getMobile())), User.class);
        if (user == null) {
            //用户不存在
            throw new BusinessException(EResultEnum.USER_DOES_NOT_EXIST.getCode());
        }
        user.setPassword(MD5Util.getMD5String(userDTO.getPassword()));
        user.setUpdateTime(new Date());
        Query query = new Query(Criteria.where("mobile").is(userDTO.getMobile()));
        Update update = new Update();
        HashMap<String, Object> map = BeanToMapUtil.beanToMapWithoutNull(user);
        map.forEach(update::set);
        return mongoTemplate.updateFirst(query, update, AsianWalletConstant.USER).getModifiedCount();
    }

}
