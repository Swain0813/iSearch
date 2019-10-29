package com.asianwallets.task.service.eastmoney.impl;
import com.asianwallets.common.entity.Information;
import com.asianwallets.common.utils.DateToolUtils;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.common.utils.JsoupUtill;
import com.asianwallets.task.service.eastmoney.EastMoneyService;
import com.asianwallets.task.vo.EastMoneyVO;
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
 * @description: 东方财富网业务接口实现类
 * @author: XuWenQi
 * @create: 2019-09-29 15:26
 * http://finance.eastmoney.com/
 **/
@Service
@Slf4j
class EastMoneyServiceImpl implements EastMoneyService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @description 爬取东方财富网
     */
    @Override
    public void grab() {
        log.info("==========【东方财富网】==========【开始爬取数据】");
        //查询最后一条记录
        Information lastInformation = null;
        Criteria criteria = Criteria.where("website").is("东方财富网");
        criteria.and("type").is("财经");
        Query query = new Query(criteria);
        Sort sort = new Sort(Sort.Direction.DESC, "releaseTime");
        query.with(sort).limit(1);
        List<Information> lastList = mongoTemplate.find(query, Information.class);
        if (lastList.size() > 0) {
            lastInformation = lastList.get(0);
            log.info("==========【东方财富网】==========【前次最后一条记录为】: 【{}】", DateToolUtils.formatDate(lastInformation.getReleaseTime()));
        }
        String website = "http://finance.eastmoney.com/";
        Document outDoc = JsoupUtill.getPageDocument(website);
        //要闻
        Elements yaowen = outDoc.select("div[class = content mt0] >ul>li");
        //财经导读
        Elements daodu = outDoc.select("ul[class = list list_side] > li");
        //新闻
        Elements news = outDoc.select("ul[class = list list_common dot]>li");
        yaowen.addAll(daodu);
        yaowen.addAll(news);
        List<Information> informationList = new ArrayList<>();
        for (Element element : yaowen) {
            EastMoneyVO eastMoneyVO = common(element, lastInformation);
            if (eastMoneyVO.getInformation() != null) {
                informationList.add(eastMoneyVO.getInformation());
            }
        }
        mongoTemplate.insertAll(informationList);
        log.info("==========【东方财富网】==========【插入的数据条数】 size: {}", informationList.size());
        log.info("==========【东方财富网】==========【结束爬取数据】");
    }

    public static EastMoneyVO common(Element element, Information lastInformation) {
        EastMoneyVO eastMoneyVO = new EastMoneyVO();
        Information information = new Information();
        try {
            //链接
            String link = element.select("a").attr("href");
            Document innerDoc = JsoupUtill.getPageDocument(link);
            String time = innerDoc.select("div[class=time]").html();
            if (StringUtils.isEmpty(time)) {
                return eastMoneyVO;
            }
            String year = time.substring(0, 4);
            String month = time.substring(5, 7);
            String day = time.substring(8, 10);
            String hour = time.substring(12);
            Date releaseTime = DateToolUtils.getReqDateyyyyMMddHHmm(year + month + day + hour);
            if (releaseTime == null) {
                releaseTime = new Date();
            }
            //判断是否已经抓取
            if (lastInformation != null && lastInformation.getReleaseTime() != null) {
                if (releaseTime.getTime() <= lastInformation.getReleaseTime().getTime()) {
                    log.info("==========【东方财富网】==========【当前记录已抓取】");
                    return eastMoneyVO;
                }
            }
            //来源
            String source = innerDoc.select("div[class=source data-source]").attr("data-source");
            //作者
            String author = innerDoc.select("div[class=author]").html();
            //标题
            String title = element.select("a").html();
            if (!StringUtils.isEmpty(author)) {
                author = author.substring(3);
                information.setSource(source);
            }
            information.setMId(IDS.uuid2());
            information.setTitle(title);
            information.setLink(link);
            information.setAuthor(author);
            information.setType("财经");
            information.setWebsite("东方财富网");
            information.setReleaseTime(releaseTime);
            information.setCreateTime(new Date());
        } catch (Exception e) {
            log.info("==========【东方财富网】==========【解析异常】", e);
            return eastMoneyVO;
        }
        eastMoneyVO.setInformation(information);
        return eastMoneyVO;
    }

//    public static void main(String[] args) {
//        String website = "http://finance.eastmoney.com/";
//        Document outDoc = JsoupUtill.getPageDocument(website);
//        Elements yaowen = outDoc.select("div[class = content mt0] >ul>li");
//        Elements daodu = outDoc.select("ul[class = list list_side] > li");
//        Elements news = outDoc.select("ul[class = list list_common dot]>li");
//        List<Information> informationList = new ArrayList<>();
//        for (Element element : yaowen) {
//            EastMoneyVO eastMoneyVO = common(element, new Information());
//            if (eastMoneyVO.getInformation() != null) {
//                informationList.add(eastMoneyVO.getInformation());
//            }
//        }
//        for (Element element : daodu) {
//            EastMoneyVO eastMoneyVO = common(element, new Information());
//            if (eastMoneyVO.getInformation() != null) {
//                informationList.add(eastMoneyVO.getInformation());
//            }
//        }
//        for (Element element : news) {
//            EastMoneyVO eastMoneyVO = common(element, new Information());
//            if (eastMoneyVO.getInformation() != null) {
//                informationList.add(eastMoneyVO.getInformation());
//            }
//        }
//        System.out.println(JSON.toJSONString(informationList));
//    }
}
