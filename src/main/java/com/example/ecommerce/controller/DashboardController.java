package com.example.ecommerce.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model, Authentication authentication) {
        String nombreUsuario = authentication.getName();
        model.addAttribute("username", nombreUsuario);
        model.addAttribute("cantidadProductos", 0); // reemplazar por lógica real
        model.addAttribute("cantidadItemsCarrito", 0); // reemplazar por lógica real
        return "dashboard";
    }
}

