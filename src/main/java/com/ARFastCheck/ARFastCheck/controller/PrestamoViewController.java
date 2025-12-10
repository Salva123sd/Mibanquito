package com.ARFastCheck.ARFastCheck.controller;

import com.ARFastCheck.ARFastCheck.model.Prestamo;
import com.ARFastCheck.ARFastCheck.model.Empeno;
import com.ARFastCheck.ARFastCheck.model.DocumentoPrestamo;
import com.ARFastCheck.ARFastCheck.repository.PersonaRepository;
import com.ARFastCheck.ARFastCheck.repository.ObjetoRepository;
import com.ARFastCheck.ARFastCheck.service.PrestamoService;
import com.ARFastCheck.ARFastCheck.service.EmpenoService;
import com.ARFastCheck.ARFastCheck.service.DocumentoPrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/prestamos")
public class PrestamoViewController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ObjetoRepository objetoRepository;

    @Autowired
    private EmpenoService empenoService;

    @Autowired
    private DocumentoPrestamoService documentoPrestamoService;

    // Listar préstamos
    @GetMapping
    public String listarPrestamos(Model model) {
        model.addAttribute("prestamos", prestamoService.listarPrestamos());
        model.addAttribute("personas", personaRepository.findAll());
        model.addAttribute("objetos", objetoRepository.findAll());
        model.addAttribute("prestamo", new Prestamo());
        return "prestamos";
    }

    // Registrar préstamo
    @PostMapping("/registrar")
    public String registrarPrestamo(@RequestParam String dni,
            @RequestParam Long objetoId,
            @RequestParam String fechaLimite,
            @RequestParam(required = false) Double interesPorcentaje,
            RedirectAttributes redirectAttrs) {

        if (prestamoService.registrarPrestamo(dni, objetoId, fechaLimite, interesPorcentaje)) {
            redirectAttrs.addFlashAttribute("mensaje", "Préstamo registrado correctamente.");
            redirectAttrs.addFlashAttribute("clase", "success");
        } else {
            redirectAttrs.addFlashAttribute("mensaje", "No se pudo registrar el préstamo.");
            redirectAttrs.addFlashAttribute("clase", "danger");
        }
        return "redirect:/prestamos";
    }

    // Devolver préstamo
    @PostMapping("/devolver/{id}")
    public String devolverPrestamo(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        String resultado = prestamoService.devolverPrestamo(id);

        switch (resultado) {
            case "success" -> {
                redirectAttrs.addFlashAttribute("mensaje", "Préstamo devuelto a tiempo.");
                redirectAttrs.addFlashAttribute("clase", "success");
            }
            case "warning" -> {
                redirectAttrs.addFlashAttribute("mensaje", "Préstamo devuelto fuera de plazo.");
                redirectAttrs.addFlashAttribute("clase", "warning");
            }
            default -> {
                redirectAttrs.addFlashAttribute("mensaje", "No se encontró el préstamo.");
                redirectAttrs.addFlashAttribute("clase", "danger");
            }
        }
        return "redirect:/prestamos";
    }

    // 🔹 Ver detalle de un préstamo (empeños + documentos)
    @GetMapping("/{id}")
    public String verDetallePrestamo(@PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttrs) {

        Prestamo prestamo = prestamoService.obtenerPorId(id);
        if (prestamo == null) {
            redirectAttrs.addFlashAttribute("mensaje", "No se encontró el préstamo.");
            redirectAttrs.addFlashAttribute("clase", "danger");
            return "redirect:/prestamos";
        }

        model.addAttribute("prestamo", prestamo);
        model.addAttribute("empenos", empenoService.listarPorPrestamo(id));
        model.addAttribute("documentos", documentoPrestamoService.listarPorPrestamo(id));

        // Para los formularios
        Empeno nuevoEmpeno = new Empeno();
        nuevoEmpeno.setPrestamo(prestamo);

        DocumentoPrestamo nuevoDocumento = new DocumentoPrestamo();
        nuevoDocumento.setPrestamo(prestamo);

        model.addAttribute("nuevoEmpeno", nuevoEmpeno);
        model.addAttribute("nuevoDocumento", nuevoDocumento);

        return "prestamo-detalle"; // vista nueva
    }

    // 🔹 Registrar un empeño para un préstamo
    @PostMapping("/{id}/empenos")
    public String registrarEmpeno(@PathVariable Long id,
            @ModelAttribute("nuevoEmpeno") Empeno nuevoEmpeno,
            RedirectAttributes redirectAttrs) {

        Prestamo prestamo = prestamoService.obtenerPorId(id);
        if (prestamo == null) {
            redirectAttrs.addFlashAttribute("mensaje", "No se encontró el préstamo.");
            redirectAttrs.addFlashAttribute("clase", "danger");
            return "redirect:/prestamos";
        }

        // Ensure ID is null for new empeño to prevent StaleObjectStateException
        nuevoEmpeno.setId(null);
        nuevoEmpeno.setPrestamo(prestamo);
        empenoService.guardar(nuevoEmpeno);

        redirectAttrs.addFlashAttribute("mensaje", "Empeño registrado correctamente.");
        redirectAttrs.addFlashAttribute("clase", "success");
        return "redirect:/prestamos/" + id;
    }

    // 🔹 Registrar un documento para un préstamo
    @PostMapping("/{id}/documentos")
    public String registrarDocumento(@PathVariable Long id,
            @ModelAttribute("nuevoDocumento") DocumentoPrestamo nuevoDocumento,
            RedirectAttributes redirectAttrs) {

        Prestamo prestamo = prestamoService.obtenerPorId(id);
        if (prestamo == null) {
            redirectAttrs.addFlashAttribute("mensaje", "No se encontró el préstamo.");
            redirectAttrs.addFlashAttribute("clase", "danger");
            return "redirect:/prestamos";
        }

        // Ensure ID is null for new document to prevent StaleObjectStateException
        nuevoDocumento.setId(null);
        nuevoDocumento.setPrestamo(prestamo);
        documentoPrestamoService.guardar(nuevoDocumento);

        redirectAttrs.addFlashAttribute("mensaje", "Documento registrado correctamente.");
        redirectAttrs.addFlashAttribute("clase", "success");
        return "redirect:/prestamos/" + id;
    }
}
