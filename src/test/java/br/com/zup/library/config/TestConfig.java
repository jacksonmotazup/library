package br.com.zup.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Configuração necessária, pois a partir do Spring Boot v2.2.x o encoding padrão não é mais definido como UTF-8,
 * mas simplesmente assumido que quando não informado na response o client deve interpretar a response como UTF-8.
 * Essa mudança parece não impactar em produção (os browsers parecem lidar bem com isso), mas impacta nos
 * testes de integração, diretamente no MockMvc.
 *
 * https://stackoverflow.com/questions/58525387/mockmvc-no-longer-handles-utf-8-characters-with-spring-boot-2-2-0-release
 */
@Configuration
public class TestConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
    }
}