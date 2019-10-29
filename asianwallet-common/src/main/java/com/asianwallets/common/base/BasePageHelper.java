package com.asianwallets.common.base;

import lombok.Data;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @version v1.0.0
 * @classDesc: 功能描述: 分页查询参数
 * @createTime 2018年6月29日 上午10:58:01
 * @copyright: 上海众哈网络技术有限公司
 */
@Data
public class BasePageHelper implements Serializable {


    private static final long serialVersionUID = -7068361700500939655L;

    /**
     * 排序字段
     */
    public String sort = "create_time";

    /**
     * 排序方向
     */
    public Sort.Direction order = DESC;

    /**
     * 页码
     */
    public Integer pageNum = 1;

    /**
     * 每页条数
     */
    public Integer pageSize = 20;

}
