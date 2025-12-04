package com.ARFastCheck.ARFastCheck.repository;

import com.ARFastCheck.ARFastCheck.model.Empeno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface EmpenoRepository extends JpaRepository<Empeno, Long> {

    // ANTES: List<Empeno> findByPrestamoId(Long prestamoId);

    // AHORA:
    List<Empeno> findByPrestamo_Id(Long prestamoId);

    // Suma total de todos los empeños (total prestado)
    @Query("SELECT COALESCE(SUM(e.valorEstimado), 0) FROM Empeno e")
    BigDecimal sumTotalPrestado();

    // Suma de empeños con préstamos activos o vencidos (pendiente de cobro)
    @Query("SELECT COALESCE(SUM(e.valorEstimado), 0) FROM Empeno e WHERE e.prestamo.estado IN ('ACTIVO', 'VENCIDO')")
    BigDecimal sumPendienteCobro();
}
