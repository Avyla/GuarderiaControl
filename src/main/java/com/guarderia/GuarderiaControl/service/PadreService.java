package com.guarderia.GuarderiaControl.service;

import com.guarderia.GuarderiaControl.dto.PadreCreateDTO;
import com.guarderia.GuarderiaControl.exception.PadreNotFoundException;
import com.guarderia.GuarderiaControl.model.Padre;
import com.guarderia.GuarderiaControl.repository.PadreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PadreService {

    @Autowired
    private PadreRepository padreRepository;

    public Padre getPadre(Long id){
        return padreRepository.findById(id)
                .orElseThrow(() -> new PadreNotFoundException("Padre no encontrado"));
    }

    public List<Padre> getAllPadres() {
        return padreRepository.findAll();
    }

    public Padre createPadre(PadreCreateDTO padreCreateDTO) {
        // Check duplicado por teléfono
        if (telefonoExiste(padreCreateDTO.getTelefono())) {
            throw new RuntimeException("Ya existe un padre con el teléfono: " + padreCreateDTO.getTelefono());
        }

        Padre padre = new Padre();
        padre.setNombreCompleto(padreCreateDTO.getNombreCompleto());
        padre.setTelefono(padreCreateDTO.getTelefono());
        padre.setEmail(padreCreateDTO.getEmail());

        return padreRepository.save(padre);
    }

    public Padre updatePadre(Long id, PadreCreateDTO padreCreateDTO) {
        Padre padre = padreRepository.findById(id)
                .orElseThrow(() -> new PadreNotFoundException("El padre con ID " + id + " no existe"));

        // Verificar duplicado al actualizar si el teléfono es diferente
        if(!padre.getTelefono().equals(padreCreateDTO.getTelefono()) && telefonoExiste(padreCreateDTO.getTelefono())){
            throw new RuntimeException("Ya existe un padre con el teléfono: " + padreCreateDTO.getTelefono());
        }

        padre.setNombreCompleto(padreCreateDTO.getNombreCompleto());
        padre.setTelefono(padreCreateDTO.getTelefono());
        padre.setEmail(padreCreateDTO.getEmail());

        return padreRepository.save(padre);
    }

    public void deletePadre(Long id) {
        padreRepository.deleteById(id);
    }

    private boolean telefonoExiste(String telefono){
        return padreRepository.findAll().stream().anyMatch(p->p.getTelefono().equals(telefono));
    }
}
