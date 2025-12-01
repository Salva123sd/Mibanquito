package com.ARFastCheck.ARFastCheck.service;

import com.ARFastCheck.ARFastCheck.model.Objeto;
import com.ARFastCheck.ARFastCheck.model.Persona;
import com.ARFastCheck.ARFastCheck.model.Prestamo;
import com.ARFastCheck.ARFastCheck.repository.ObjetoRepository;
import com.ARFastCheck.ARFastCheck.repository.PersonaRepository;
import com.ARFastCheck.ARFastCheck.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ObjetoRepository objetoRepository;

    // Listar préstamos y actualizar vencidos automáticamente
    public List<Prestamo> listarPrestamos() {
        List<Prestamo> prestamos = prestamoRepository.findAll();

        for (Prestamo p : prestamos) {
            if ("ACTIVO".equalsIgnoreCase(p.getEstado())
                    && p.getFechaLimite() != null
                    && p.getFechaLimite().isBefore(LocalDate.now())) {
                p.setEstado("VENCIDO");
                prestamoRepository.save(p);
            }
        }

        return prestamos;
    }

    // Registrar préstamo
    public boolean registrarPrestamo(String dni, Long objetoId, String fechaLimite) {
        Persona persona = personaRepository.findById(dni).orElse(null);
        Objeto objeto = objetoRepository.findById(objetoId).orElse(null);

        if (persona != null && objeto != null && !"PRESTADO".equalsIgnoreCase(objeto.getEstado())) {
            Prestamo prestamo = new Prestamo();
            prestamo.setPersona(persona);
            prestamo.setObjeto(objeto);
            prestamo.setFechaPrestamo(LocalDate.now());
            prestamo.setFechaLimite(LocalDate.parse(fechaLimite));
            prestamo.setEstado("ACTIVO");

            objeto.setEstado("PRESTADO");

            objetoRepository.save(objeto);
            prestamoRepository.save(prestamo);
            return true;
        }
        return false;
    }

    // Devolver préstamo
    public String devolverPrestamo(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id).orElse(null);

        if (prestamo != null) {
            Objeto objeto = prestamo.getObjeto();

            if ("ACTIVO".equals(prestamo.getEstado())) {
                prestamo.setEstado("DEVUELTO");
                prestamo.setFechaDevolucion(LocalDate.now());
                objeto.setEstado("DISPONIBLE");
                prestamoRepository.save(prestamo);
                objetoRepository.save(objeto);
                return "success";
            } else if ("VENCIDO".equals(prestamo.getEstado())) {
                prestamo.setEstado("ENTREGADO ATRASADO");
                prestamo.setFechaDevolucion(LocalDate.now());
                objeto.setEstado("DISPONIBLE");
                prestamoRepository.save(prestamo);
                objetoRepository.save(objeto);
                return "warning";
            }
        }
        return "danger"; // no encontrado o error
    }

    // 🔹 Nuevo: Obtener préstamo por id (para detalle, empeños, documentos)
    public Prestamo obtenerPorId(Long id) {
        return prestamoRepository.findById(id).orElse(null);
    }
}
