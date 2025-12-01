package com.ARFastCheck.ARFastCheck.service;

import com.ARFastCheck.ARFastCheck.model.Cliente;
import com.ARFastCheck.ARFastCheck.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cliente cliente = clienteRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return User.builder()
                .username(cliente.getUsername())
                .password(cliente.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(cliente.getRole())))
                .disabled(!cliente.isEnabled())
                .build();
    }
}
