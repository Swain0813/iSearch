package com.asianwallets.task.service.sina.impl;
import com.asianwallets.common.entity.Information;
import com.asianwallets.common.utils.DateToolUtils;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.common.utils.JsoupUtill;
import com.asianwallets.task.service.sina.SinaService;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.*;

/**
 * @description: 新浪财经
 * @author: YangXu
 * @create: 2019-09-27 11:36
 * http://finance.sina.com.cn/china/ 宏观
 * http://finance.sina.com.cn/chanjing/ 财经
 **/
@Service
@Slf4j
public class SinaServiceImpl implements SinaService {

    private static String china = "http://finance.sina.com.cn/china/";//宏观
    private static String chanjing = "http://finance.sina.com.cn/chanjing/";//财经

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void grab() {
        Map<String, String> map = new HashMap<>();
        map.put("财经", chanjing);
        map.put("宏观", china);
        Set<String> set = map.keySet();
        for (String key : set) {
            String url = map.get(key);
            grabImpl(url, key);
        }

    }

    public void grabImpl(String url, String type) {
        WebClient wc = new WebClient();
        wc.getOptions().setJavaScriptEnabled(true); //启用JS解释器，默认为true
        wc.getOptions().setCssEnabled(false); //禁用css支持
        wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
        wc.getOptions().setTimeout(10000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待
        HtmlPage page = null;
        try {
            page = wc.getPage(url);
        } catch (IOException e) {
            log.info("====新浪财经获取时==========发生异常:",e);
        }finally {
            wc.close();
        }
        String pageXml = page.asXml(); //以xml的形式获取响应文本

        /**jsoup解析文档*/
        Document doc = Jsoup.parse(pageXml, "http://finance.sina.com.cn");
        List<Element> list = new ArrayList<>();
        Elements trs = doc.select("div[class=feed-card-content]>div>div");
        list.addAll(trs);

        //查询最后一条记录
        Information lastInformation = null;
        Criteria criteria = Criteria.where("website").is("新浪财经");
        criteria.and("type").is(type);
        Query query = new Query(criteria);
        Sort sort = new Sort(Sort.Direction.DESC, "releaseTime");
        query.with(sort).limit(1);
        List<Information> lastlist = mongoTemplate.find(query, Information.class);
        if (lastlist.size() > 0) {
            lastInformation = lastlist.get(0);
            log.info("============= 抓取新浪财经 ============ ,前次最后一条记录为：【{}】", DateToolUtils.formatDate(lastInformation.getReleaseTime()));
        }
        List<Information> informationList = new ArrayList<>();
        for (Element e : list) {
            Information information = new Information();
            try {
                String title = e.select("h2>a").html();
                String link = e.select("h2>a").attr("abs:href");
                String describe = e.select("div[class=feed-card-txt]>a").html();
                Document doc1 = JsoupUtill.getPageDocument(e.select("h2>a").attr("abs:href"));
                String source = doc1.select("div[class=date-source]>a").html();
                Date releaseTime = DateToolUtils.getDateByStr(doc1.select("span[class=date]").html().replaceAll("(?:年|月)", "-").replace("日","")+":00");
                if(releaseTime ==null){
                    releaseTime = new Date();
                }

                //判断是否已经抓取
                if (lastInformation != null) {
                    if (releaseTime.getTime() <= lastInformation.getReleaseTime().getTime()) {
                        break;
                    }
                }
                information.setMId(IDS.uuid2());
                information.setTitle(title);
                information.setSource(source);
                information.setLink(link);
                information.setDescribe(describe);
                information.setReleaseTime(releaseTime);
                information.setWebsite("新浪财经");
                information.setType(type);
                information.setCreateTime(new Date());
            }catch (Exception e1){
                log.info("=========== 抓取新浪财经 ========== 解析异常 :【{}】", e1);
                continue;
            }
            informationList.add(information);
        }
        mongoTemplate.insertAll(informationList);


    }

//    public static void main(String[] args) {
//        //WebClient wc = new WebClient();
//        //wc.getOptions().setJavaScriptEnabled(true); //启用JS解释器，默认为true
//        //wc.getOptions().setCssEnabled(false); //禁用css支持
//        //wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
//        //wc.getOptions().setTimeout(10000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待
//        //HtmlPage page = null;
//        //try {
//        //    page = wc.getPage("http://finance.sina.com.cn/china/");
//        //} catch (IOException e) {
//        //    log.info("====");
//        //}
//        //String pageXml = page.asXml(); //以xml的形式获取响应文本
//        //
//        ///**jsoup解析文档*/
//        //Document doc = Jsoup.parse(pageXml, "http://finance.sina.com.cn");
//        //
//        //List<Element> list = new ArrayList<>();
//        //Elements trs = doc.select("div[class=feed-card-content]>div>div");
//        //list.addAll(trs);
//        //for (Element e : list) {
//        //    log.info("title = " + e.select("h2>a").html());
//        //    log.info("link = " + e.select("h2>a").attr("abs:href"));
//        //    log.info("describe = " + e.select("div[class=feed-card-txt]>a").html());
//        //    Document doc1 = JsoupUtill.getPageDocument(e.select("h2>a").attr("abs:href"));
//        //    log.info("source = " + doc1.select("div[class=date-source]>a").html());
//        //    log.info("releaseTime = " + doc1.select("span[class=date]").html().replaceAll("(?:年|月)", "-").replace("日","")+":00");
//        //    //log.info("author = " + doc1.select("div[class=person f-cb]>ul>a>li>p").html());
//        //    System.out.println("----------------");
//        //}
//    }
}
