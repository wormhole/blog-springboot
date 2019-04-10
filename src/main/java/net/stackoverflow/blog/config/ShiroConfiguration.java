package net.stackoverflow.blog.config;

import net.stackoverflow.blog.shiro.cache.ShiroRedisCacheManager;
import net.stackoverflow.blog.shiro.realm.JdbcRealm;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro配置类
 *
 * @author 凉衫薄
 */
@Configuration
public class ShiroConfiguration {

    /**
     * 自定义凭证匹配器
     *
     * @return credentialsMatcher
     */
    @Bean(name = "credentialsMatcher")
    public CredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(1);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    /**
     * 自定义realm
     *
     * @return jdbcRealm
     */
    @Bean(name = "jdbcRealm")
    public JdbcRealm jdbcRealm(CredentialsMatcher credentialsMatcher) {
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setCredentialsMatcher(credentialsMatcher);
        jdbcRealm.setCachingEnabled(true);
        jdbcRealm.setAuthenticationCachingEnabled(true);
        jdbcRealm.setAuthenticationCacheName("authentication");
        jdbcRealm.setAuthorizationCachingEnabled(true);
        jdbcRealm.setAuthorizationCacheName("authorization");
        return jdbcRealm;
    }

    /**
     * Shiro整合redis,缓存管理器
     *
     * @return cacheManager
     */
    @Bean(name = "shiroRedisCacheManager")
    public CacheManager shiroRedisCacheManager(RedisTemplate redisTemplate, RedisCacheManager redisCacheManager) {
        ShiroRedisCacheManager shiroRedisCacheManager = new ShiroRedisCacheManager();
        shiroRedisCacheManager.setRedisCacheManager(redisCacheManager);
        shiroRedisCacheManager.setRedisTemplate(redisTemplate);
        return shiroRedisCacheManager;
    }

    /**
     * 会话Id生成器
     *
     * @return sessionIdGenerator
     */
    @Bean(name = "sessionIdGenerator")
    public SessionIdGenerator sessionIdGenerator() {
        SessionIdGenerator sessionIdGenerator = new JavaUuidSessionIdGenerator();
        return sessionIdGenerator;
    }

    /**
     * 会话持久化DAO,持久化到缓存
     *
     * @param sessionIdGenerator 会话Id生成器
     * @return sessionDAO
     */
    @Bean(name = "sessionDAO")
    public SessionDAO sessionDAO(SessionIdGenerator sessionIdGenerator) {
        EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
        sessionDAO.setActiveSessionsCacheName("session");
        sessionDAO.setSessionIdGenerator(sessionIdGenerator);
        return sessionDAO;
    }

    /**
     * 会话cookie模板
     *
     * @return simpleCookie
     */
    @Bean(name = "sessionIdCookie")
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        simpleCookie.setMaxAge(-1);
        simpleCookie.setHttpOnly(true);
        return simpleCookie;
    }

    /**
     * 会话管理器
     *
     * @param sessionDAO 会话持久化DAO
     * @param cookie     会话cookie模板
     * @return sessionManager
     */
    @Bean(name = "sessionManager")
    public SessionManager sessionManager(SessionDAO sessionDAO, @Qualifier(value = "sessionIdCookie") SimpleCookie cookie) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);

        ExecutorServiceSessionValidationScheduler scheduler = new ExecutorServiceSessionValidationScheduler();
        scheduler.setSessionManager(sessionManager);
        scheduler.setInterval(1800000);

        sessionManager.setSessionValidationScheduler(scheduler);
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(cookie);
        return sessionManager;
    }

    /**
     * rememberMeCookie模板
     *
     * @return rememberMeCookie
     */
    @Bean(name = "rememberMeCookie")
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    /**
     * rememberMe管理器
     *
     * @param cookie rememberMeCookie模板
     * @return rememberMeManager
     */
    @Bean(name = "rememberMeManager")
    public RememberMeManager rememberMeManager(@Qualifier(value = "rememberMeCookie") SimpleCookie cookie) {
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCookie(cookie);
        rememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return rememberMeManager;
    }

    /**
     * 安全管理器
     *
     * @param jdbcRealm         自定义JdbcRealm
     * @param cacheManager      缓存管理器
     * @param sessionManager    会话管理器
     * @param rememberMeManager 记住我管理器
     * @return securityManager
     */
    @Bean(name = "securityManager")
    public SecurityManager securityManager(JdbcRealm jdbcRealm, CacheManager cacheManager, SessionManager sessionManager, RememberMeManager rememberMeManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(jdbcRealm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(cacheManager);
        securityManager.setRememberMeManager(rememberMeManager);
        return securityManager;
    }

    /**
     * shiro的web过滤器
     *
     * @param securityManager 安全管理器
     * @return shiroFilter
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl("/login");
        shiroFilter.setSuccessUrl("/");
        shiroFilter.setUnauthorizedUrl("/unauthorized");

        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        formAuthenticationFilter.setUsernameParam("email");
        formAuthenticationFilter.setPasswordParam("password");
        formAuthenticationFilter.setLoginUrl("/login");
        formAuthenticationFilter.setSuccessUrl("/");
        formAuthenticationFilter.setFailureKeyAttribute("shiroLoginFailure");
        formAuthenticationFilter.setRememberMeParam("rememberMe");

        UserFilter userFilter = new UserFilter();
        userFilter.setLoginUrl("/login");

        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("authc", formAuthenticationFilter);
        filterMap.put("user", userFilter);
        shiroFilter.setFilters(filterMap);

        Map<String, String> filterChainMap = new LinkedHashMap<>();
        filterChainMap.put("/logout", "logout");
        filterChainMap.put("/login", "authc");
        filterChainMap.put("/actuator/**", "anon");
        filterChainMap.put("/static/**", "anon");
        filterChainMap.put("/upload/**", "anon");
        filterChainMap.put("/favicon.ico", "anon");
        filterChainMap.put("/robots.txt", "anon");
        filterChainMap.put("/admin/**", "user");
        shiroFilter.setFilterChainDefinitionMap(filterChainMap);

        return shiroFilter;
    }

    /**
     * 相当于在web.xml加入shiroFilter
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    /**
     * 开启shiro注解
     *
     * @param securityManager 安全管理器
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * shiro生命周期处理器
     *
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        LifecycleBeanPostProcessor lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor();
        return lifecycleBeanPostProcessor;
    }
}
