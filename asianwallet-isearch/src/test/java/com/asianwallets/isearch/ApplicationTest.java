package com.asianwallets.isearch;

import com.asianwallets.common.entity.Information;
import com.asianwallets.common.entity.User;
import com.asianwallets.common.utils.IDS;
import com.asianwallets.isearch.dao.UserDao;
import com.asianwallets.isearch.dto.ConfigDTO;
import com.asianwallets.isearch.dto.UserDTO;
import com.asianwallets.isearch.service.BingService;
import com.asianwallets.isearch.service.ConfigService;
import com.asianwallets.isearch.service.UserKeyWordService;
import com.asianwallets.isearch.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserKeyWordService userKeyWordService;

    @Autowired
    private BingService bingService;

    @Autowired
    private ConfigService configService;


    @Test
    public void test() {
        List<User> users = userDao.findAll();
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void test1() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("aadd@aa1.com");
        userDTO.setMobile("185123456781");
        userDTO.setPassword("123456");
        userDTO.setNickName("DABB");
        userDTO.setRemark("ABC");
        userService.register(userDTO);
    }

    @Test
    public void test2() {

        //userKeyWordService.queryHotWords();
        for (int i = 0; i < 10; i++) {
            List<Information> list = bingService.search("小狗");
            System.out.println("result" + list);
        }


    }

    @Test
    public void test3() {
        //ConfigDTO configDTO = ConfigDTO.builder()
        //        .configId("3e2bc6935afc4b35be843ba445c3a7a9")
        //        .token("1841749622")
        //        .cookie("pgv_pvi=9525940224; pgv_pvid=2174111674; RK=wGr4t/epeU; ptcz=23ba777a22adc5d0efc8b70330297a4f7e93ceb6aa6fd5bd5bf286f5fa0a73f7; o_cookie=562988147; tvfe_boss_uuid=19a76418573250fd; pac_uid=1_562988147; eas_sid=91i5x5i729B056d6b610H4s3U0; LW_uid=s1P5O5I7n940U6I6x6K384C9v2; LW_sid=T1U5L5O7r960o6E6E7U4n194C5; pgv_pvid_new=562988147_ad01b9923f; ua_id=Kdi9NWePv3SfOZNZAAAAAEtX_KiCbRDIq8TCqif5Ino=; rewardsn=; wxtokenkey=777; wxuin=1774275314; devicetype=Windows10; version=62060833; lang=zh_CN; uin=o0562988147; ptisp=ctc; pgv_info=ssid=s9920116560; pgv_si=s5647122432; pass_ticket=KTw1O42CatmLIs/AETq3u2M0E5rxz+R68saWEeQOx7IjTnrP4+m+NoLuv8pzPFDd; wap_sid2=CPKVhc4GElxvVXdlTUtxRnpaSENnLTRFZEhkWFF6UWNRUjRTUUxJUXprZG5tQzNESlYwd2J6RHNZdnlmUzVGSEVyYjhpSDdxcEtWa08xN0swdko2NTl1eWxpNkR6Z1lFQUFBfjCY3PbsBTgNQAE=; cert=kTRN9bPaLfXyf23bS2JQW8GvcieeEVlG; sig=h0137f44e67532b63c11a5167459def5445bd09970c2749d2c106b5ccb6f269eb2c1649d2dc2dce39e6; skey=@7ZlDTZn1P; data_bizuin=3261633696; data_ticket=8pWwrb/f9W7mdmDC6rOGkrspkNtGSe9a1owy+0iNPNFOPhUBc7pMevh+lr/XQ6Jk; master_key=4qw2JKP0qjLMgmvhWGhlOluG9NNm2+S3/NQyNHP+OYo=; master_user=gh_93338cc2aa03; master_sid=Z3pETWZWU0tvVlhVVmRjdnRiQVJoQ0FRMlR6NDB2MUVvQVJIZHdaQkNvWU1HOXZoMVB0Rld5dEdSVEN1S2o3WkRwUHYxUW02WXA5RDlnWE9nZmJxVTlCM1JYeDZmZXROSXVqZnd0anRCeENlTWJpZktwTmxNSmNPa2liWGJ4VDVKbWtZVFVTTHNsSE5aTHhl; master_ticket=c3bece35b21a526d9d4729a4c764bd77; bizuin=3261633696; slave_user=gh_93338cc2aa03; slave_sid=Y0hwVENqNTV4ZDRnZWRINGxvSjNLbjFiRDE3WmIxeGhmWHJjUVB5aW9sUEVIR0NXWkpOa2xPbGVZS25hMjFqX3JaQ18wOEhkZF94bjZVQTJMTDZIQzBWaTZmTkl1WXozOG9CSjR0Rmd0VGJlc3lKS1dPUXRSRVNBaEthYU82MjBDWVdkT2RDd3c2YXBRSzAx")
        //        .build();
        ////configService.insertConfig(configDTO);
        //configService.updateConfig(configDTO);
    }
}

