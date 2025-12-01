package com.ARFastCheck.ARFastCheck.service;

import com.ARFastCheck.ARFastCheck.model.Cliente;
import com.ARFastCheck.ARFastCheck.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Cliente registrarCliente(Cliente cliente) {
        // Validar que el username no exista
        if (clienteRepository.existsByUsername(cliente.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        // Validar que el email no exista
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Validar que el DNI no exista (si se proporciona)
        if (cliente.getDni() != null && !cliente.getDni().isEmpty()
                && clienteRepository.existsByDni(cliente.getDni())) {
            throw new RuntimeException("El DNI ya está registrado");
        }

        // Encriptar la contraseña
        cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));

        // Guardar el cliente
        return clienteRepository.save(cliente);
    }

    public Cliente findByUsername(String username) {
        return clienteRepository.findByUsername(username).orElse(null);
    }
}
