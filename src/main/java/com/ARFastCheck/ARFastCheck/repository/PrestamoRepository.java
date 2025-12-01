package com.ARFastCheck.ARFastCheck.repository;

import com.ARFastCheck.ARFastCheck.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByPersonaDni(String dni);

    // Préstamos dentro de un rango de fechas (para el mes)
    List<Prestamo> findByFechaPrestamoBetween(LocalDate inicio, LocalDate fin);

    // Contar por estado (ACTIVO, DEVUELTO, VENCIDO, ENTREGADO ATRASADO)
    long countByEstado(String estado);
}
