package ru.samsebemehanik.catalog.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerUiController {

    @GetMapping("/swagger-ui/index.html")
    public String swaggerUi() {
        return "redirect:/webjars/swagger-ui/index.html?url=/openapi.yaml";
    }
}
