package com.wskj.manage.config;

import net.sf.ehcache.CacheManager;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @author: xuchang
 * @date: 2019/10/18
 */
@Configuration
public class EhCacheConfig {

    @Bean("ehCacheManagerFactoryBean")
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new ClassPathResource("ehcache/ehcache-local.xml"));
        return factoryBean;
    }

    @Bean("ehcacheManager")
    public CacheManager ehcacheManager(EhCacheManagerFactoryBean ehCacheManagerFactoryBean) {
        return ehCacheManagerFactoryBean.getObject();
    }

}
