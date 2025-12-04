package com.ARFastCheck.ARFastCheck.service;

import com.ARFastCheck.ARFastCheck.model.Objeto;
import com.ARFastCheck.ARFastCheck.repository.ObjetoRepository;
import com.ARFastCheck.ARFastCheck.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjetoService {

    @Autowired
    private ObjetoRepository objetoRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

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
    public String eliminarObjeto(Long id) {
        if (!objetoRepository.existsById(id)) {
            return "NO_ENCONTRADO";
        }
        // Solo bloquear si tiene préstamos ACTIVOS o VENCIDOS
        List<String> estadosActivos = List.of("ACTIVO", "VENCIDO");
        if (prestamoRepository.existsByObjetoIdAndEstadoIn(id, estadosActivos)) {
            return "TIENE_PRESTAMOS";
        }
        objetoRepository.deleteById(id);
        return "ELIMINADO";
    }
}
