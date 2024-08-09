package org.astrologist.midea.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /upload/** 요청을 C:/upload/ 디렉토리로 매핑합니다.
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:C:/upload/");
    }
}
