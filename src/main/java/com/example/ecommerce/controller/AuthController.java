package com.example.ecommerce.controller;

import com.example.ecommerce.model.Usuario;
import com.example.ecommerce.service.IusuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private IusuarioService usuarioService;

    // Mostrar login personalizado
    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               @RequestParam(value = "registroExitoso", required = false) String registroExitoso,
                               Model model) {

        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas.");
        }

        if (logout != null) {
            model.addAttribute("mensaje", "Sesión cerrada exitosamente.");
        }

        if (registroExitoso != null) {
            model.addAttribute("registroExitoso", "¡Registro exitoso! Ya podés iniciar sesión.");
        }

        return "auth/login"; // resources/templates/auth/login.html
    }

    // Mostrar formulario de registro
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/registro"; // resources/templates/auth/registro.html
    }

    //Procesar registro
    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute Usuario usuario,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("error", "Por favor completá correctamente todos los campos.");
            return "auth/registro";
        }

        if (usuarioService.buscarPorNombre(usuario.getNombre()).isPresent()) {
            model.addAttribute("error", "Ya existe un usuario con ese nombre.");
            return "auth/registro";
        }

        usuarioService.registrar(usuario);
        redirectAttributes.addFlashAttribute("registroExitoso", "¡Registro exitoso! Ya podés iniciar sesión.");
        return "redirect:/login";
    }


}
