package net.stackoverflow.blog.config;

import net.stackoverflow.blog.web.interceptor.VisitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private VisitInterceptor visitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePaths = new ArrayList<>();
        excludePaths.add("/api/**");
        excludePaths.add("/static/**");
        excludePaths.add("/upload/**");
        excludePaths.add("/error/**");
        excludePaths.add("/admin/**");
        excludePaths.add("/favicon.ico");
        registry.addInterceptor(visitInterceptor).addPathPatterns("/**").excludePathPatterns(excludePaths);
    }
}
