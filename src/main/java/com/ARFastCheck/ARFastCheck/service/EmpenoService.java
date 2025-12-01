package com.ARFastCheck.ARFastCheck.service;

import com.ARFastCheck.ARFastCheck.model.Empeno;
import com.ARFastCheck.ARFastCheck.repository.EmpenoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpenoService {

    private static final Logger logger = LoggerFactory.getLogger(EmpenoService.class);

    private final EmpenoRepository empenoRepository;

    public EmpenoService(EmpenoRepository empenoRepository) {
        this.empenoRepository = empenoRepository;
    }

    public List<Empeno> listarPorPrestamo(Long prestamoId) {
        return empenoRepository.findByPrestamo_Id(prestamoId);
    }

    public Empeno guardar(Empeno empeno) {
        logger.debug("Guardando Empeno con ID: {}", empeno.getId());
        return empenoRepository.save(empeno);
    }
}
