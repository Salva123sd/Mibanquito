package com.ARFastCheck.ARFastCheck.repository;

import com.ARFastCheck.ARFastCheck.model.Empeno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpenoRepository extends JpaRepository<Empeno, Long> {

    // ANTES: List<Empeno> findByPrestamoId(Long prestamoId);

    // AHORA:
    List<Empeno> findByPrestamo_Id(Long prestamoId);
}
