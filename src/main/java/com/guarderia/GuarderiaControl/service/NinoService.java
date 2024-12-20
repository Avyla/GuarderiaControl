package com.guarderia.GuarderiaControl.service;

import com.guarderia.GuarderiaControl.dto.NinoCreateDTO;
import com.guarderia.GuarderiaControl.exception.NinoNotFoundException;
import com.guarderia.GuarderiaControl.exception.PadreNotFoundException;
import com.guarderia.GuarderiaControl.model.Nino;
import com.guarderia.GuarderiaControl.model.Padre;
import com.guarderia.GuarderiaControl.repository.NinoRepository;
import com.guarderia.GuarderiaControl.repository.PadreRepository;
import com.guarderia.GuarderiaControl.util.TipoCuidado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NinoService {

    private static final Logger logger = LoggerFactory.getLogger(NinoService.class);

    @Autowired
    private NinoRepository ninoRepository;

    @Autowired
    private PadreRepository padreRepository;

    @Autowired
    private PagoService pagoService;

    public Nino getNino(Long id) {
        logger.info("Fetching Niño with ID: {}", id);
        return ninoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Niño with ID {} not found", id);
                    return new NinoNotFoundException("Niño no encontrado");
                });
    }

    public List<Nino> getAllNinos() {
        logger.info("Fetching all Niños");
        return ninoRepository.findAll();
    }

    public Nino createNino(NinoCreateDTO ninoCreateDTO) {
        logger.info("Creating Niño with data: {}", ninoCreateDTO);

        if (ninoCreateDTO.getPadreId() == null) {
            throw new RuntimeException("Debes seleccionar un padre para el niño");
        }

        Padre padre = padreRepository.findById(ninoCreateDTO.getPadreId())
                .orElseThrow(() -> {
                    logger.error("Padre with ID {} not found", ninoCreateDTO.getPadreId());
                    return new PadreNotFoundException("El padre con ID " + ninoCreateDTO.getPadreId() + " no existe");
                });

        // Chequear duplicados de Niño si es necesario (opcional)
        // Por ejemplo, evitar que se creen dos niños con mismo nombre y fecha de nacimiento.
        boolean exists = ninoRepository.findAll().stream().anyMatch(n ->
                n.getNombreCompleto().equalsIgnoreCase(ninoCreateDTO.getNombreCompleto())
                        && n.getFechaNacimiento().equals(ninoCreateDTO.getFechaNacimiento()));
        if (exists) {
            throw new RuntimeException("Ya existe un niño con ese nombre y fecha de nacimiento");
        }

        Nino nino = new Nino();
        nino.setNombreCompleto(ninoCreateDTO.getNombreCompleto());
        nino.setFechaNacimiento(ninoCreateDTO.getFechaNacimiento());
        nino.setTipoCuidado(ninoCreateDTO.getTipoCuidado());
        nino.setPadre(padre);

        logger.info("Saving Niño in repository...");
        nino = ninoRepository.save(nino);

        try {
            logger.info("Creating initial payment for Niño with ID: {}", nino.getId());
            pagoService.crearPagoInicial(nino);
        } catch (Exception e) {
            logger.error("Error creating initial payment for Niño with ID: {}", nino.getId(), e);
            throw e;
        }

        logger.info("Niño created successfully with ID: {}", nino.getId());
        return nino;
    }

    public Nino updateNino(Long id, NinoCreateDTO ninoCreateDTO) {
        logger.info("Updating Niño with ID: {}", id);

        Nino nino = ninoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Niño with ID {} not found", id);
                    return new NinoNotFoundException("El niño con ID " + id + " no existe");
                });

        Padre padre = padreRepository.findById(ninoCreateDTO.getPadreId())
                .orElseThrow(() -> {
                    logger.error("Padre with ID {} not found", ninoCreateDTO.getPadreId());
                    return new PadreNotFoundException("El padre con ID " + ninoCreateDTO.getPadreId() + " no existe");
                });

        nino.setNombreCompleto(ninoCreateDTO.getNombreCompleto());
        nino.setFechaNacimiento(ninoCreateDTO.getFechaNacimiento());
        nino.setTipoCuidado(ninoCreateDTO.getTipoCuidado());
        nino.setPadre(padre);

        logger.info("Saving updated Niño in repository...");
        nino = ninoRepository.save(nino);

        logger.info("Niño updated successfully with ID: {}", nino.getId());
        return nino;
    }

    public void deleteNino(Long id) {
        logger.info("Deleting Niño with ID: {}", id);
        ninoRepository.deleteById(id);
        logger.info("Niño with ID {} deleted successfully", id);
    }
}
