package com.egyetem.szakdolgozat;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
            .addResourceLocations("/build/index.html");

        registry.setOrder(1);
    }
}