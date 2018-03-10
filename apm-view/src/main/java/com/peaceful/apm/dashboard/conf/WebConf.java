package com.peaceful.apm.dashboard.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by wangjun on 16/9/15.
 */
@Configuration("webapp_config")
public class WebConf extends WebMvcConfigurerAdapter {

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SpringMvcRequest()).addPathPatterns(new String[]{"/**"});
        super.addInterceptors(registry);
    }

}
