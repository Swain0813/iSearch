package com.asianwallets.task.service.wxbgw.impl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asianwallets.common.entity.Config;
import com.asianwallets.common.entity.Information;
import com.asianwallets.common.utils.DateToolUtils;
import com.asianwallets.common.utils.HttpClientUtils;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.task.service.wxbgw.WxBgwService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * @description: 微信并购汪
 * @author: YangXu
 * @create: 2019-09-29 10:32
 * "https://mp.weixin.qq.com/cgi-bin/appmsg?token=" + config.getToken() + "&lang=zh_CN&f=json&ajax=1&random=0.2515180108182724&action=list_ex&begin=0&count=5&query=&fakeid=Mzg2MDI0OTIxNA%3D%3D&type=9"
 **/
@Service
@Slf4j
public class WxBgwServiceImpl implements WxBgwService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @description 微信并购汪
     */
    @Override
    public void grab() {
        log.info("==========【微信并购汪】==========【开始爬取数据】");
        Query configQuery = new Query(Criteria.where("remark").is("1"));
        List<Config> configs = mongoTemplate.find(configQuery, Config.class);
        if (configs == null || configs.size() == 0) {
            log.info("==========【微信并购汪】==========【配置出错】");
            return;
        }
        Config config = configs.get(0);
        String url = "https://mp.weixin.qq.com/cgi-bin/appmsg?token=" + config.getToken() + "&lang=zh_CN&f=json&ajax=1&random=0.2515180108182724&action=list_ex&begin=0&count=5&query=&fakeid=Mzg2MDI0OTIxNA%3D%3D&type=9";
        //请求头Map
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        headerMap.put("Cookie", config.getCookie());
        JSONObject result = HttpClientUtils.reqGet(url, null, headerMap);
        if (result == null || result.getJSONArray("app_msg_list") == null) {
            log.info("==========【微信并购汪】==========【数据为空】");
            return;
        }
        //查询最后一条记录
        Information lastInformation = null;
        Criteria criteria = Criteria.where("website").is("并购汪");
        criteria.and("type").is("并购汪");
        Query query = new Query(criteria);
        Sort sort = new Sort(Sort.Direction.DESC, "releaseTime");
        query.with(sort).limit(1);
        List<Information> lastList = mongoTemplate.find(query, Information.class);
        if (lastList.size() > 0) {
            lastInformation = lastList.get(0);
            log.info("==========【微信并购汪】==========【前次最后一条记录为】: 【{}】", DateToolUtils.formatDate(lastInformation.getReleaseTime()));
        }
        List<Information> informationList = new ArrayList<>();
        JSONArray resultList = result.getJSONArray("app_msg_list");
        for (int i = 0; i < resultList.size(); i++) {
            Information information = new Information();
            try {
                JSONObject row = resultList.getJSONObject(i);
                //时间
                Integer time = row.getInteger("create_time");
                Date releaseTime = new Date(time * 1000L);
                //判断是否已经抓取
                if (lastInformation != null) {
                    if (releaseTime.getTime() <= lastInformation.getReleaseTime().getTime()) {
                        log.info("==========【微信并购汪】==========【当前记录已抓取】");
                        break;
                    }
                }
                //图片
                String img = row.getString("cover");
                //描述
                String describe = row.getString("digest");
                //标题
                String title = row.getString("title");
                //链接
                String link = row.getString("link");
                information.setMId(IDS.uuid2());
                information.setTitle(title);
                information.setLink(link);
                information.setImg(img);
                information.setDescribe(describe);
                information.setSource("并购汪");
                information.setType("并购汪");
                information.setWebsite("并购汪");
                information.setReleaseTime(releaseTime);
                information.setCreateTime(new Date());
            } catch (Exception e) {
                log.info("==========【微信并购汪】==========【解析异常】", e);
                continue;
            }
            informationList.add(information);
        }
        mongoTemplate.insertAll(informationList);
        log.info("==========【微信并购汪】==========【结束爬取数据】");
    }
}
