package com.egyetem.szakdolgozat;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@EnableWebMvc
@Configuration
public class MvcConfig implements WebMvcConfigurer {


    public MvcConfig() {
        super();
    }

    @Override
    public void addResourceHandlers(
        ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/static/**")
            .addResourceLocations("/build/static/");
        registry.addResourceHandler("/*.js")
            .addResourceLocations("/build/");
        registry.addResourceHandler("/*.json")
            .addResourceLocations("/build/");
        registry.addResourceHandler("/*.ico")
            .addResourceLocations("/build/");
        registry.addResourceHandler("/index.html")
            .addResourceLocations("/build/index.html")
            .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));

        registry.setOrder(1);
    }
}