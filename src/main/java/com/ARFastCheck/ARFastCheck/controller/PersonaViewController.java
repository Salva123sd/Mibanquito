package com.ARFastCheck.ARFastCheck.controller;

import com.ARFastCheck.ARFastCheck.model.Persona;
import com.ARFastCheck.ARFastCheck.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/personas")
public class PersonaViewController {

    @Autowired
    private PersonaService personaService;

    // Listar personas
    @GetMapping
    public String listarPersonas(Model model) {
        model.addAttribute("personas", personaService.listarPersonas());
        model.addAttribute("persona", new Persona()); // objeto vacío para el formulario
        return "personas";
    }

    // Guardar persona
    @PostMapping("/guardar")
    public String guardarPersona(@ModelAttribute Persona persona,
                                 RedirectAttributes redirectAttrs) {
        String resultado = personaService.guardarPersona(persona);

        switch (resultado) {
            case "success" -> {
                redirectAttrs.addFlashAttribute("mensaje", "Persona registrada con validación RENIEC.");
                redirectAttrs.addFlashAttribute("clase", "success");
            }
            case "invalid" -> {
                redirectAttrs.addFlashAttribute("mensaje", "DNI no válido o no encontrado en RENIEC.");
                redirectAttrs.addFlashAttribute("clase", "danger");
            }
            default -> {
                redirectAttrs.addFlashAttribute("mensaje", "Error al guardar la persona.");
                redirectAttrs.addFlashAttribute("clase", "danger");
            }
        }
        return "redirect:/personas";
    }

    // Eliminar persona
    @PostMapping("/eliminar/{dni}")
    public String eliminarPersona(@PathVariable String dni,
                                  RedirectAttributes redirectAttrs) {
        String resultado = personaService.eliminarPersona(dni);

        switch (resultado) {
            case "success" -> {
                redirectAttrs.addFlashAttribute("mensaje", "Persona eliminada correctamente.");
                redirectAttrs.addFlashAttribute("clase", "success");
            }
            case "tienePrestamos" -> {
                redirectAttrs.addFlashAttribute("mensaje", "No se puede eliminar: la persona tiene préstamos registrados.");
                redirectAttrs.addFlashAttribute("clase", "warning");
            }
            case "notFound" -> {
                redirectAttrs.addFlashAttribute("mensaje", "No se encontró la persona.");
                redirectAttrs.addFlashAttribute("clase", "danger");
            }
            default -> {
                redirectAttrs.addFlashAttribute("mensaje", "Error al eliminar la persona.");
                redirectAttrs.addFlashAttribute("clase", "danger");
            }
        }
        return "redirect:/personas";
    }
}
