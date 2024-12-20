package com.guarderia.GuarderiaControl.controller;

import com.guarderia.GuarderiaControl.model.Pago;
import com.guarderia.GuarderiaControl.repository.PagoRepository;
import com.guarderia.GuarderiaControl.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/pagos")
public class PagoWebController {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PagoService pagoService;

    @GetMapping("/pagar/{id}")
    public String showPagoForm(@PathVariable Long id, Model model) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        model.addAttribute("pago", pago);
        return "pagar-pago";
    }

    @PostMapping("/pagar/{id}")
    public String realizarPago(@PathVariable Long id, @RequestParam("cantidad") Double cantidad, Model model) {
        try {
            pagoService.realizarPago(id, cantidad);
            return "redirect:/dashboard";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            Pago pago = pagoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
            model.addAttribute("pago", pago);
            return "pagar-pago";
        }
    }

    @GetMapping("/pagos/edit/{id}")
    public String showEditPagoForm(@PathVariable Long id, Model model) {
        Pago pago = pagoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        model.addAttribute("pago", pago);
        return "edit-pago";
    }

    @PostMapping("/pagos/edit/{id}")
    public String editPago(@PathVariable Long id,
                           @RequestParam("monto") Double monto,
                           @RequestParam("fechaCorte") String fechaCorteStr,
                           Model model) {
        Pago pago = pagoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        // Convertir fechaCorteStr a LocalDate
        LocalDate fechaCorte = LocalDate.parse(fechaCorteStr);
        pago.setFechaCorte(fechaCorte);
        pago.setMonto(monto);
        // Si quieres validar algo aquí, hazlo
        pagoRepository.save(pago);
        return "redirect:/dashboard";
    }

    @PostMapping("/pagos/delete/{id}")
    public String deletePago(@PathVariable Long id) {
        pagoRepository.deleteById(id);
        return "redirect:/dashboard";
    }


}

