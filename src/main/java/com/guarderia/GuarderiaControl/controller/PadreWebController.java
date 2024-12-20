package com.guarderia.GuarderiaControl.controller;

import com.guarderia.GuarderiaControl.dto.PadreCreateDTO;
import com.guarderia.GuarderiaControl.model.Padre;
import com.guarderia.GuarderiaControl.service.PadreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PadreWebController {

    @Autowired
    private PadreService padreService;

    @GetMapping("/padres/create")
    public String showCreatePadreForm(Model model) {
        model.addAttribute("padreForm", new PadreCreateDTO());
        return "create-padre";
    }

    @PostMapping("/padres/create")
    public String createPadre(@Valid PadreCreateDTO padreCreateDTO, Model model) {
        try {
            Padre padre = padreService.createPadre(padreCreateDTO);
            return "redirect:/dashboard";
        } catch (RuntimeException e) {
            // Asumimos que la excepción se lanza si el padre está duplicado
            model.addAttribute("padreForm", padreCreateDTO);
            model.addAttribute("errorMessage", e.getMessage());
            return "create-padre";
        }
    }

    @GetMapping("/padres")
    public String listPadres(Model model) {
        model.addAttribute("padres", padreService.getAllPadres());
        return "padres";
    }

    @GetMapping("/padres/edit/{id}")
    public String showEditPadreForm(@PathVariable Long id, Model model) {
        Padre padre = padreService.getPadre(id);
        PadreCreateDTO dto = new PadreCreateDTO();
        dto.setId(padre.getId());
        dto.setNombreCompleto(padre.getNombreCompleto());
        dto.setTelefono(padre.getTelefono());
        dto.setEmail(padre.getEmail());

        model.addAttribute("padreForm", dto);
        return "edit-padre";
    }

    @PostMapping("/padres/edit/{id}")
    public String editPadre(@PathVariable Long id, @Valid PadreCreateDTO padreCreateDTO, Model model) {
        try {
            padreService.updatePadre(id, padreCreateDTO);
            return "redirect:/padres";
        } catch (RuntimeException e) {
            model.addAttribute("padreForm", padreCreateDTO);
            model.addAttribute("errorMessage", e.getMessage());
            return "edit-padre";
        }
    }

    @PostMapping("/padres/delete/{id}")
    public String deletePadre(@PathVariable Long id) {
        padreService.deletePadre(id);
        return "redirect:/padres";
    }


}
