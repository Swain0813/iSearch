package com.asianwallets.isearch.service.impl;
import com.asianwallets.common.constant.AsianWalletConstant;
import com.asianwallets.common.entity.User;
import com.asianwallets.common.entity.UserKeyWord;
import com.asianwallets.common.exception.BusinessException;
import com.asianwallets.common.response.EResultEnum;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.isearch.dto.UserKeyWordDTO;
import com.asianwallets.isearch.service.UserKeyWordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * 输入关键字的实现类
 */
@Service
public class UserKeyWordServiceImpl implements UserKeyWordService {

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 新增输入关键字
     *
     * @param userId
     * @param keyWords
     */
    @Override
    public void addUserKeyWord(String userId, String keyWords) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(keyWords)) {
            //输入参数不能为空
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        User user = mongoTemplate.findOne(new Query(Criteria.where("userId").is(userId)), User.class);
        if (user == null) {
            //用户不存在
            throw new BusinessException(EResultEnum.USER_DOES_NOT_EXIST.getCode());
        }
        UserKeyWord keyWord = mongoTemplate.findOne(new Query(Criteria.where("keyWord").is(keyWords)), UserKeyWord.class);
        if (keyWord != null) {
            //更新次数
            LinkedHashMap<String, Integer> userKeyWord = user.getUserKeyWord();
            //存在加一
            //更新user
            Integer nums = userKeyWord.get(keyWords);
            if (nums == null) {
                nums = 0;
            }
            userKeyWord.put(keyWords, nums + 1);
            Query query1 = new Query(Criteria.where("userId").is(userId));
            Update update1 = new Update().set("userKeyWord", userKeyWord);
            mongoTemplate.updateFirst(query1, update1, AsianWalletConstant.USER);
            //更新UserKeyWord
            Query query = new Query(Criteria.where("keyWord").is(keyWords));
            Update update = new Update().set("searchNumber", keyWord.getSearchNumber() + 1);
            mongoTemplate.updateFirst(query, update, AsianWalletConstant.USER_KEY_WORD);
        } else {
            //新增到关键字记录表
            UserKeyWord ukw = new UserKeyWord();
            ukw.setId(IDS.uuid2());
            ukw.setKeyWord(keyWords);
            ukw.setSearchNumber(1);
            ukw.setCreateTime(new Date());
            mongoTemplate.save(ukw, AsianWalletConstant.USER_KEY_WORD);
            LinkedHashMap<String, Integer> userKeyWord = new LinkedHashMap<>();
            if (user.getUserKeyWord() != null) {
                userKeyWord.putAll(user.getUserKeyWord());
            }
            userKeyWord.put(keyWords, 1);
            Query query = new Query(Criteria.where("userId").is(userId));
            Update update = new Update();
            update.set("userKeyWord", userKeyWord);
            mongoTemplate.updateFirst(query, update, AsianWalletConstant.USER);
        }
    }

    /**
     * 查询用户的关键字
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> selectUserKeyWord(String userId) {
        User user = mongoTemplate.findOne(new Query(Criteria.where("userId").is(userId)), User.class);
        if (user == null) {
            //用户不存在
            throw new BusinessException(EResultEnum.USER_DOES_NOT_EXIST.getCode());
        }
        LinkedHashMap<String, Integer> userKeyWord = user.getUserKeyWord();
        Set<String> set = userKeyWord.keySet();
        ArrayList<String> list = new ArrayList<>(set);
        Collections.reverse(list);
        return list;
    }

    /**
     * 删除用户的关键字
     *
     * @param userKeyWordDTO
     * @return
     */
    @Override
    public long deleteUserKeyWord(UserKeyWordDTO userKeyWordDTO) {
        if (StringUtils.isBlank(userKeyWordDTO.getUserId())) {
            //输入参数不能为空
            throw new BusinessException(EResultEnum.PARAMETER_IS_NOT_PRESENT.getCode());
        }
        User user = mongoTemplate.findOne(new Query(Criteria.where("userId").is(userKeyWordDTO.getUserId())), User.class);
        if (user == null) {
            //用户不存在
            throw new BusinessException(EResultEnum.USER_DOES_NOT_EXIST.getCode());
        }
        LinkedHashMap<String, Integer> userKeyWordMap = user.getUserKeyWord();
        userKeyWordMap.remove(userKeyWordDTO.getKeyWord());
        Query query = new Query(Criteria.where("userId").is(userKeyWordDTO.getUserId()));
        Update update = new Update().set("userKeyWord", userKeyWordMap);
        return mongoTemplate.updateFirst(query, update, AsianWalletConstant.USER).getModifiedCount();
    }

    /**
     * 删除用户的所有搜索关键字
     *
     * @param userId
     * @return
     */
    @Override
    public long deleteUserAllKeyWord(String userId) {
        User user = mongoTemplate.findOne(new Query(Criteria.where("userId").is(userId)), User.class);
        if (user == null) {
            //用户不存在
            throw new BusinessException(EResultEnum.USER_DOES_NOT_EXIST.getCode());
        }
        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = new Update().set("userKeyWord", null);
        return mongoTemplate.updateFirst(query, update, AsianWalletConstant.USER).getModifiedCount();
    }

    /**
     * 查询热词
     *
     * @return
     */
    @Override
    public List<String> queryHotWords() {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "searchNumber")).limit(10);
        List<UserKeyWord> userKeyWords = mongoTemplate.find(query, UserKeyWord.class);
        ArrayList<String> hotWords = new ArrayList<>();
        userKeyWords.forEach(n -> {
            hotWords.add(n.getKeyWord());
        });
        return hotWords;
    }
}
