package com.asianwallets.task.service.huxiu.impl;
import com.asianwallets.common.entity.Information;
import com.asianwallets.common.utils.DateToolUtils;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.common.utils.JsoupUtill;
import com.asianwallets.task.service.huxiu.HuXiuService;
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
 * @description: 虎嗅网业务接口实现类
 * @author: XuWenQi
 * @create: 2019-09-27 11:27
 * https://www.huxiu.com/article
 **/
@Service
@Slf4j
public class HuXiuServiceImpl implements HuXiuService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @description 爬取虎嗅网资讯信息
     */
    @Override
    public void grabHuXiu() {
        log.info("==========【虎嗅网】==========【开始爬取数据】");
        String website = "https://www.huxiu.com/article";
        Document outDoc = JsoupUtill.getPageDocument(website);
        Elements elements = outDoc.select("div[class = article-item article-item--big]");
        if (elements.size() == 0) {
            log.info("==========【虎嗅网】==========【数据为空】");
            return;
        }
        //查询最后一条记录
        Information lastInformation = null;
        Criteria criteria = Criteria.where("website").is("虎嗅");
        criteria.and("type").is("资讯");
        Query query = new Query(criteria);
        Sort sort = new Sort(Sort.Direction.DESC, "releaseTime");
        query.with(sort).limit(1);
        List<Information> lastList = mongoTemplate.find(query, Information.class);
        if (lastList.size() > 0) {
            lastInformation = lastList.get(0);
            log.info("==========【虎嗅网】==========【前次最后一条记录为】: 【{}】", DateToolUtils.formatDate(lastInformation.getReleaseTime()));
        }
        List<Information> informationList = new ArrayList<>();
        for (Element element : elements) {
            Information information = new Information();
            try {
                //链接
                String link = "https://www.huxiu.com" + element.select("a[data-hx]").attr("href");
                //调用内层Link
                Document innerDoc = JsoupUtill.getPageDocument(link);
                String time = innerDoc.select("span[class = article__time]").html();
                //时间格式的特殊处理
                if (StringUtils.isEmpty(time)) {
                    time = innerDoc.select("span[class = article-time]").html();
                }
                if (StringUtils.isEmpty(time)) {
                    time = innerDoc.select("span[class = m-article-time]").html();
                }
                Date releaseTime = DateToolUtils.getReqDateyyyyMMddHHmmE(time);
                if (releaseTime == null) {
                    releaseTime = new Date();
                }
                //判断是否已经抓取
                if (lastInformation != null) {
                    if (releaseTime.getTime() <= lastInformation.getReleaseTime().getTime()) {
                        log.info("==========【虎嗅网】==========【当前记录已抓取】");
                        break;
                    }
                }
                //图片
                String img = element.select("img[src]").attr("src");
                //标题
                String title = element.select("img[alt]").attr("alt");
                //描述
                String describe = element.select("p[class = article-item__content__intro single-line-overflow]").html();
                //来源
                String source = innerDoc.select("meta[name = author]").attr("content");
                Elements remark = innerDoc.select("span[class = text-remarks]");
                String contents = innerDoc.select("span[class = text-remarks]:contains(作者)").html();
                //作者信息的特殊处理
                if (!StringUtils.isEmpty(contents)) {
                    String[] split = contents.split("\n");
                    //作者
                    String author = split[0];
                    information.setAuthor(author);
                }
                information.setMId(IDS.uuid2());
                information.setTitle(title);
                information.setLink(link);
                //来源的特殊处理
                if (remark != null && remark.size() != 0 && remark.get(0).html().contains("来自")) {
                    information.setSource(remark.get(0).html() + source);
                } else {
                    information.setSource(source);
                }
                information.setDescribe(describe);
                information.setImg(img);
                information.setType("资讯");
                information.setWebsite("虎嗅");
                information.setReleaseTime(releaseTime);
                information.setCreateTime(new Date());
            } catch (Exception e) {
                log.info("==========【虎嗅网】==========【解析异常】", e);
                continue;
            }
            informationList.add(information);
        }
        mongoTemplate.insertAll(informationList);
        log.info("==========【虎嗅网】==========【结束爬取数据】");
    }
}
