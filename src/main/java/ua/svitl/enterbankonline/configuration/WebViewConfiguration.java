package ua.svitl.enterbankonline.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = "ua.svitl.enterbankonline")
public class WebViewConfiguration implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/resources/static/bootstrap/**")
                    .addResourceLocations("classpath:/resources/static/bootstrap/");
            registry.addResourceHandler("/resources/static/js/**")
                    .addResourceLocations("classpath:/resources/static/js/");

        }

}
