package com.asianwallets.task.scheduled;
import com.asianwallets.task.service.chinaventure.ChinaventureService;
import com.asianwallets.task.service.cnstock.CnStockService;
import com.asianwallets.task.service.cs.CsService;
import com.asianwallets.task.service.eastmoney.EastMoneyService;
import com.asianwallets.task.service.economic21.Economic21Service;
import com.asianwallets.task.service.huxiu.HuXiuService;
import com.asianwallets.task.service.nbd.NbdService;
import com.asianwallets.task.service.sina.SinaService;
import com.asianwallets.task.service.weibo.WeiboService;
import com.asianwallets.task.service.wxbgw.WxBgwService;
import com.asianwallets.task.service.xueqiu.XueqiuService;
import com.asianwallets.task.service.yicai.YiCaiService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 第三方新闻网站获取新闻信息落地
 * 每15分钟获取一次
 *
 */
@Component
@Slf4j
@Api(value = "第三方新闻网站获取新闻信息落地")
public class GetNewsTask {

    /**
     * 投中网
     */
    @Autowired
    private ChinaventureService chinaventureService;

    /**
     *上海证券报
     */
    @Autowired
    private CnStockService cnStockService;

    /**
     *中证网
     */
    @Autowired
    private CsService csService;

    /**
     *21经济网
     * 21经济网-数字报
     */
    @Autowired
    private Economic21Service economic21Service;

    /**
     * 虎嗅
     */
    @Autowired
    private HuXiuService huXiuService;

    /**
     * 每经网
     */
    @Autowired
    private NbdService nbdService;

    /**
     * 新浪财经
     */
    @Autowired
    private SinaService sinaService;

    /**
     * 微博
     */
    @Autowired
    private WeiboService weiboService;


    /**
     * 雪球
     */
    @Autowired
    private XueqiuService xueqiuService;

    /**
     * 第一财经
     */
    @Autowired
    private YiCaiService yiCaiService;

    /**
     * 东方财富网
     */
    @Autowired
    private EastMoneyService eastMoneyService;

    /**
     * 微信并购汪
     */
    @Autowired
    private WxBgwService wxBgwService;


    /**
     * 从第三方获取新闻信息-投中网
     */
    @Scheduled(cron = "${getinfo.newstime}")
    public void getChinaventure(){
        log.info("********************从投中网获取新闻信息开始***************");
        chinaventureService.grab();
        log.info("********************从投中网获取新闻信息结束***************");
    }


    /**
     * 从第三方获取新闻信息-上海证券报
     */
    @Scheduled(cron = "${getinfo.newstime}")
    public void getCnStock(){
        log.info("********************从上海证券报获取新闻信息开始***************");
        cnStockService.grabCnStock();
        log.info("********************从上海证券报获取新闻信息结束***************");
    }


    /**
     * 从第三方获取新闻信息-中证网
     */
    @Scheduled(cron = "${getinfo.newstime}")
    public void getCs(){
        log.info("********************从中证网获取新闻信息开始***************");
        csService.grab();
        log.info("********************从中证网报获取新闻信息结束***************");
    }


    /**
     * 从第三方获取新闻信息-21世纪经济网
     */
    @Scheduled(cron = "${getinfo.newstime}")
    public void grabBusiness(){
        log.info("********************从21世纪经济网获取新闻信息开始***************");
        economic21Service.grabBusiness();
        log.info("********************从21世纪经济网获取新闻信息结束***************");
    }

    /**
     * 从第三方获取新闻信息-21经济网-数字报
     */
    @Scheduled(cron = "${getinfo.newstime}")
    public void grabDigitalPaper(){
        log.info("********************从21经济网-数字报获取新闻信息开始***************");
        economic21Service.grabDigitalPaper();
        log.info("********************从21经济网-数字报获取新闻信息结束***************");
    }

    /**
     * 从第三方获取新闻信息-虎嗅
     */
    @Scheduled(cron = "${getinfo.newstime}")
    public void grabHuXiu(){
        log.info("********************从虎嗅获取新闻信息开始***************");
        huXiuService.grabHuXiu();
        log.info("********************从虎嗅获取新闻信息结束***************");
    }

    /**
     * 从第三方获取新闻信息-每经网
     */
    @Scheduled(cron = "${getinfo.newstime}")
    public void getNbd(){
        log.info("********************从每经网获取新闻信息开始***************");
        nbdService.grab();
        log.info("********************从每经网获取新闻信息结束***************");
    }

    /**
     * 从第三方获取新闻信息-新浪财经
     */
    @Scheduled(cron = "${getinfo.newstime}")
    public void getSinaFinance(){
        log.info("********************从新浪财经获取新闻信息开始***************");
        sinaService.grab();
        log.info("********************从新浪财经获取新闻信息结束***************");
    }

    /**
     * 从第三方获取新闻信息-微博
     */
    @Scheduled(cron = "${getinfo.newstime}")
    public void getWeibo(){
        log.info("********************从微博获取新闻信息开始***************");
        weiboService.grab();
        log.info("********************从微博获取新闻信息结束***************");
    }

    /**
     * 从第三方获取新闻信息-雪球
     */
    @Scheduled(cron = "${getinfo.newstime}")
    public void getXueQiu(){
        log.info("********************从雪球获取新闻信息开始***************");
        xueqiuService.grab();
        log.info("********************从雪球获取新闻信息结束***************");
    }


    /**
     * 从第三方获取新闻信息-第一财经
     */
    @Scheduled(cron = "${getinfo.newstime}")
    public void getYiCai(){
        log.info("********************从第一财经获取新闻信息开始***************");
        yiCaiService.grab();
        log.info("********************从第一财经获取新闻信息结束***************");
    }

    /**
     * 从第三方获取新闻信息-东方财富网
     */
    @Scheduled(cron = "${getinfo.newstime}")
    public void getEastMoney(){
        log.info("********************从东方财富网获取新闻信息开始***************");
        eastMoneyService.grab();
        log.info("********************从东方财富网获取新闻信息结束***************");
    }

    /**
     * 从第三方获取新闻信息-微信并购汪
     */
    @Scheduled(cron = "${getinfo.newstime}")
    public void getWxBgw(){
        log.info("********************从微信并购汪获取新闻信息开始***************");
        wxBgwService.grab();
        log.info("********************从微信并购汪获取新闻信息结束***************");
    }
}
