package com.guarderia.GuarderiaControl.controller;

import com.guarderia.GuarderiaControl.dto.NinoCreateDTO;
import com.guarderia.GuarderiaControl.dto.NinoInfoDTO;
import com.guarderia.GuarderiaControl.service.NinoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ninos")
public class NinoController {

    @Autowired
    private NinoService ninoService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public NinoInfoDTO getNino(@PathVariable Long id){
        return new NinoInfoDTO(ninoService.getNino(id));
    }

    // Consultar todos los niños
    @GetMapping
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public List<NinoInfoDTO> getAllNinos() {
        return ninoService.getAllNinos().stream()
                .map(NinoInfoDTO::new)
                .collect(Collectors.toList());
    }

    // Crear un nuevo niño
    @PostMapping
    @PreAuthorize("hasAnyRole('DOCENTE', 'ADMINISTRADOR')")
    public NinoInfoDTO createNino(@Valid @RequestBody NinoCreateDTO ninoCreateDTO) {
        return new NinoInfoDTO(ninoService.createNino(ninoCreateDTO));
    }

    // Modificar un niño
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public NinoInfoDTO updateNino(@PathVariable Long id, @Valid @RequestBody NinoCreateDTO ninoCreateDTO) {
        return new NinoInfoDTO(ninoService.updateNino(id, ninoCreateDTO));
    }

    // Eliminar un niño
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void deleteNino(@PathVariable Long id) {
        ninoService.deleteNino(id);
    }
}

