package com.asianwallets.task.service.cs.impl;
import com.asianwallets.common.entity.Information;
import com.asianwallets.common.utils.DateToolUtils;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.common.utils.JsoupUtill;
import com.asianwallets.task.service.cs.CsService;
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
 * @description: 中证网
 * @author: YangXu
 * @create: 2019-09-26 16:42
 *  http://www.cs.com.cn/ssgs/gsxw/  公司新闻
 *  http://www.cs.com.cn/ssgs/gssd/  公司深度
 *  http://www.cs.com.cn/cj/hyzx/  行业资讯
 *  http://www.cs.com.cn/xwzx/hg/  财经要闻
 *  http://www.cs.com.cn/ssgs/ssb/   新三板
 *  http://www.cs.com.cn/jg/08/  互联网金融
 **/
@Service
@Slf4j
public class CsServiceImpl implements CsService {

    private static String gsxw = "http://www.cs.com.cn/ssgs/gsxw/";// 公司新闻
    private static String gssd = "http://www.cs.com.cn/ssgs/gssd/";// 公司深度
    private static String hyzx = "http://www.cs.com.cn/cj/hyzx/";// 行业资讯
    private static String hg = "http://www.cs.com.cn/xwzx/hg/";// 财经要闻
    private static String ssb = "http://www.cs.com.cn/ssgs/ssb/";// 新三板
    private static String hlw = "http://www.cs.com.cn/jg/08/";// 互联网金融

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

        Map<String, String> map = new HashMap<>();
        map.put("公司新闻", gsxw);
        map.put("公司深度", gssd);
        map.put("行业资讯", hyzx);
        map.put("财经要闻", hg);
        map.put("新三板", ssb);
        map.put("互联网金融", hlw);
        Set<String> set = map.keySet();
        for (String key : set) {
            String url = map.get(key);
            grabImpl(url, key);
        }
    }

    public void grabImpl(String url, String type) {

        log.info("============= 抓取中证网 ============ type :【{}】，url:【{}】",type,url);
        Document doc = JsoupUtill.getPageDocument(url);
        List<Element> list = new ArrayList<>();
        Elements trs = doc.select("ul[class=list-lm pad10]>li");
        list.addAll(trs);
        if(list==null || list.size()==0){
            return;
        }
        //查询最后一条记录
        Information lastInformation = null;
        Criteria criteria = Criteria.where("website").is("中证网");
        criteria.and("type").is(type);
        Query query = new Query(criteria);
        Sort sort = new Sort(Sort.Direction.DESC, "releaseTime");
        query.with(sort).limit(1);
        List<Information> lastlist = mongoTemplate.find(query, Information.class);
        if (lastlist.size() > 0) {
            lastInformation = lastlist.get(0);
            log.info("============= 抓取中证网 ============ ,前次最后一条记录为：【{}】", DateToolUtils.formatDate(lastInformation.getReleaseTime()));
        }
        List<Information> informationList = new ArrayList<>();


        for (Element e : list) {

            Information information = new Information();
            try {
                String title =e.select("a").html();
                String link = e.select("a").attr("abs:href");
                Document doc1 = JsoupUtill.getPageDocument(e.select("a").attr("abs:href"));
                String source = doc1.select("div[class=info]>p").get(0).html();
                Date releaseTime = DateToolUtils.getDateByStr(doc1.select("div[class=info]>p>em").get(0).html());
                if(releaseTime ==null){
                    releaseTime = new Date();
                }
                String author = doc1.select("div[class=info]>p>em").get(1).html();

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
                information.setReleaseTime(releaseTime);
                information.setWebsite("中证网");
                information.setType(type);
                information.setCreateTime(new Date());
            }catch (Exception e1){
                log.info("=========== 抓取中证网 ==========type:【{}】 解析异常 :【{}】",type, e);
                continue;
            }
            informationList.add(information);
        }

        mongoTemplate.insertAll(informationList);
    }

//    public static void main(String[] args) {
//        Document doc = JsoupUtill.getPageDocument("http://www.cs.com.cn/xwzx/hg/");
//        List<Element> list = new ArrayList<>();
//        Elements trs = doc.select("ul[class=list-lm pad10]>li");
//        list.addAll(trs);
//        for (Element e : list) {
//            log.info("title = " + e.select("a").html());
//            log.info("link = " + e.select("a").attr("abs:href"));
//            log.info("releaseTime + " + e.select("span").html());
//            Document doc1 = JsoupUtill.getPageDocument(e.select("a").attr("abs:href"));
//            log.info("source = " + doc1.select("div[class=info]>p").get(0).html());
//            log.info("author = " + doc1.select("div[class=info]>p>em").get(1).html());
//
//        }
//    }

}
