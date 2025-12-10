package com.ARFastCheck.ARFastCheck.controller;

import com.ARFastCheck.ARFastCheck.model.Persona;
import com.ARFastCheck.ARFastCheck.model.Prestamo;
import com.ARFastCheck.ARFastCheck.repository.EmpenoRepository;
import com.ARFastCheck.ARFastCheck.repository.PersonaRepository;
import com.ARFastCheck.ARFastCheck.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

        @Autowired
        private PersonaRepository personaRepository;

        @Autowired
        private PrestamoRepository prestamoRepository;

        @Autowired
        private EmpenoRepository empenoRepository;

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
                                                                || p.getEstado().equalsIgnoreCase(
                                                                                "ENTREGADO ATRASADO")))
                                .count();

                // 4) Totales de dinero desde empeños
                BigDecimal totalPrestado = empenoRepository.sumTotalPrestado();
                BigDecimal pendienteCobro = empenoRepository.sumPendienteCobro();

                // 5) Calcular clientes frecuentes (ordenados por cantidad de préstamos)
                Map<String, Long> prestamosPorDni = prestamos.stream()
                                .filter(p -> p.getPersona() != null)
                                .collect(Collectors.groupingBy(p -> p.getPersona().getDni(), Collectors.counting()));

                List<Map<String, Object>> clientesFrecuentes = new ArrayList<>();
                prestamosPorDni.entrySet().stream()
                                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                                .limit(5)
                                .forEach(entry -> {
                                        Persona persona = personaRepository.findById(entry.getKey()).orElse(null);
                                        if (persona != null) {
                                                Map<String, Object> clienteInfo = new HashMap<>();
                                                clienteInfo.put("nombres", persona.getNombres());
                                                clienteInfo.put("apellidos", persona.getApellidos());
                                                clienteInfo.put("dni", persona.getDni());
                                                clienteInfo.put("cantidadPrestamos", entry.getValue());
                                                clientesFrecuentes.add(clienteInfo);
                                        }
                                });

                // 6) Identificar clientes morosos (tienen préstamos VENCIDOS o ENTREGADO
                // ATRASADO)
                Map<String, List<Prestamo>> prestamosMorososPorDni = prestamos.stream()
                                .filter(p -> p.getPersona() != null && p.getEstado() != null &&
                                                (p.getEstado().equalsIgnoreCase("VENCIDO") ||
                                                                p.getEstado().equalsIgnoreCase("ENTREGADO ATRASADO")))
                                .collect(Collectors.groupingBy(p -> p.getPersona().getDni()));

                List<Map<String, Object>> clientesMorosos = new ArrayList<>();
                for (Map.Entry<String, List<Prestamo>> entry : prestamosMorososPorDni.entrySet()) {
                        Persona persona = personaRepository.findById(entry.getKey()).orElse(null);
                        if (persona != null) {
                                Map<String, Object> clienteInfo = new HashMap<>();
                                clienteInfo.put("nombres", persona.getNombres());
                                clienteInfo.put("apellidos", persona.getApellidos());
                                clienteInfo.put("dni", persona.getDni());
                                clienteInfo.put("cantidadMorosos", entry.getValue().size());

                                // Contar cuántos están actualmente vencidos (no entregados)
                                long vencidosActivos = entry.getValue().stream()
                                                .filter(p -> "VENCIDO".equalsIgnoreCase(p.getEstado()))
                                                .count();
                                clienteInfo.put("vencidosActivos", vencidosActivos);
                                clienteInfo.put("tieneVencidoActivo", vencidosActivos > 0);
                                clientesMorosos.add(clienteInfo);
                        }
                }

                // Ordenar morosos: primero los que tienen vencidos activos
                clientesMorosos.sort((a, b) -> {
                        boolean aActivo = (boolean) a.get("tieneVencidoActivo");
                        boolean bActivo = (boolean) b.get("tieneVencidoActivo");
                        if (aActivo != bActivo)
                                return bActivo ? 1 : -1;
                        return ((Integer) b.get("cantidadMorosos")).compareTo((Integer) a.get("cantidadMorosos"));
                });

                // 7) Enviar datos a la vista
                model.addAttribute("totalClientes", totalClientes);
                model.addAttribute("prestamosActivos", prestamosActivos);
                model.addAttribute("prestamosPagados", prestamosPagados);
                model.addAttribute("totalPrestado", totalPrestado);
                model.addAttribute("pendienteCobro", pendienteCobro);
                model.addAttribute("clientesFrecuentes", clientesFrecuentes);
                model.addAttribute("clientesMorosos", clientesMorosos);

                return "dashboard";
        }
}
