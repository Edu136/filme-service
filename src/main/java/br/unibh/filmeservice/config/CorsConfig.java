package br.unibh.filmeservice.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // ⚠️ ATENÇÃO: Isso é ótimo para dev, mas inseguro em produção
                        .allowedOrigins("*")
                        // Permite os métodos que seu app usa
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // Permite todos os headers
                        .allowedHeaders("*");
            }
        };
    }
}