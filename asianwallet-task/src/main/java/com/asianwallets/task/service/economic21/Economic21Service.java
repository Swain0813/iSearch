package com.asianwallets.task.service.economic21;

/**
 * @description: 21经济网业务接口
 * @author: XuWenQi
 * @create: 2019-09-26 10:32
 **/
public interface Economic21Service {

    /**
     * @description 爬取21经济网-商业模块信息
     */
    void grabBusiness();

    /**
     * @description 爬取21经济网-数字报模块信息
     */
    void grabDigitalPaper();
}
