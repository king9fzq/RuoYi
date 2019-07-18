package com.ruoyi.app.controller.user;

import com.ruoyi.app.dto.UserInDto;
import com.ruoyi.app.exception.UserNamePwdErrorException;
import com.ruoyi.app.shiroJwt.JWTUtil;
import com.ruoyi.app.util.communication.Result;
import com.ruoyi.app.util.communication.ResultBuilder;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RequestMapping("user")
@RestController
public class UserController{

    @Resource
    private ISysUserService iSysUserService;

    @ApiOperation(value="登录功能", notes="用户密码登录")
    @PostMapping("/login")
    public Result login(@RequestBody @ApiParam(name="userInDto",value="username-用户名,password-密码")
                                    UserInDto userInDto){
        String username = userInDto.getUsername();
        String password = userInDto.getPassword();

        SysUser user =iSysUserService.selectUserByLoginName(username);
        if(null==user)throw UserNamePwdErrorException.build();
        String encryptPwd = encryptPassword(username,password,user.getSalt());

        //验证密码
        if (!user.getPassword().equals(encryptPwd)) throw UserNamePwdErrorException.build();

        String token = JWTUtil.sign(username, encryptPwd);
        return ResultBuilder.success(token);
    }

    @GetMapping("/findOwn")
    public Result findOwn(){

        SysUser user = iSysUserService.selectUserById(1L);
        return ResultBuilder.success(user);
    }

    public static String encryptPassword(String username, String password, String salt){
        return new Md5Hash(username + password + salt).toHex().toString();
    }

    public static void main(String[] args) {

        System.out.println(encryptPassword("admin","admin123","111111"));
    }
}



