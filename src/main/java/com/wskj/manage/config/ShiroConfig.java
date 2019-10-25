package com.wskj.manage.config;

import com.wskj.manage.common.config.Global;
import com.wskj.manage.system.security.CORSAuthenticationFilter;
import com.wskj.manage.system.security.CustomFormAuthenticationFilter;
import com.wskj.manage.system.security.ShiroSession;
import com.wskj.manage.system.security.SystemRealm;
import com.wskj.manage.system.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: xuchang
 * @date: 2019/10/18
 */
@Configuration
@Slf4j
public class ShiroConfig {

    private String tokenName = Global.getConfig("shiro.tokenName");

    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager")SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl("/authc/login");
        shiroFilter.setUnauthorizedUrl("/error?code=403");
        // 配置拦截器
        Map<String, String> filterMap = new LinkedHashMap<>(3);
        filterMap.put("/swagger*/**","anon");
        filterMap.put("/webjars/**","anon");
        filterMap.put("/v2/**","anon");
        filterMap.put("/authc/login","authc");
        filterMap.put("/error","anon");
        filterMap.put("/logout","logout");
        filterMap.put("/**","authc");
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        // 自定义拦截器
        Map<String, Filter> customFilter = new LinkedHashMap<>(1);
        customFilter.put("cors", new CORSAuthenticationFilter());
        CustomFormAuthenticationFilter customFormAuthenticationFilter = new CustomFormAuthenticationFilter();
        customFormAuthenticationFilter.setTokenName(tokenName);
        customFilter.put("authc", customFormAuthenticationFilter);
        shiroFilter.setFilters(customFilter);
        return shiroFilter;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(@Qualifier("shiroCacheManager") EhCacheManager shiroCacheManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());
        securityManager.setSessionManager(sessionManager());
        securityManager.setCacheManager(shiroCacheManager);
        return securityManager;
    }

    @Bean("shiroCacheManager")
    public EhCacheManager shiroCacheManager(@Qualifier("ehcacheManager") CacheManager cacheManager) {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(cacheManager);
        return ehCacheManager;
    }

    @Bean
    public Realm customRealm(){
        SystemRealm systemRealm = new SystemRealm();
        // 设定密码校验的Hash算法与迭代次数
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SystemService.HASH_ALGORITHM);
        matcher.setHashIterations(SystemService.HASH_INTERATIONS);
        systemRealm.setCredentialsMatcher(matcher);
        return systemRealm;
    }

    @Bean
    public SessionManager sessionManager() {
        ShiroSession shiroSession = new ShiroSession();
        shiroSession.setTokenName(tokenName);
        // 多tomcat部署,使用shiro-redis开源插件管理session,或者nginx
        shiroSession.setSessionDAO(sessionDao());
        shiroSession.setSessionIdCookie(new SimpleCookie(tokenName));
        shiroSession.setSessionIdCookieEnabled(true);
        return shiroSession;
    }

    @Bean("sessionDao")
    public SessionDAO sessionDao() {
        return new EnterpriseCacheSessionDAO();
    }

    /**
     * 开启AOP注解
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager")SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }


}
