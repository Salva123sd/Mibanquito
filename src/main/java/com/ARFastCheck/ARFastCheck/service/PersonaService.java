package com.ARFastCheck.ARFastCheck.service;

import com.ARFastCheck.ARFastCheck.model.Persona;
import com.ARFastCheck.ARFastCheck.repository.PersonaRepository;
import com.ARFastCheck.ARFastCheck.repository.PrestamoRepository;
import com.ARFastCheck.ARFastCheck.dto.ReniecResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private ReniecService reniecService;

    // Listar todas las personas
    public List<Persona> listarPersonas() {
        return personaRepository.findAll();
    }

    // Guardar persona validando con RENIEC
    public String guardarPersona(Persona persona) {
        try {
            ReniecResponse datos = reniecService.consultarDni(persona.getDni()).orElse(null);

            if (datos == null || datos.getFirstName() == null) {
                return "invalid"; // DNI inválido
            }

            persona.setNombres(datos.getFirstName());
            persona.setApellidos(
                    (datos.getFirstLastName() != null ? datos.getFirstLastName() : "") +
                            (datos.getSecondLastName() != null ? " " + datos.getSecondLastName() : "")
            );

            personaRepository.save(persona);
            return "success";

        } catch (Exception e) {
            return "error";
        }
    }

    // Eliminar persona validando préstamos
    public String eliminarPersona(String dni) {
        if (!personaRepository.existsById(dni)) {
            return "notFound"; //No existe
        }

        if (!prestamoRepository.findByPersonaDni(dni).isEmpty()) {
            return "tienePrestamos"; //Tiene préstamos registrados
        }

        try {
            personaRepository.deleteById(dni);
            return "success"; //Eliminado
        } catch (Exception e) {
            return "error"; //Error inesperado
        }
    }
}
