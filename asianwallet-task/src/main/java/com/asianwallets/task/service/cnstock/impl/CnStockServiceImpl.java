package com.asianwallets.task.service.cnstock.impl;
import com.asianwallets.common.entity.Information;
import com.asianwallets.common.utils.DateToolUtils;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.common.utils.JsoupUtill;
import com.asianwallets.task.service.cnstock.CnStockService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 上海证券报业务接口实现类
 * @author: XuWenQi
 * @create: 2019-09-26 17:20
 * http://www.cnstock.com/
 **/
@Service
@Slf4j
public class CnStockServiceImpl implements CnStockService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @description 爬取上海证券报信息
     */
    @Override
    public void grabCnStock() {
        log.info("==========【上海证券报】==========【开始爬取数据】");
        String website = "http://www.cnstock.com/";
        Document outDoc = JsoupUtill.getPageDocument(website);
        List<Information> informationList = new ArrayList<>();
        List<Element> elements = outDoc.select("li[id]");
        if (elements == null || elements.size() == 0) {
            return;
        }
        //查询最后一条记录
        Information lastInformation = null;
        Criteria criteria = Criteria.where("website").is("上海证券报");
        criteria.and("type").is("证券");
        Query query = new Query(criteria);
        Sort sort = new Sort(Sort.Direction.DESC, "releaseTime");
        query.with(sort).limit(1);
        List<Information> lastList = mongoTemplate.find(query, Information.class);
        if (lastList.size() > 0) {
            lastInformation = lastList.get(0);
            log.info("==========【上海证券报】==========【前次最后一条记录为】: 【{}】", DateToolUtils.formatDate(lastInformation.getReleaseTime()));
        }
        for (Element element : elements) {
            Information information = new Information();
            try {
                //链接
                String link = element.select("a").attr("href");
                //调用内层Link
                Document innerDoc = JsoupUtill.getPageDocument(link);
                //发布时间
                String time = innerDoc.select("span[class = timer]").html();
                Date releaseTime = null;
                //时间格式的特殊处理
                if (!StringUtils.isEmpty(time)) {
//                    String outTime = element.select("span[class=time]").html();
//                    time = DateToolUtils.getReqDate() + " " + outTime + ":00";
                    releaseTime = DateToolUtils.getDateByE(time);
                }
                if (releaseTime == null) {
                    releaseTime = new Date();
                }
                //判断是否已经抓取
                if (lastInformation != null) {
                    if (releaseTime.getTime() <= lastInformation.getReleaseTime().getTime()) {
                        log.info("==========【上海证券报】==========【当前记录已抓取】");
                        break;
                    }
                }
                information.setReleaseTime(releaseTime);
                //图片
                String img = element.select("img").attr("src");
                //标题
                String title = element.select("a").attr("title");
                //描述
                String describe = element.select("p[class=des]").html();
                //作者
                String author = innerDoc.select("span[class=author]").html();
                //来源的特殊处理
                String source = innerDoc.select("span[class=source]>a").html();
                if (StringUtils.isEmpty(source)) {
                    String otherSource = innerDoc.select("span[class=source]").html();
                    if (!StringUtils.isEmpty(otherSource)) {
                        source = otherSource.substring(3);
                    }
                }
                if (!StringUtils.isEmpty(source)) {
                    information.setSource(source);
                } else {
                    information.setSource("上海证券报");
                }
                if (!StringUtils.isEmpty(author)) {
                    information.setAuthor(author.substring(3));
                }
                information.setMId(IDS.uuid2());
                information.setTitle(title);
                information.setLink(link);
                information.setType("证券");
                information.setWebsite("上海证券报");
                information.setCreateTime(new Date());
                information.setDescribe(describe);
                information.setImg(img);
            } catch (Exception e) {
                log.info("==========【上海证券报】==========【解析异常】", e);
                continue;
            }
            informationList.add(information);
        }
        mongoTemplate.insertAll(informationList);
        log.info("==========【上海证券报】==========【插入的数据条数】 size: {}", informationList.size());
        log.info("==========【上海证券报】==========【结束爬取数据】");
    }

//    public static void main(String[] args) {
//        String website = "http://www.cnstock.com/";
//        Document outDoc = JsoupUtill.getPageDocument(website);
//        List<Element> elements = outDoc.select("li[id]");
//        List<Information> informationList = new ArrayList<>();
//        for (Element element : elements) {
//            Information information = new Information();
//            try {
//                //链接
//                String link = element.select("a").attr("href");
//                //调用内层Link
//                Document innerDoc = JsoupUtill.getPageDocument(link);
//                //发布时间
//                String time = innerDoc.select("span[class = timer]").html();
//                //时间格式的特殊处理
//                if (StringUtils.isEmpty(time)) {
//                    String outTime = element.select("span[class=time]").html();
//                    time = DateToolUtils.getReqDate() + " " + outTime + ":00";
//                }
//                Date releaseTime = DateToolUtils.getDateByE(time);
//                if (releaseTime == null) {
//                    releaseTime = new Date();
//                }
////                //判断是否已经抓取
////                if (lastInformation != null) {
////                    if (releaseTime.getTime() <= lastInformation.getReleaseTime().getTime()) {
////                        log.info("==========【上海证券报】==========【当前记录已抓取】");
////                        break;
////                    }
////                }
//                information.setReleaseTime(releaseTime);
//                //图片
//                String img = element.select("img").attr("src");
//                //标题
//                String title = element.select("a").attr("title");
//                //描述
//                String describe = element.select("p[class=des]").html();
//                //作者
//                String author = innerDoc.select("span[class=author]").html();
//                //来源的特殊处理
//                String source = innerDoc.select("span[class=source]>a").html();
//                if (StringUtils.isEmpty(source)) {
//                    String otherSource = innerDoc.select("span[class=source]").html();
//                    if (!StringUtils.isEmpty(otherSource)) {
//                        source = otherSource.substring(3);
//                    }
//                }
//                if (!StringUtils.isEmpty(source)) {
//                    information.setSource(source);
//                } else {
//                    information.setSource("上海证券报");
//                }
//                if (!StringUtils.isEmpty(author)) {
//                    information.setAuthor(author.substring(3));
//                }
//                information.setMId(IDS.uuid2());
//                information.setTitle(title);
//                information.setLink(link);
//                information.setType("证券");
//                information.setWebsite("上海证券报");
//                information.setCreateTime(new Date());
//                information.setDescribe(describe);
//                information.setImg(img);
//            } catch (Exception e) {
//                log.info("==========【上海证券报】==========【解析异常】", e);
//                continue;
//            }
//            informationList.add(information);
//        }
//        System.out.println(JSON.toJSONString(informationList));
//    }
}
