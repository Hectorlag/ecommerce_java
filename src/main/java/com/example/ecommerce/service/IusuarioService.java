package com.example.ecommerce.service;

import com.example.ecommerce.model.Usuario;

import java.util.Optional;

public interface IusuarioService {

        Usuario registrar(Usuario usuario);

        Usuario buscarPorId(Long id);

        Optional<Usuario> buscarPorNombre(String nombre);

}
