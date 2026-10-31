package com.aresexpress.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Configuración de CORS
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }

    // Mapeo blindado para servir los archivos locales
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. Logos de los comercios (el que ya tenías)
        Path uploadDirLogos = Paths.get("uploads/logos").toAbsolutePath();
        String uploadUriLogos = uploadDirLogos.toUri().toString();

        registry.addResourceHandler("/logos/**")
                .addResourceLocations(uploadUriLogos);

        // 2. Imágenes de los platillos del menú (NUEVO)
        Path uploadDirMenus = Paths.get("uploads/menus").toAbsolutePath();
        String uploadUriMenus = uploadDirMenus.toUri().toString();

        registry.addResourceHandler("/api/menus/images/**")
                .addResourceLocations(uploadUriMenus);
    }
}