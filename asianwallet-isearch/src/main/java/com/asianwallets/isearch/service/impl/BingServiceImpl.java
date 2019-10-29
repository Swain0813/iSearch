package com.asianwallets.isearch.service.impl;
import java.io.IOException;
import java.util.*;
import com.asianwallets.common.entity.Information;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.common.utils.JsoupUtill;
import com.asianwallets.isearch.service.BingService;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 * @description: 必应业务接口实现类
 * @author: XuWenQi
 * @create: 2019-09-29 11:27
 **/
@Service
@Slf4j
public class BingServiceImpl implements BingService {
    /**
     * 根据关键字调用必应API搜索
     *
     * @param key 关键字
     * @return informationList
     */
    @Override
    public List<Information> search(String key) {
        log.info("==========【必应网页搜索】========== key: {}", key);
        List<Information> resultList = new ArrayList<>();
        WebClient client = new WebClient(BrowserVersion.FIREFOX_60);
        //默认执行js
        client.getOptions().setJavaScriptEnabled(true);
        client.getOptions().setCssEnabled(false);
        client.setAjaxController(new NicelyResynchronizingAjaxController());
        client.getOptions().setThrowExceptionOnScriptError(false);
        //拼接抓取的URL
        String searchUrl = "https://cn.bing.com/search?q=" + key;
        try {
            HtmlPage firstPage = client.getPage(searchUrl);
            if (firstPage == null) {
                log.info("==========【必应网页搜索】==========【网页抓取内容不正确】");
                return resultList;
            }
            //解析第一页的内容
            Document firstDoc = Jsoup.parse(firstPage.asXml());
            //选取第二页内容元素
            Elements secondElement = firstDoc.select("a[aria-label=Page 2]");
            if (secondElement == null || secondElement.size() == 0) {
                //进入到这里,说明第一页爬取到的内容不正确
                for (int i = 0; i < 10; i++) {
                    //再循环爬取5次第一页的内容
                    firstDoc = selectByUrl(client, searchUrl);
                    if (firstDoc != null && firstDoc.select("a[aria-label=Page 2]") != null && firstDoc.select("a[aria-label=Page 2]").size() != 0) {
                        //此时说明第一页的内容是正确的的,那么第二页的内容也是正确的
                        secondElement = firstDoc.select("a[aria-label=Page 2]");
                        break;
                    }
                }
                if (secondElement == null || secondElement.size() == 0) {
                    //进入到这里,说明第一页爬取到的内容不正确,循环调用后也没有抓取到正确页面
                    log.info("==========【必应网页搜索】==========【网页抓取内容不正确】");
                    return resultList;
                }
            }
            //第二页的链接
            String secondLink = "https://cn.bing.com/" + secondElement.attr("href");
            log.info("==========【必应网页搜索】==========【SecondLink】: {}", secondLink);
            HtmlPage secondPage = client.getPage(secondLink);
            if (secondPage == null) {
                log.info("==========【必应网页搜索】==========【网页抓取内容不正确】");
                return resultList;
            }
            Document secondDoc = Jsoup.parse(secondPage.asXml());
            //第一页内容文档List
            Elements firstDocs = firstDoc.select("li[class=b_algo]");
            //第二页内容文档List
            Elements secondDocs = secondDoc.select("li[class=b_algo]");
            for (Element element : firstDocs) {
                String link = element.select("a[target=_blank]").attr("href");
                String title = JsoupUtill.getText(element.select("a[target=_blank]").html());
                if (!StringUtils.isEmpty(title)) {
                    title = title.replace(" Translate this page", "");
                }
                Information information = new Information();
                information.setMId(IDS.uuid2());
                information.setTitle(title);
                information.setLink(link);
                information.setType("Bing");
                information.setSource("Bing");
                information.setWebsite("Bing");
                information.setCreateTime(new Date());
                information.setReleaseTime(new Date());
                resultList.add(information);
            }
            for (Element element : secondDocs) {
                String link = element.select("a[target=_blank]").attr("href");
                String title = JsoupUtill.getText(element.select("a[target=_blank]").html());
                if (!StringUtils.isEmpty(title)) {
                    title = title.replace(" Translate this page", "");
                }
                Information information = new Information();
                information.setMId(IDS.uuid2());
                information.setTitle(title);
                information.setLink(link);
                information.setType("Bing");
                information.setSource("Bing");
                information.setWebsite("Bing");
                information.setCreateTime(new Date());
                information.setReleaseTime(new Date());
                resultList.add(information);
            }
        } catch (Exception e) {
            log.info("==========【必应网页搜索】==========【接口异常】", e);
        } finally {
            client.close();
        }
        return resultList;
    }

    private Document selectByUrl(WebClient client, String url) throws IOException {
        HtmlPage page = client.getPage(url);
        return Jsoup.parse(page.asXml());
    }

//    public static void main(String[] args) throws UnsupportedEncodingException {
//        try {
//
//            List<BrowserVersion> list = new ArrayList<>();
//            list.add(BrowserVersion.FIREFOX_60);
//            list.add(BrowserVersion.FIREFOX_52);
//            list.add(BrowserVersion.INTERNET_EXPLORER);
//            list.add(BrowserVersion.CHROME);
//            list.add(BrowserVersion.EDGE);
//            WebClient client = new WebClient(list.get((int) (Math.random() * 5 + 1)));
//            //默认执行js
//            client.getOptions().setJavaScriptEnabled(true);
//            client.getOptions().setCssEnabled(false);
//            client.setAjaxController(new NicelyResynchronizingAjaxController());
//            client.getOptions().setThrowExceptionOnScriptError(false);
//            String searchUrl = "https://cn.bing.com/search?q=香港&go=搜索&qs=ds&form=QBRE";
//            HtmlPage firstPage = client.getPage(searchUrl);
//
//            Thread.sleep(1000 * 3);
//            System.out.println(firstPage.asXml());
//        } catch (Exception e) {
//
//        }
//    }
}
