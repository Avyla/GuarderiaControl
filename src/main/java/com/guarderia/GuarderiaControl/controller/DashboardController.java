package com.guarderia.GuarderiaControl.controller;

import com.guarderia.GuarderiaControl.model.Nino;
import com.guarderia.GuarderiaControl.service.NinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private NinoService ninoService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Nino> ninos = ninoService.getAllNinos();
        model.addAttribute("ninos", ninos);
        return "dashboard";
    }
}
