package net.stackoverflow.blog.config;

import net.stackoverflow.blog.web.interceptor.VisitInterceptor;
import net.stackoverflow.blog.web.listener.InitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private VisitInterceptor visitInterceptor;
    @Value("${server.upload.path}")
    private String path;

    /**
     * 静态文件路径映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/static/**")) {
            registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        }
        if (!registry.hasMappingForPattern("/upload/**")) {
            registry.addResourceHandler("/upload/**").addResourceLocations("file:" + path);
        }
    }

    /**
     * 配置拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePaths = new ArrayList<>();
        excludePaths.add("/static/**");
        excludePaths.add("/upload/**");
        excludePaths.add("/error/**");
        excludePaths.add("/admin/**");
        excludePaths.add("/vcode");
        excludePaths.add("/comment");
        excludePaths.add("/like");
        excludePaths.add("/favicon.ico");
        registry.addInterceptor(visitInterceptor).addPathPatterns("/**").excludePathPatterns(excludePaths);
    }

    /**
     * 配置监听器
     *
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean addListener() {
        ServletListenerRegistrationBean lrb = new ServletListenerRegistrationBean();
        lrb.setListener(new InitListener());
        return lrb;
    }
}
