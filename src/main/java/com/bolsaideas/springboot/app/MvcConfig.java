// package com.bolsaideas.springboot.app;

// import java.nio.file.Paths;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.context.annotation.Configuration;
// import
// org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class MvcConfig implements WebMvcConfigurer {

// private final Logger log = LoggerFactory.getLogger(getClass());

// /**
// * Con este método agregamos recursos externos al proyecto
// */
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

// }
