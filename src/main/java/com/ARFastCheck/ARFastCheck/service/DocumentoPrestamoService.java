package com.ARFastCheck.ARFastCheck.service;


import com.ARFastCheck.ARFastCheck.model.DocumentoPrestamo;
import com.ARFastCheck.ARFastCheck.repository.DocumentoPrestamoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentoPrestamoService {

    private final DocumentoPrestamoRepository documentoPrestamoRepository;

    private static final Logger logger = LoggerFactory.getLogger(DocumentoPrestamoService.class);

    public DocumentoPrestamoService(DocumentoPrestamoRepository documentoPrestamoRepository) {
        this.documentoPrestamoRepository = documentoPrestamoRepository;
    }

    public List<DocumentoPrestamo> listarPorPrestamo(Long prestamoId) {
        return documentoPrestamoRepository.findByPrestamo_Id(prestamoId);
    }

    public DocumentoPrestamo guardar(DocumentoPrestamo documento) {
        logger.debug("Guardando DocumentoPrestamo con ID: {}", documento.getId());
        return documentoPrestamoRepository.save(documento);
    }
}
