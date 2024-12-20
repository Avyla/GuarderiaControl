package com.guarderia.GuarderiaControl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // "login.html" debe estar en src/main/resources/templates
    }
}


