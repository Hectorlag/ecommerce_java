package com.example.ecommerce.service;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Usuario;
import com.example.ecommerce.repository.IusuarioRepository;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements IusuarioService{


    @Autowired
    private IusuarioRepository usuarioRepository;

    @Autowired
    @Lazy
    private IcarritoService carritoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario registrar(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario guardado = usuarioRepository.save(usuario);

        // Crear el carrito después de guardar el usuario
        carritoService.crearCarritoParaUsuario(guardado);

        System.out.println("✅ Usuario registrado con carrito: " + guardado.getNombre());
        return guardado;
    }


    @Override
        public Usuario buscarPorId(Long id) {
            return usuarioRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        }

    @Override
    public Optional<Usuario> buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }



}

