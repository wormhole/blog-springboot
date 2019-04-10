package net.stackoverflow.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

/**
 * 校验配置类
 *
 * @author 凉衫薄
 */
@Configuration
public class ValidationConfiguration {

    @Bean
    public ValidatorFactory validatorFactory() {
        return Validation.buildDefaultValidatorFactory();
    }
}
