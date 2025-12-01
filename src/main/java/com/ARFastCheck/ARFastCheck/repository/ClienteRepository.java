package com.ARFastCheck.ARFastCheck.repository;

import com.ARFastCheck.ARFastCheck.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);
}
