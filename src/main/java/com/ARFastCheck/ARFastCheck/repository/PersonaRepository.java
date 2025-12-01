package com.ARFastCheck.ARFastCheck.repository;

import com.ARFastCheck.ARFastCheck.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, String> {
    Persona findByDni(String dni);
}
