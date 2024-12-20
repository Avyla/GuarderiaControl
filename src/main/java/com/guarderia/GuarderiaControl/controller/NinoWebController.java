package com.guarderia.GuarderiaControl.controller;

import com.guarderia.GuarderiaControl.dto.NinoCreateDTO;
import com.guarderia.GuarderiaControl.model.Nino;
import com.guarderia.GuarderiaControl.service.NinoService;
import com.guarderia.GuarderiaControl.service.PadreService;
import com.guarderia.GuarderiaControl.util.TipoCuidado;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;

@Controller
public class NinoWebController {

    @Autowired
    private NinoService ninoService;

    @Autowired
    private PadreService padreService;

    @GetMapping("/ninos/create")
    public String showCreateNinoForm(Model model) {
        model.addAttribute("ninoForm", new NinoCreateDTO());
        model.addAttribute("tiposCuidado", Arrays.asList(TipoCuidado.values()));
        model.addAttribute("padres", padreService.getAllPadres());
        return "create-nino";
    }

    @PostMapping("/ninos/create")
    public String createNino(@Valid NinoCreateDTO ninoCreateDTO, Model model) {
        // Validar que padreId no sea nulo o vacío
        if (ninoCreateDTO.getPadreId() == null) {
            model.addAttribute("ninoForm", ninoCreateDTO);
            model.addAttribute("errorMessage", "Debes seleccionar un padre para el niño.");
            model.addAttribute("tiposCuidado", Arrays.asList(TipoCuidado.values()));
            model.addAttribute("padres", padreService.getAllPadres());
            return "create-nino";
        }

        try {
            Nino nino = ninoService.createNino(ninoCreateDTO);
            return "redirect:/dashboard";
        } catch (RuntimeException e) {
            // Por ejemplo, si el padre no existe u otro error
            model.addAttribute("ninoForm", ninoCreateDTO);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("tiposCuidado", Arrays.asList(TipoCuidado.values()));
            model.addAttribute("padres", padreService.getAllPadres());
            return "create-nino";
        }
    }

    @GetMapping("/ninos/edit/{id}")
    public String showEditNinoForm(@PathVariable Long id, Model model) {
        Nino nino = ninoService.getNino(id);
        NinoCreateDTO dto = new NinoCreateDTO();
        dto.setId(nino.getId());
        dto.setNombreCompleto(nino.getNombreCompleto());
        dto.setFechaNacimiento(nino.getFechaNacimiento());
        dto.setTipoCuidado(nino.getTipoCuidado());
        dto.setPadreId(nino.getPadre().getId());

        model.addAttribute("ninoForm", dto);
        model.addAttribute("tiposCuidado", Arrays.asList(TipoCuidado.values()));
        model.addAttribute("padres", padreService.getAllPadres());
        return "edit-nino";
    }

    @PostMapping("/ninos/edit/{id}")
    public String editNino(@PathVariable Long id, @Valid NinoCreateDTO ninoCreateDTO, Model model) {
        // Validación de padreId igual que en create.
        if (ninoCreateDTO.getPadreId() == null) {
            model.addAttribute("ninoForm", ninoCreateDTO);
            model.addAttribute("errorMessage", "Debes seleccionar un padre para el niño.");
            model.addAttribute("tiposCuidado", Arrays.asList(TipoCuidado.values()));
            model.addAttribute("padres", padreService.getAllPadres());
            return "edit-nino";
        }
        try {
            ninoService.updateNino(id, ninoCreateDTO);
            return "redirect:/dashboard";
        } catch (RuntimeException e) {
            model.addAttribute("ninoForm", ninoCreateDTO);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("tiposCuidado", Arrays.asList(TipoCuidado.values()));
            model.addAttribute("padres", padreService.getAllPadres());
            return "edit-nino";
        }
    }

    @PostMapping("/ninos/delete/{id}")
    public String deleteNino(@PathVariable Long id) {
        ninoService.deleteNino(id);
        return "redirect:/dashboard";
    }


}