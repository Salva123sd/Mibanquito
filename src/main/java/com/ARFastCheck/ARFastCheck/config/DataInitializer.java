package com.ARFastCheck.ARFastCheck.config;

import com.ARFastCheck.ARFastCheck.model.Cliente;
import com.ARFastCheck.ARFastCheck.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Crear un usuario de prueba si no existe
            if (!clienteRepository.existsByUsername("admin")) {
                Cliente admin = new Cliente();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@arfastcheck.com");
                admin.setNombres("Administrador");
                admin.setApellidos("Sistema");
                admin.setDni("12345678");
                clienteRepository.save(admin);
                System.out.println("✓ Usuario de prueba creado: admin / admin123");
            }
        };
    }
}
