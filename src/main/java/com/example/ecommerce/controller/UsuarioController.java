package com.example.ecommerce.controller;

import com.example.ecommerce.model.Usuario;
import com.example.ecommerce.service.IusuarioService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IusuarioService usuarioService;

    // Mostrar formulario de registro
    @GetMapping("/registrar")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/formulario"; // /templates/usuario/formulario.html
    }

    // Guardar nuevo usuario
    @PostMapping("/guardar")
    public String registrarUsuario(@Valid @ModelAttribute Usuario usuario) {
        usuarioService.registrar(usuario);
        return "redirect:/usuarios/perfil/" + usuario.getId();
    }

    // Ver perfil de usuario
    @GetMapping("/perfil/{id}")
    public String verPerfil(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        model.addAttribute("usuario", usuario);
        return "usuario/perfil"; // /templates/usuario/perfil.html
    }

    @GetMapping("/buscar")
    public String buscarPorEmail(@RequestParam String nombre, Model model) {
        Optional<Usuario> usuario = usuarioService.buscarPorNombre(nombre);

        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            return "usuario/perfil"; // Si lo encontró
        } else {
            model.addAttribute("error", "Usuario no encontrado con ese email");
            return "auth/login"; // O una vista de error o retorno al login
        }
    }

}
