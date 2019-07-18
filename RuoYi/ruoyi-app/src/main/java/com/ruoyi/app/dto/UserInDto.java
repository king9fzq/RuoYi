package com.ruoyi.app.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="user对象",description="用户对象user")
@Data
public class UserInDto {

    @ApiModelProperty(value="用户名",name="username",example="admin")
    private String username;
    @ApiModelProperty(value="密码",name="password",example="admin123")
    private String password;

}
