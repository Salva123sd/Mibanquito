package com.ARFastCheck.ARFastCheck.service;

import com.ARFastCheck.ARFastCheck.model.Objeto;
import com.ARFastCheck.ARFastCheck.repository.ObjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjetoService {

    @Autowired
    private ObjetoRepository objetoRepository;

    // Listar todos los objetos
    public List<Objeto> listarObjetos() {
        return objetoRepository.findAll();
    }

    // Guardar objeto con estado por defecto
    public Objeto guardarObjeto(Objeto objeto) {
        if (objeto.getEstado() == null || objeto.getEstado().isEmpty()) {
            objeto.setEstado("DISPONIBLE");
        }
        return objetoRepository.save(objeto);
    }

    // Eliminar objeto por ID
    public boolean eliminarObjeto(Long id) {
        if (objetoRepository.existsById(id)) {
            objetoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
