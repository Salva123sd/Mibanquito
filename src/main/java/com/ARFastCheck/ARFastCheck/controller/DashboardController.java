package com.ARFastCheck.ARFastCheck.controller;

import com.ARFastCheck.ARFastCheck.model.Prestamo;
import com.ARFastCheck.ARFastCheck.repository.PersonaRepository;
import com.ARFastCheck.ARFastCheck.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @GetMapping
    public String verDashboard(Model model) {

        // 1) Total de clientes
        long totalClientes = personaRepository.count();

        // 2) Obtener todos los préstamos
        List<Prestamo> prestamos = prestamoRepository.findAll();

        // 3) Contar por estado usando streams
        long prestamosActivos = prestamos.stream()
                .filter(p -> p.getEstado() != null &&
                        (p.getEstado().equalsIgnoreCase("ACTIVO")
                                || p.getEstado().equalsIgnoreCase("VENCIDO")))
                .count();

        long prestamosPagados = prestamos.stream()
                .filter(p -> p.getEstado() != null &&
                        (p.getEstado().equalsIgnoreCase("DEVUELTO")
                                || p.getEstado().equalsIgnoreCase("ENTREGADO ATRASADO")))
                .count();

        // 4) Totales de dinero (por ahora en 0 hasta que tengas campos de monto)
        double totalPrestado = 0.0;
        double pendienteCobro = 0.0;

        // 5) Enviar datos a la vista
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("prestamosActivos", prestamosActivos);
        model.addAttribute("prestamosPagados", prestamosPagados);
        model.addAttribute("totalPrestado", totalPrestado);
        model.addAttribute("pendienteCobro", pendienteCobro);

        return "dashboard";
    }
}
