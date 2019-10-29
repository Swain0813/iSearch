package com.asianwallets.task.service.weibo.impl;
import com.asianwallets.common.entity.Information;
import com.asianwallets.common.utils.DateToolUtils;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.task.service.weibo.WeiboService;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 微博头条
 * @author: YangXu
 * @create: 2019-09-27 15:30
 * https://d.weibo.com/623751_6?from=faxian_hot&mod=fenlei#
 **/
@Service
@Slf4j
public class WeiboServiceImpl implements WeiboService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @return
     * @Author YangXu
     * @Date 2019/9/26
     * @Descripate 抓取微博
     **/
    @Override
    public void grab() {
        WebClient client = new WebClient(BrowserVersion.FIREFOX_52);
        try {
            client.getOptions().setJavaScriptEnabled(true);    //默认执行js，如果不执行js，则可能会登录失败，因为用户名密码框需要js来绘制。
            client.getOptions().setCssEnabled(false);
            client.setAjaxController(new NicelyResynchronizingAjaxController());
            client.getOptions().setThrowExceptionOnScriptError(false);
            HtmlPage page = client.getPage("http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.3.16)");
            //登录
            HtmlInput ln = page.getHtmlElementById("username");
            HtmlInput pwd = page.getHtmlElementById("password");
            HtmlInput btn = page.getFirstByXPath("//div[@class='form_mod']/ul/li[7]/div[@class='btn_mod']/input");
            //微博账号
            ln.setAttribute("value", "15618906770");
            //微博密码
            pwd.setAttribute("value", "aw123456");
            HtmlPage page2 = btn.click();
            //登录完成，现在可以爬取任意你想要的页面了。
            HtmlPage page3 = client.getPage("https://d.weibo.com/623751_6?from=faxian_hot&mod=fenlei#");
            Thread.sleep(1000 * 10);//等待js渲染加载页面，不然获取到的页面body内容不全
            Document doc = Jsoup.parse(page3.asXml(), "https://d.weibo.com/");
            List<Element> list = new ArrayList<>();
            Elements trs = doc.select("ul[class=pt_ul clearfix]>li");
            list.addAll(trs);
            log.info("*******抓取微博的内容***********:{}", list.size());
            if (list == null || list.size() == 0) {
                return;
            }
            //查询最后一条记录
            Information lastInformation = null;
            Criteria criteria = Criteria.where("website").is("微博头条");
            criteria.and("type").is("财经");
            Query query = new Query(criteria);
            Sort sort = new Sort(Sort.Direction.DESC, "releaseTime");
            query.with(sort).limit(1);
            List<Information> lastlist = mongoTemplate.find(query, Information.class);
            if (lastlist.size() > 0) {
                lastInformation = lastlist.get(0);
                log.info("============= 抓取微博头条 ============ ,前次最后一条记录为：【{}】", DateToolUtils.formatDate(lastInformation.getReleaseTime()));
            }

            List<Information> informationList = new ArrayList<>();

            for (Element e : list) {
                Information information = new Information();
                try {
                    String title = e.select("div[class=title W_autocut]>a").html();
                    String link = e.select("div[class=title W_autocut]>a").attr("abs:href");
                    String describe = e.select("div[class=text text_cut S_txt2]").html();
                    String source = e.select("div[class=subinfo_box clearfix]>a").get(1).select("span").html();
                    Date releaseTime = DateToolUtils.getDateByStr("2019-" + e.select("div[class=subinfo_box clearfix]>span").get(0).html().replaceAll("(?:年|月)", "-").replace("日", "") + ":00");
                    if (releaseTime == null) {
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
                    information.setWebsite("微博头条");
                    information.setType("财经");
                    information.setCreateTime(new Date());
                } catch (Exception e1) {
                    log.info("=========== 抓取新浪财经 ========== 解析异常 :【{}】", e1);
                    continue;
                }
                informationList.add(information);
            }
            mongoTemplate.insertAll(informationList);
        } catch (Exception e) {
            log.info("=========== 抓取新浪财经 ========== 解析异常 :【{}】", e);
        } finally {
            client.close();
        }

    }


    //public static void main(String[] args) throws IOException {
    //    WebClient client = new WebClient(BrowserVersion.CHROME);
    //    client.getOptions().setJavaScriptEnabled(true);    //默认执行js，如果不执行js，则可能会登录失败，因为用户名密码框需要js来绘制。
    //    client.getOptions().setCssEnabled(false);
    //    client.setAjaxController(new NicelyResynchronizingAjaxController());
    //    client.getOptions().setThrowExceptionOnScriptError(false);
    //    HtmlPage page = client.getPage("http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.3.16)");
    //    //登录
    //    HtmlInput ln = page.getHtmlElementById("username");
    //    HtmlInput pwd = page.getHtmlElementById("password");
    //    HtmlInput btn = page.getFirstByXPath("//div[@class='form_mod']/ul/li[7]/div[@class='btn_mod']/input");
    //    //微博账号
    //    ln.setAttribute("value", "15618906770");
    //    //微博密码
    //    pwd.setAttribute("value", "aw123456");
    //    HtmlPage page2 = btn.click();
    //    //登录完成，现在可以爬取任意你想要的页面了。
    //    HtmlPage page3 = client.getPage("https://d.weibo.com/623751_6?from=faxian_hot&mod=fenlei#");
    //    Document doc = Jsoup.parse(page3.asXml(), "https://d.weibo.com/");
    //    System.out.println(doc);
    //}
}
