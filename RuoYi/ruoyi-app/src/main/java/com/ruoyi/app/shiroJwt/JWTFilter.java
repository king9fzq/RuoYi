package com.ruoyi.app.shiroJwt;

import com.ruoyi.app.util.JsonUtil;
import com.ruoyi.app.util.communication.Result;
import com.ruoyi.app.util.communication.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

/**
 * @ClassName: JWTFilter
 * @Description:
 * @Author fzq
 * @DateTime 2019年7月3日 下午4:52:19
 */
@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {

    /**
     * 执行登录认证     *
     * * @param request
     * * @param response
     * * @param mappedValue
     * * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            executeLogin(request, response);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected void out(ServletResponse response,Exception e) throws IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String json = JsonUtil.toString(
                Result.builder()
                        .code(ResultCode.unauthorized.getCode())
                        .msg(e.getMessage())
                        .build());

        out.print(json);
        out.flush();
        out.close();
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("Authorization");

        log.warn("token:"+token);
        log.warn(httpServletRequest.getRequestURL().toString());
        log.warn(httpServletRequest.getRequestURI());
        JWTToken jwtToken = new JWTToken(token);

        try {
            // 提交给realm进行登入，如果错误他会抛出异常并被捕获
            getSubject(request, response).login(jwtToken);
        }catch (Exception e){
            out(response,e);
            return false;
        }

        PrincipalCollection session =  getSubject(request,response).getPrincipals();
        log.warn("session:"+session);

        Iterator iterator = session.iterator();
        while (true){
            if(!iterator.hasNext()) break;
            log.warn(iterator.next().toString());
        }
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}

