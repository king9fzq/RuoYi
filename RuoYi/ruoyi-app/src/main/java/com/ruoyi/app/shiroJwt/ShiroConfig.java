package com.ruoyi.app.shiroJwt;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName: ShiroConfig
 * @Description:
 * @Author fzq
 * @DateTime 2019年7月3日 下午4:14:32
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") DefaultSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //拦截器
        Map<String, String> filterChain = new LinkedHashMap<String, String>();
        // 配置不会被拦截的链接 顺序判断
        //swagger
        filterChain.put("/swagger-ui.html", "anon");
        filterChain.put("/swagger-resources", "anon");
        filterChain.put("/swagger-resources/configuration/security", "anon");
        filterChain.put("/swagger-resources/configuration/ui", "anon");
        filterChain.put("/v2/api-docs","anon");
        filterChain.put("/webjars/springfox-swagger-ui/**","anon");

        //activiti
        filterChain.put("/activiti-ui.html", "anon");
        filterChain.put("/model-list.html","anon");
        filterChain.put("/modeler.html", "anon");
        filterChain.put("/editor-app/**", "anon");
        filterChain.put("/diagram-viewer/**", "anon");
        filterChain.put("/templates/**","anon");
        //这个是原型创建controller中定义的接口路径，不做拦截（安全问题后面再说）
        filterChain.put("/static/**","anon");
        filterChain.put("/service/**", "anon");
        filterChain.put("/runs/**","anon");
        filterChain.put("/models/**","anon");
        filterChain.put("/prof","anon");
        filterChain.put("/editor","anon");

        //自己的接口
        filterChain.put("/","anon");
        filterChain.put("/csrf","anon");
        filterChain.put("/index","anon");
        filterChain.put("/401","anon");
        filterChain.put("/**.ico","anon");
        filterChain.put("/**.js", "anon");
        filterChain.put("**.css","anon");
        filterChain.put("/druid/**", "anon");
        filterChain.put("/user/login", "anon");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setLoginUrl("/index");

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JWTFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
        filterChain.put("/**", "jwt");
        //filterChain.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChain);
        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(MyRealm myRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm);
        /*
        * \* 关闭shiro自带的session，详情见文档
        * * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
        * */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    /**
     *
     * 下面的代码是添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启shiro注解功能
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager
                                                                                           securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
