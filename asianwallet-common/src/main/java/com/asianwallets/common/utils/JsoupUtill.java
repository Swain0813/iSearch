package com.asianwallets.common.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import java.io.IOException;

/**
 * jsoup处理工具类
 */
public class JsoupUtill {

    /**
     * 获得指定url的document对象
     *
     * @param url:网址
     * @return
     */
    public static Document getPageDocument(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).timeout(12000).get();// 设置连接超时时间
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }


    /**
     * 获取html标签里的纯文本
     *
     * @param html html文本
     * @return html标签里的纯文本内容
     */
    public static String getText(String html) {
        if (html == null)
            return null;
        return Jsoup.clean(html, Whitelist.none());
    }

}
