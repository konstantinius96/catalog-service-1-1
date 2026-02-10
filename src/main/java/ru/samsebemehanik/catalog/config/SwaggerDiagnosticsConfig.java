package ru.samsebemehanik.catalog.config;

import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerDiagnosticsConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(SwaggerDiagnosticsConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Fallback mapping in case springdoc swagger-ui resource handler is not auto-registered.
        registry.addResourceHandler("/swagger-ui/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/");
    }

    @Bean
    public ApplicationRunner swaggerDiagnosticsRunner(
        ApplicationContext applicationContext,
        ObjectProvider<Map<String, HandlerMapping>> handlerMappingsProvider
    ) {
        return args -> {
            boolean swaggerUiClassPresent = isClassPresent("org.springdoc.webmvc.ui.SwaggerConfig");
            boolean openApiClassPresent = isClassPresent("org.springdoc.webmvc.api.OpenApiWebMvcResource");

            boolean swaggerUiWebjarIndexPresent =
                applicationContext.getResource("classpath:/META-INF/resources/webjars/swagger-ui/index.html").exists();

            log.info("[swagger-diagnostics] springdoc SwaggerConfig class present: {}", swaggerUiClassPresent);
            log.info("[swagger-diagnostics] springdoc OpenApiWebMvcResource class present: {}", openApiClassPresent);
            log.info("[swagger-diagnostics] webjar resource '/META-INF/resources/webjars/swagger-ui/index.html' present: {}",
                swaggerUiWebjarIndexPresent);

            Map<String, HandlerMapping> handlerMappings = handlerMappingsProvider.getIfAvailable();
            if (handlerMappings != null) {
                String mappings = handlerMappings.entrySet().stream()
                    .map(entry -> entry.getKey() + " -> " + entry.getValue().getClass().getName())
                    .sorted()
                    .collect(Collectors.joining(", "));

                log.info("[swagger-diagnostics] HandlerMapping beans: {}", mappings);
            } else {
                log.warn("[swagger-diagnostics] No HandlerMapping beans found in context.");
            }
        };
    }

    private boolean isClassPresent(String fqcn) {
        try {
            Class.forName(fqcn);
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }
}
