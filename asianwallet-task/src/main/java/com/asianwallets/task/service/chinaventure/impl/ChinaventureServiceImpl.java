package com.asianwallets.task.service.chinaventure.impl;
import com.asianwallets.common.entity.Information;
import com.asianwallets.common.utils.DateToolUtils;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.common.utils.JsoupUtill;
import com.asianwallets.task.service.chinaventure.ChinaventureService;
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
 * @description: 投中网
 * @author: YangXu
 * @create: 2019-09-27 10:30
 * https://www.chinaventure.com.cn/
 **/
@Service
@Slf4j
public class ChinaventureServiceImpl implements ChinaventureService {


    private static String url = "https://www.chinaventure.com.cn/";

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
        Elements trs = doc.select("ul[id=index_newstab_article01]>li");
        list.addAll(trs);

        //查询最后一条记录
        Information lastInformation = null;
        Criteria criteria = Criteria.where("website").is("投中网");
        criteria.and("type").is("财经");
        Query query = new Query(criteria);
        Sort sort = new Sort(Sort.Direction.DESC, "releaseTime");
        query.with(sort).limit(1);
        List<Information> lastlist = mongoTemplate.find(query, Information.class);
        if (lastlist.size() > 0) {
            lastInformation = lastlist.get(0);
            log.info("============= 抓取投中网 ============ ,前次最后一条记录为：【{}】", DateToolUtils.formatDate(lastInformation.getReleaseTime()));
        }
        List<Information> informationList = new ArrayList<>();

        for (Element e : list) {
            Information information = new Information();
            try {

                String title = e.select("a>h1").html();
                String link = e.select("a").attr("abs:href");
                String describe = e.select("a>h2").html();
                String source = e.select("a>p>span").get(0).html();
                String img = e.select("div[class=coverimg]>img").attr("src");

                Document doc1 = JsoupUtill.getPageDocument(e.select("a").attr("abs:href"));
                Date releaseTime = DateToolUtils.getDateByStr(doc1.select("div[class=releaseTime]>span").html());
                if (releaseTime == null) {
                    releaseTime = new Date();
                }
                String author = doc1.select("div[class=source_author]>span").get(2).html();

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
                information.setImg(img);
                information.setDescribe(describe);
                information.setReleaseTime(new Date());
                information.setWebsite("投中网");
                information.setType("财经");
                information.setCreateTime(new Date());

            } catch (Exception e1) {
                log.info("=========== 抓取投中网 ========== 解析异常 :【{}】", e1);
                continue;
            }
            informationList.add(information);
        }
        mongoTemplate.insertAll(informationList);

    }

//    public static void main(String[] args) {
//        Document doc = JsoupUtill.getPageDocument(url);
//        List<Element> list = new ArrayList<>();
//        Elements trs = doc.select("ul[id=index_newstab_article01]>li");
//        list.addAll(trs);
//        for (Element e : list) {
//            log.info("title = " + e.select("a>h1").html());
//            log.info("link = " + e.select("a").attr("abs:href"));
//            log.info("describe = " + e.select("a>h2").html());
//            log.info("source = " + e.select("a>p>span").get(0).html());
//            Document doc1 = JsoupUtill.getPageDocument(e.select("a").attr("abs:href"));
//            log.info("releaseTime = " + doc1.select("div[class=releaseTime]>span").html());
//            log.info("author = " + doc1.select("div[class=source_author]>span").get(2).html());
//            System.out.println("----------------");
//        }
//    }
}
