package com.asianwallets.task.service.xueqiu.impl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asianwallets.common.entity.Config;
import com.asianwallets.common.entity.Information;
import com.asianwallets.common.utils.DateToolUtils;
import com.asianwallets.common.utils.HttpClientUtils;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.task.service.xueqiu.XueqiuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * @description: 雪球
 * @author: YangXu
 * @create: 2019-09-26 17:47
 * https://xueqiu.com/
 **/
@Service
@Slf4j
public class XueqiuServiceImpl implements XueqiuService {
    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * @return
     * @Author YangXu
     * @Date 2019/9/26
     * @Descripate 抓取信息
     **/
    @Override
    public void grab() {
        Information lastInformation = null;
        Criteria criteria = Criteria.where("website").is("雪球");
        criteria.and("type").is("财经");
        Query query = new Query(criteria);
        Sort sort = new Sort(Sort.Direction.DESC, "releaseTime");
        query.with(sort).limit(1);
        List<Information> lastlist = mongoTemplate.find(query, Information.class);
        if (lastlist.size() > 0) {
            lastInformation = lastlist.get(0);
            log.info("============= 抓取雪球 ============ ,前次最后一条记录为：【{}】", DateToolUtils.formatDate(lastInformation.getReleaseTime()));
        }

        Query configQuery = new Query(Criteria.where("remark").is("2"));
        List<Config> configs = mongoTemplate.find(configQuery, Config.class);
        if (configs == null || configs.size() == 0) {
            log.info("==========【抓取雪球】==========【配置出错】");
            return;
        }
        Config config = configs.get(0);

        List<Information> informationList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("Cookie", config.getCookie());
        JSONObject jsonObject = HttpClientUtils.reqGet("https://xueqiu.com/v4/statuses/public_timeline_by_category.json?since_id=-1&max_id=-1&count=15&category=-1", null, map);
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("list"));
        for (int i = 0; i < jsonArray.size(); i++) {
            Information information = new Information();
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            JSONObject jsonObject2 = JSONObject.parseObject(jsonObject1.get("data").toString());
            try {
                String title = jsonObject2.get("title").toString();
                String link = "https://xueqiu.com" + jsonObject2.get("target").toString();
                String describe = jsonObject2.get("description").toString();
                String source = jsonObject2.get("source").toString();
                Date releaseTime = new Date(new Long(jsonObject2.get("created_at").toString()));
                if (releaseTime == null) {
                    releaseTime = new Date();
                }

                //判断是否已经抓取
                if (lastInformation != null) {
                    if (releaseTime.getTime() <= lastInformation.getReleaseTime().getTime()) {
                        continue;
                    }
                }

                information.setMId(IDS.uuid2());
                information.setTitle(title);
                information.setSource(source);
                information.setAuthor(JSONObject.parseObject((jsonObject2.get("user").toString())).get("screen_name").toString());
                information.setLink(link);
                information.setDescribe(describe);
                information.setReleaseTime(releaseTime);
                information.setWebsite("雪球");
                information.setType("财经");
                information.setCreateTime(new Date());
            } catch (Exception e1) {
                log.info("=========== 抓取雪球 ========== 解析异常 :【{}】", e1);
                continue;
            }
            informationList.add(information);
        }
        mongoTemplate.insertAll(informationList);

    }


    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("Cookie", "device_id=24700f9f1986800ab4fcc880530dd0ed; s=df11v4yqjr; webp=0; u=801571118423817; xq_a_token=87993a504d5d350e6271c337ad8e9ec8809acb79; xq_r_token=2b9912fb63f07c0f11e94985018ad64e78cca498; Hm_lvt_1db88642e346389874251b5a1eded6e3=1569491066,1569723180,1571118424,1571625449; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1571712903");
        JSONObject jsonObject = HttpClientUtils.reqGet("https://xueqiu.com/v4/statuses/public_timeline_by_category.json?since_id=-1&max_id=-1&count=15&category=-1", null, map);
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("list"));
        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
        JSONObject jsonObject2 = JSONObject.parseObject(jsonObject1.get("data").toString());

        System.out.println(jsonObject2.get("title"));
        System.out.println(jsonObject2.get("description"));
        System.out.println(jsonObject2.get("target"));
        System.out.println(JSONObject.parseObject((jsonObject2.get("user").toString())).get("screen_name"));
        System.out.println(jsonObject2.get("source"));
        System.out.println(jsonObject2.get("created_at"));

    }

}
