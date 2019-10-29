package com.asianwallets.task.service.yicai.impl;
import com.asianwallets.common.entity.Information;
import com.asianwallets.common.utils.DateToolUtils;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.common.utils.JsoupUtill;
import com.asianwallets.task.service.yicai.YiCaiService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 第一财经
 * @author: YangXu
 * @create: 2019-09-26 14:58
 * https://www.yicai.com/
 **/
@Service
@Slf4j
public class YiCaiServiceImpl implements YiCaiService {

    private static String url = "https://www.yicai.com/";

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
            Document doc = JsoupUtill.getPageDocument(url);
            List<Element> list = new ArrayList<>();
            Elements trs = doc.select("div#headlist > a.f-db");
            list.addAll(trs);
            if(list==null || list.size()==0){
                return;
            }
            //查询最后一条记录
            Information lastInformation = null;
            Criteria criteria = Criteria.where("website").is("第一财经");
            criteria.and("type").is("财经");
            Query query = new Query(criteria);
            Sort sort = new Sort(Sort.Direction.DESC, "releaseTime");
            query.with(sort).limit(1);
            List<Information> lastlist = mongoTemplate.find(query, Information.class);
            if (lastlist.size() > 0) {
                lastInformation = lastlist.get(0);
                log.info("============= 抓取第一财经 ============ ,前次最后一条记录为：【{}】", DateToolUtils.formatDate(lastInformation.getReleaseTime()));
            }
            List<Information> informationList = new ArrayList<>();
            for (Element e : list) {
                Information information = new Information();
                try {
                    String title = e.select("div > div > h2").html();
                    if(title.contains("直播")){
                        continue;
                    }
                    String link = e.attr("abs:href");
                    String describe = e.select("div > div > p").html();
                    Document doc1 = JsoupUtill.getPageDocument(e.attr("abs:href"));
                    String source = doc1.select("div[class=title f-pr]>p>span").html();
                    Date releaseTime = DateToolUtils.getDateByStr(e.select("div > div > div > span").html());
                    if(releaseTime ==null){
                        releaseTime = new Date();
                    }
                    String author = doc1.select("div[class=person f-cb]>ul>a>li>p").html();

                    //判断是否已经抓取
                    if (lastInformation != null) {
                        if (releaseTime.getTime() <= lastInformation.getReleaseTime().getTime()) {
                            break;
                        }
                    }
                    information.setMId(IDS.uuid2());
                    information.setTitle(title);
                    information.setAuthor(author);
                    information.setSource(source);
                    information.setLink(link);
                    information.setDescribe(describe);
                    information.setReleaseTime(releaseTime);
                    information.setWebsite("第一财经");
                    information.setType("财经");
                    information.setCreateTime(new Date());
                }catch (Exception e1){
                    log.info("=========== 抓取第一财经 ========== 解析异常 :【{}】", e1);
                    continue;
                }
                informationList.add(information);
            }
            mongoTemplate.insertAll(informationList);



    }


//    public static void main(String[] args) {
//        //Document doc = JsoupUtill.getPageDocument(url);
//        //List<Element> list = new ArrayList<>();
//        //Elements trs = doc.select("div#headlist > a.f-db");
//        //list.addAll(trs);
//        //for (Element e : list) {
//        //    log.info("title = " + e.select("div > div > h2").html());
//        //    log.info("link = " + e.attr("abs:href"));
//        //    log.info("describe = " + e.select("div > div > p").html());
//        //    Document doc1 = JsoupUtill.getPageDocument(e.attr("abs:href"));
//        //    log.info("source = " + doc1.select("div[class=title f-pr]>p>span").html());
//        //    log.info("releaseTime = " + doc1.select("div[class=title f-pr]>p>em").html());
//        //    log.info("author = " + doc1.select("div[class=person f-cb]>ul>a>li>p").html());
//        //    System.out.println("----------------");
//        //}
//
//    }
}
