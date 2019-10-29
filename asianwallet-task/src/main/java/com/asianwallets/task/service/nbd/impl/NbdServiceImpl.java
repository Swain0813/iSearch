package com.asianwallets.task.service.nbd.impl;
import com.asianwallets.common.entity.Information;
import com.asianwallets.common.utils.DateToolUtils;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.common.utils.JsoupUtill;
import com.asianwallets.task.service.nbd.NbdService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * @description: 每经网
 * @author: YangXu
 * @create: 2019-09-26 10:32
 * http://industry.nbd.com.cn/ 公司
 * http://finance.nbd.com.cn/ 金融
 * http://economy.nbd.com.cn/ 宏观
 **/
@Service
@Slf4j
public class NbdServiceImpl implements NbdService {

    private static String industry = "http://industry.nbd.com.cn/";//公司
    private static String finance = "http://finance.nbd.com.cn/";//金融
    private static String economy = "http://economy.nbd.com.cn/";//宏观

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @return
     * @Author YangXu
     * @Date 2019/9/26
     * @Descripate 抓取每经网信息
     **/
    @Override
    public void grab() {
        Map<String, String> map = new HashMap<>();
        map.put("公司", industry);
        map.put("金融", finance);
        map.put("宏观", economy);
        Set<String> set = map.keySet();
        for (String key : set) {
            String url = map.get(key);
            grabImpl(url, key);
        }
    }

    public void grabImpl(String url, String type) {
        Document doc = JsoupUtill.getPageDocument(url);
        if(doc==null){
            return;
        }
        List<Element> list = new ArrayList<>();
        Elements trs = doc.select("ul.m-columnnews-list > li");
        if(trs==null){
            return;
        }
        list.addAll(trs);
        if(list==null || list.size()==0){
            return;
        }

        //查询最后一条记录
        Information lastInformation = null;
        Criteria criteria = Criteria.where("website").is("每经网");
        criteria.and("type").is(type);
        Query query = new Query(criteria);
        Sort sort = new Sort(Sort.Direction.DESC, "releaseTime");
        query.with(sort).limit(1);
        List<Information> lastlist = mongoTemplate.find(query, Information.class);
        if (lastlist.size() > 0) {
            lastInformation = lastlist.get(0);
            log.info("============= 抓取每经网公司模块 ============ ,前次最后一条记录为：【{}】", DateToolUtils.formatDate(lastInformation.getReleaseTime()));
        }
        List<Information> informationList = new ArrayList<>();
        for (Element e : list) {
            Information information = new Information();
            try {
                information.setMId(IDS.uuid2());
                //标题
                String title = e.select("a.f-title").html();
                information.setTitle(title);

                List<Element> list1 = new ArrayList<>();
                Elements trs1 = e.select("p.f-source > span");
                list1.addAll(trs1);
                //来源
                String source = list1.get(0).html();
                information.setSource(source);
                //新闻连接
                String link = e.select("a.f-title").attr("abs:href");
                information.setLink(link);
                //发布时间
                Date date = DateToolUtils.getDateByStr(list1.get(1).html());
                if (date == null) {
                    date = new Date();
                }
                //判断是否已经抓取
                if (lastInformation != null) {
                    if (date.getTime() <= lastInformation.getReleaseTime().getTime()) {
                        break;
                    }
                }
                information.setReleaseTime(date);

                //描述
                String describe = e.select("a.f-text").html();
                information.setDescribe(describe);

                //图片
                String img = e.select("a.u-columnnews-img > img").attr("data-aload");
                information.setImg(img);

                information.setWebsite("每经网");
                information.setType(type);
                information.setCreateTime(new Date());
            } catch (Exception e1) {
                log.info("=========== 抓取每经网公司模块 ========== 解析异常 :【{}】", e1);
                continue;
            }
            informationList.add(information);
        }
        mongoTemplate.insertAll(informationList);


    }


//    public static void main(String[] args) {
//        //Document doc = JsoupUtill.getPageDocument(url);
//        //List<Element> list = new ArrayList<>();
//        //Elements trs =   doc.select("ul.m-columnnews-list > li");
//        //list.addAll(trs);
//        //for (Element e:list) {
//        //
//        //    System.out.println(e.select("a.u-columnnews-img > img").attr("data-aload"));
//        //}
//    }
}
