package com.asianwallets.task.service.economic21.impl;
import com.asianwallets.common.entity.Information;
import com.asianwallets.common.utils.DateToolUtils;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.common.utils.JsoupUtill;
import com.asianwallets.task.service.economic21.Economic21Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * @description: 21经济网业务接口实现类
 * @author: XuWenQi
 * @create: 2019-09-26 10:32
 * http://www.21jingji.com/channel/business/ 商业
 * http://epaper.21jingji.com/ 数字模块
 **/
@Service
@Slf4j
public class Economic21ServiceImpl implements Economic21Service {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @description 爬取21经济网-商业模块信息
     */
    @Override
    public void grabBusiness() {
        log.info("==========【21经济网商业模块】==========【开始爬取数据】");
        String website = "http://www.21jingji.com/channel/business/";
        Document outDoc = JsoupUtill.getPageDocument(website);
        List<Element> elements = outDoc.select("div[class=li]");
        if (elements.size() == 0) {
            log.info("==========【21经济网商业模块】==========【数据为空】");
            return;
        }
        //查询最后一条记录
        Information lastInformation = null;
        Criteria criteria = Criteria.where("website").is("21经济网");
        criteria.and("type").is("商业");
        Query query = new Query(criteria);
        Sort sort = new Sort(Sort.Direction.DESC, "releaseTime");
        query.with(sort).limit(1);
        List<Information> lastList = mongoTemplate.find(query, Information.class);
        if (lastList.size() > 0) {
            lastInformation = lastList.get(0);
            log.info("==========【21经济网商业模块】==========【前次最后一条记录为】: 【{}】", DateToolUtils.formatDate(lastInformation.getReleaseTime()));
        }
        List<Information> informationList = new ArrayList<>();
        for (Element element : elements) {
            Information information = new Information();
            try {
                //链接
                String link = element.select("a[href]").attr("href");
                //继续抓取标签内层链接
                Document linkDoc = JsoupUtill.getPageDocument(link);
                //年月日
                Elements yearMonth = linkDoc.select("p[class=Wh]>span[class=\"\"]");
                //时刻
                Elements hour = linkDoc.select("p[class=Wh]>span[class=hour]");
                Date releaseTime = null;
                //时间格式的特殊处理
                if (StringUtils.isEmpty(hour.html())) {
                    releaseTime = DateToolUtils.getDateByE(yearMonth.html());
                } else {
                    String yearMonthText = yearMonth.html();
                    String year = yearMonthText.substring(0, 4);
                    String month = yearMonthText.substring(5, 7);
                    String day = yearMonthText.substring(8, 10);
                    releaseTime = DateToolUtils.getReqDateyyyyMMddHHmm(year + month + day + hour.html());
                }
                if (releaseTime == null) {
                    releaseTime = new Date();
                }
                //判断是否已经抓取
                if (lastInformation != null) {
                    if (releaseTime.getTime() <= lastInformation.getReleaseTime().getTime()) {
                        log.info("==========【21经济网商业模块】==========【当前记录已抓取】");
                        break;
                    }
                }
                //图片
                String img = element.select("img").attr("src");
                //描述
                String describe = element.select("p[class=listCont]").html();
                //标题
                String title = element.select("a[class=listTit]").attr("title");
                //来源
                Elements source = linkDoc.select("p[class=Wh]>span[class=baodao]");
                //作者
                Elements author = linkDoc.select("p[class=Wh]>span[class=Wh1]");
                information.setMId(IDS.uuid2());
                information.setTitle(title);
                information.setLink(link);
                information.setImg(img);
                information.setDescribe(describe);
                information.setAuthor(author.html());
                information.setSource(source.html());
                information.setType("商业");
                information.setWebsite("21经济网");
                information.setReleaseTime(releaseTime);
                information.setCreateTime(new Date());
            } catch (Exception e) {
                log.info("==========【21经济网商业模块】==========【解析异常】", e);
                continue;
            }
            informationList.add(information);
        }
        mongoTemplate.insertAll(informationList);
        log.info("==========【21经济网商业模块】==========【插入的数据条数】 size: {}", informationList.size());
        log.info("==========【21经济网商业模块】==========【结束爬取数据】");
    }

