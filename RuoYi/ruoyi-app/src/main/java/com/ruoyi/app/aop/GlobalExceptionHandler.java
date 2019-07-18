package com.ruoyi.app.aop;

import com.ruoyi.common.exception.MyException;
import com.ruoyi.app.util.communication.Result;
import com.ruoyi.app.util.communication.ResultBuilder;
import com.ruoyi.app.util.communication.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: GlobalExceptionHandler
 * @Description: 
 * @Author fzq
 * @DateTime 2019年7月3日 下午5:23:30
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final ExecutorService e = Executors.newSingleThreadExecutor();

    //自定义异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MyException.class)
    public Result handle400(MyException e){
        printLog(e);
        return Result.builder().code(400)
                .msg(e.getMessage())
                .build();
    }

    //没得对应对的角色或者权限
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public Result handle401(UnauthorizedException e){
        printLog(e);
        return ResultBuilder.error(ResultCode.unauthorized);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public Result handle403(AuthenticationException e){
        printLog(e);
        return ResultBuilder.error(ResultCode.unauthorized);
    }

    // 捕捉其他所有异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result globalException(Exception e) {
        printLog(e);
        return ResultBuilder.error(ResultCode.SYSTEM_ERROR);
    }

    public void printLog(Exception ex){

        //先记录到日志
        e.execute(new Runnable() {
            public void run() {
                //打印到文件
                log.error("开始记录异常：" + new Timestamp(System.currentTimeMillis()) +
                        "===============================================================================================");
                log.error("=========发生" + ex + "异常=============");
                StackTraceElement[] els = ex.getStackTrace();
                for (StackTraceElement e : els) {
                    log.error(e.toString());
                }
            }
        });
    }
}