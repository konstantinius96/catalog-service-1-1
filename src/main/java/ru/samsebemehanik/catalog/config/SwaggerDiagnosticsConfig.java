package ru.samsebemehanik.catalog.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class SwaggerDiagnosticsConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        List<String> swaggerVersions = detectSwaggerUiVersions();

        registry.addResourceHandler("/swagger-ui/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/")
            .resourceChain(false)
            .addResolver(new VersionedSwaggerUiResolver(swaggerVersions));
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
