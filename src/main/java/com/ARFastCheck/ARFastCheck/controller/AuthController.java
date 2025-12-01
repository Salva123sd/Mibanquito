package com.ARFastCheck.ARFastCheck.controller;

import com.ARFastCheck.ARFastCheck.model.Cliente;
import com.ARFastCheck.ARFastCheck.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }
        if (logout != null) {
            model.addAttribute("mensaje", "Has cerrado sesión exitosamente");
        }
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarCliente(@ModelAttribute Cliente cliente, Model model) {
        try {
            clienteService.registrarCliente(cliente);
            model.addAttribute("exito", "Registro exitoso. Ahora puedes iniciar sesión.");
            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cliente", cliente);
            return "registro";
        }
    }
}
