package com.ruoyi.app.controller.user;

import com.ruoyi.app.util.communication.Result;
import com.ruoyi.app.util.communication.ResultBuilder;
import com.ruoyi.app.util.communication.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IndexController {

    @GetMapping("/index")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result index() {

        return ResultBuilder.error(ResultCode.unauthorized);
    }

    @GetMapping("/403")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result unauthorized() {
        return ResultBuilder.error(ResultCode.FORBIDDEN);
    }
}
