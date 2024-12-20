package com.guarderia.GuarderiaControl.controller;


import com.guarderia.GuarderiaControl.dto.PadreCreateDTO;
import com.guarderia.GuarderiaControl.dto.PadreInfoDTO;
import com.guarderia.GuarderiaControl.service.PadreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/padres")
public class PadreController {

    @Autowired
    private PadreService padreService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public PadreInfoDTO getPadre(@PathVariable Long id) {
        return new PadreInfoDTO(padreService.getPadre(id));
    }

    // Consultar todos los padres
    @GetMapping
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public List<PadreInfoDTO> getAllPadres() {
        return padreService.getAllPadres().stream()
                .map(PadreInfoDTO::new)
                .collect(Collectors.toList());
    }

    // Crear un nuevo padre
    @PostMapping
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public PadreInfoDTO createPadre(@Valid @RequestBody PadreCreateDTO padreCreateDTO) {
        return new PadreInfoDTO(padreService.createPadre(padreCreateDTO));
    }

    // Modificar un padre
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public PadreInfoDTO updatePadre(@PathVariable Long id, @Valid @RequestBody PadreCreateDTO padreCreateDTO) {
        return new PadreInfoDTO(padreService.updatePadre(id, padreCreateDTO));
    }


    // Eliminar un padre
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void deletePadre(@PathVariable Long id) {
        padreService.deletePadre(id);
    }
}

