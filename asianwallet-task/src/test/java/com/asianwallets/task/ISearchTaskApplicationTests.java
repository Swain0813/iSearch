package com.asianwallets.task;
import com.asianwallets.common.entity.Config;
import com.asianwallets.task.service.cnstock.CnStockService;
import com.asianwallets.task.service.chinaventure.ChinaventureService;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class ISearchTaskApplicationTests {

    @Autowired
    private Economic21Service economic21Service;

    @Autowired
    private NbdService nbdService;

    @Autowired
    private YiCaiService yiCaiService;

    @Autowired
    private CsService csService;

    @Autowired
    private XueqiuService xueqiuService;

    @Autowired
    private ChinaventureService chinaventureService;

    @Autowired
    private SinaService sinaService;

    @Autowired
    private CnStockService cnStockService;

    @Autowired
    private WeiboService weiboService;

    @Autowired
    private HuXiuService huXiuService;

    @Autowired
    private EastMoneyService eastMoneyService;

    @Autowired
    private WxBgwService wxBgwService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void test() {
        //economic21Service.grabBusiness();
        economic21Service.grabDigitalPaper();
        //cnStockService.grabCnStock();
        //huXiuService.grabHuXiu();
        //eastMoneyService.grab();
        //wxBgwService.grab();
        //cnStockService.grabCnStock();
        //Config config = new Config();
        //config.setCookie("noticeLoginFlag=1; remember_acct=swain_yx%40163.com; ua_id=9NT5VXKeVT1VgxnaAAAAAFvjRVhwb1jYFp5pVCFQfgI=; pgv_pvi=7217434624; pgv_si=s2657966080; cert=B6DkdyaIqCmmt9Gswvi8spwzmjbUty1T; ticket_id=gh_ae4a6ffeb772; noticeLoginFlag=1; remember_acct=swain_yx%40163.com; mm_lang=zh_CN; uuid=6633ba5107487fdf6b1317d8d7e4f227; bizuin=3253018254; ticket=6d603cd4f1fee9a2addc97c675c7923326676810; data_bizuin=3253018254; data_ticket=HeJPTJY5V2Q5pkQ7P+5O9Bp/J0RkeEvKhN9lcJjtrqJNpO6mniabvXUlPDGMYmNN; slave_sid=UXJYMXhOQ1BwZW9MbXNhYkVpQnJid21KdFY1OWFtWHdHamt4T0hYcWFGNDBFNU9CSThoVEZqRl83aVJyS2J4cmZzY05xd2luRkd2S3lnRVFNNEZaQmY2NFlvbnpHQjVQaDNSTHhzNzhjSmJGMlp2R0trUlE1dFRrekJvUjViTU00UnZwUlkzeUFjVm9nT2h0; slave_user=gh_ae4a6ffeb772; xid=b16926818b2263b8a60ab036442f45b2; openid2ticket_ojh7lwWyqFV_-WwwTeCJqA35VrmU=qn7Zh3axLOJn+UyohgAny7ZfAD48Z94lPjBt9Rx6Uks=");
        //config.setToken("1507570610");
        //mongoTemplate.save(config);
        //eastMoneyService.grab();
    }


    @Test
    public void grab(){
        xueqiuService.grab();
    }
}
