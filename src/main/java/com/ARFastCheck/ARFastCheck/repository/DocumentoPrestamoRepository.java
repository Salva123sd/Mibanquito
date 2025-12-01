package com.ARFastCheck.ARFastCheck.repository;

import com.ARFastCheck.ARFastCheck.model.DocumentoPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoPrestamoRepository extends JpaRepository<DocumentoPrestamo, Long> {

    // ANTES: List<DocumentoPrestamo> findByPrestamoId(Long prestamoId);

    // AHORA:
    List<DocumentoPrestamo> findByPrestamo_Id(Long prestamoId);
}
