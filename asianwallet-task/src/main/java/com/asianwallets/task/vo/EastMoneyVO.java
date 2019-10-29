package com.asianwallets.task.vo;

import com.asianwallets.common.entity.Information;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EastMoneyVO {

    @ApiModelProperty("跳出循环标记")
    private String flag;

    @ApiModelProperty("information")
    private Information information;
}
