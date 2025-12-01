package com.ARFastCheck.ARFastCheck.controller;

import com.ARFastCheck.ARFastCheck.model.Objeto;
import com.ARFastCheck.ARFastCheck.service.ObjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/objetos")
public class ObjetoViewController {

    @Autowired
    private ObjetoService objetoService;

    // Listar objetos
    @GetMapping
    public String listarObjetos(Model model) {
        model.addAttribute("objetos", objetoService.listarObjetos());
        model.addAttribute("objeto", new Objeto());
        return "objetos";
    }

    // Guardar objeto
    @PostMapping("/guardar")
    public String guardarObjeto(@ModelAttribute Objeto objeto, RedirectAttributes redirectAttrs) {
        objetoService.guardarObjeto(objeto);
        redirectAttrs.addFlashAttribute("mensaje", "Objeto guardado correctamente.");
        redirectAttrs.addFlashAttribute("clase", "success");
        return "redirect:/objetos";
    }

    // Eliminar objeto
    @PostMapping("/eliminar/{id}")
    public String eliminarObjeto(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        if (objetoService.eliminarObjeto(id)) {
            redirectAttrs.addFlashAttribute("mensaje", "Objeto eliminado correctamente.");
            redirectAttrs.addFlashAttribute("clase", "success");
        } else {
            redirectAttrs.addFlashAttribute("mensaje", "No se encontró el objeto.");
            redirectAttrs.addFlashAttribute("clase", "danger");
        }
        return "redirect:/objetos";
    }
}
