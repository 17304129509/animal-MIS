package com.campus.animal.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {
    @Value("${app.admin-port}")
    private int adminPort;

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        if (request.getLocalPort() == adminPort) {
            return "forward:/console/index.html";
        }
        return "forward:/index.html";
    }

    @GetMapping("/bg.png")
    public String adminBackground() {
        return "forward:/console/bg.png";
    }

    @GetMapping({"/login", "/register", "/home", "/animals", "/animals/new",
            "/animals/{id:[0-9]+}", "/feed-points", "/feed-points/{id:[0-9]+}/checkin",
            "/rank", "/profile", "/admin", "/rescue", "/rescue/new", "/rescue/{id:[0-9]+}",
            "/adoption", "/adoption/new", "/adoption/{id:[0-9]+}"})
    public String userPage() {
        return "forward:/index.html";
    }
}
