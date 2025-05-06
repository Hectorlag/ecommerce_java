package com.example.ecommerce.service;


import com.example.ecommerce.model.Usuario;
import com.example.ecommerce.repository.IusuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UsuarioDetailService implements UserDetailsService {

    @Autowired
    private IusuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        System.out.println("🔍 Buscando usuario con nombre: " + nombre);

        Usuario usuario = usuarioRepository.findByNombre(nombre)
                .orElseThrow(() -> {
                    System.out.println("❌ Usuario no encontrado con nombre: " + nombre);
                    return new UsernameNotFoundException("Usuario no encontrado: " + nombre);
                });

        System.out.println("✅ Usuario encontrado: " + usuario.getNombre());
        System.out.println("🔐 Contraseña (encriptada): " + usuario.getPassword());

        return new User(
                usuario.getNombre(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}