    /**
     * @description 爬取21经济网-数字报模块信息
     */
    @Override
    public void grabDigitalPaper() {
        log.info("==========【21经济网商业模块-数字报】==========【开始爬取数据】");
        String website = "http://epaper.21jingji.com/";
        Document outDoc = JsoupUtill.getPageDocument(website);
        Elements elements = outDoc.select("map > area");
        if (elements.size() == 0) {
            log.info("==========【21经济网商业模块-数字报】==========【数据为空】");
            return;
        }
        //查询最后一条记录
        Information lastInformation = null;
        Criteria criteria = Criteria.where("website").is("21经济网-数字报");
        criteria.and("type").is("21经济网-数字报");
        Query query = new Query(criteria);
        Sort sort = new Sort(Sort.Direction.DESC, "releaseTime");
        query.with(sort).limit(1);
        List<Information> lastList = mongoTemplate.find(query, Information.class);
        if (lastList.size() > 0) {
            lastInformation = lastList.get(0);
            log.info("==========【21经济网商业模块-数字报】==========【前次最后一条记录为】: 【{}】", DateToolUtils.formatDate(lastInformation.getReleaseTime()));
        }
        List<Information> informationList = new ArrayList<>();
        for (Element element : elements) {
            Information information = new Information();
            try {
                //链接
                String link = element.attr("href");
                if (StringUtils.isEmpty(link) || link.endsWith("png")) {
                    continue;
                }
                //调用内层Link
                Document innerDoc = JsoupUtill.getPageDocument(link);
                //发布日期
                String newsDate = innerDoc.select("div[class=newsDate]").html();
                Date releaseTime = DateToolUtils.getReqDateyyyyMMddHHmmE(newsDate);
                if (releaseTime == null) {
                    releaseTime = new Date();
                }
                //判断是否已经抓取
                if (lastInformation != null) {
                    if (releaseTime.getTime() <= lastInformation.getReleaseTime().getTime()) {
                        log.info("==========【21经济网商业模块-数字报】==========【当前记录已抓取】");
                        break;
                    }
                }
                //标题
                String title = element.attr("alt");
                //类型
                //String type = JsoupUtill.getText(typeList.get(i).html());
                //来源与作者信息的特殊处理
                String newsInfo = JsoupUtill.getText(innerDoc.select("div[class=newsInfo]").html());
                if (!StringUtils.isEmpty(newsInfo)) {
                    String[] split = newsInfo.split(" ");
                    //来源
                    String source = split[0];
                    information.setSource(source);
                    if (split.length > 1) {
                        //作者
                        String author = split[1];
                        information.setAuthor(author);
                    }
                }
                information.setMId(IDS.uuid2());
                information.setTitle(title);
                information.setLink(link);
                information.setType("21经济网-数字报");
                information.setWebsite("21经济网-数字报");
                information.setReleaseTime(releaseTime);
                information.setCreateTime(new Date());
            } catch (Exception e) {
                log.info("==========【21经济网商业模块-数字报】==========【解析异常】", e);
                continue;
            }
            informationList.add(information);
        }
        mongoTemplate.insertAll(informationList);
        log.info("==========【21经济网商业模块-数字报】==========【插入的数据条数】 size: {}", informationList.size());
        log.info("==========【21经济网商业模块-数字报】==========【结束爬取数据】");
    }

//    public static void main(String[] args) {
//        String website = "http://epaper.21jingji.com/";
//        Document outDoc = JsoupUtill.getPageDocument(website);
//        Elements elements = outDoc.select("map > area");
//        Elements typeList = outDoc.select("div[class=catalogs]>ul>li");
//        System.out.println(typeList);
//    }
}
