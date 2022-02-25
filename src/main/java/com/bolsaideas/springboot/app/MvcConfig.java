package com.bolsaideas.springboot.app;

import java.util.Locale;

import org.springframework.context.annotation.Bean;

// import java.nio.file.Paths;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Con este método agregamos recursos externos al proyecto
     */
    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // WebMvcConfigurer.super.addResourceHandlers(registry);
    // // El toUri agrega el file:
    // String resourcePath =
    // Paths.get("uploads").toAbsolutePath().toUri().toString();
    // log.info(resourcePath);
    // // Directorio url / directorio físico
    // //
    // registry.addResourceHandler("/uploads/**").addResourceLocations("file:/C:/temp/uploads/");
    // registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath);
    // }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error_403").setViewName("error_403");
    }

    /**
     * Internacionalización de la app
     * 
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("es", "ES"));
        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setParamName("lang");
        return localeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

}
