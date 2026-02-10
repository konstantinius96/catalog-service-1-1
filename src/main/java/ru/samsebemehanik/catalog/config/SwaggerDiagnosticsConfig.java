package ru.samsebemehanik.catalog.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class SwaggerDiagnosticsConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(SwaggerDiagnosticsConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        List<String> swaggerVersions = detectSwaggerUiVersions();

        registry.addResourceHandler("/swagger-ui/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/")
            .resourceChain(false)
            .addResolver(new VersionedSwaggerUiResolver(swaggerVersions));
    }

    @Bean
    public ApplicationRunner swaggerDiagnosticsRunner(
        ApplicationContext applicationContext,
        Environment environment,
        ObjectProvider<Map<String, HandlerMapping>> handlerMappingsProvider
    ) {
        return args -> {
            boolean swaggerUiClassPresent = isClassPresent("org.springdoc.webmvc.ui.SwaggerConfig");
            boolean openApiClassPresent = isClassPresent("org.springdoc.webmvc.api.OpenApiWebMvcResource");

            boolean swaggerUiWebjarIndexAtRootPresent =
                applicationContext.getResource("classpath:/META-INF/resources/webjars/swagger-ui/index.html").exists();

            Resource[] versionedSwaggerIndexes = findResources("classpath*:/META-INF/resources/webjars/swagger-ui/*/index.html");

            String loggingFileName = environment.getProperty("logging.file.name", "<console-only>");
            String loggingFilePath = environment.getProperty("logging.file.path", "<not-set>");

            log.info("[swagger-diagnostics] springdoc SwaggerConfig class present: {}", swaggerUiClassPresent);
            log.info("[swagger-diagnostics] springdoc OpenApiWebMvcResource class present: {}", openApiClassPresent);
            log.info("[swagger-diagnostics] webjar resource '/META-INF/resources/webjars/swagger-ui/index.html' present: {}",
                swaggerUiWebjarIndexAtRootPresent);
            log.info("[swagger-diagnostics] versioned swagger-ui index resources found: {}",
                versionedSwaggerIndexes.length);
            for (Resource resource : versionedSwaggerIndexes) {
                log.info("[swagger-diagnostics] versioned swagger-ui index location: {}", safeDescription(resource));
            }

            log.info("[swagger-diagnostics] logs output: logging.file.name='{}', logging.file.path='{}'",
                loggingFileName, loggingFilePath);

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

    private List<String> detectSwaggerUiVersions() {
        Resource[] versionedSwaggerIndexes = findResources("classpath*:/META-INF/resources/webjars/swagger-ui/*/index.html");
        List<String> versions = new ArrayList<>();

        for (Resource resource : versionedSwaggerIndexes) {
            String description = safeDescription(resource);
            String marker = "/META-INF/resources/webjars/swagger-ui/";
            int start = description.indexOf(marker);
            if (start < 0) {
                continue;
            }
            String tail = description.substring(start + marker.length());
            int end = tail.indexOf('/');
            if (end > 0) {
                String version = tail.substring(0, end);
                if (!versions.contains(version)) {
                    versions.add(version);
                }
            }
        }

        return versions;
    }

    private Resource[] findResources(String pattern) {
        try {
            return new PathMatchingResourcePatternResolver().getResources(pattern);
        } catch (IOException ex) {
            log.warn("[swagger-diagnostics] failed to resolve resources by pattern '{}': {}", pattern, ex.getMessage());
            return new Resource[0];
        }
    }

    private String safeDescription(Resource resource) {
        try {
            return resource.getURL().toString();
        } catch (IOException ex) {
            return resource.getDescription();
        }
    }

    private boolean isClassPresent(String fqcn) {
        try {
            Class.forName(fqcn);
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    private static final class VersionedSwaggerUiResolver extends PathResourceResolver {

        private final List<String> versions;

        private VersionedSwaggerUiResolver(List<String> versions) {
            this.versions = versions;
        }

        @Override
        @Nullable
        protected Resource getResource(String resourcePath, Resource location) throws IOException {
            Resource direct = location.createRelative(resourcePath);
            if (direct.exists() && direct.isReadable()) {
                return direct;
            }

            String normalized = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
            for (String version : versions) {
                Resource candidate = location.createRelative(version + "/" + normalized);
                if (candidate.exists() && candidate.isReadable()) {
                    return candidate;
                }
            }

            return null;
        }
    }
}
