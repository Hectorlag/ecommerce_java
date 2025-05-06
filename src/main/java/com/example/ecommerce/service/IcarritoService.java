package com.example.ecommerce.service;

import com.example.ecommerce.model.Carrito;
import com.example.ecommerce.model.Usuario;

public interface IcarritoService {

    Carrito crearCarritoParaUsuario(Usuario usuario);

    Carrito obtenerCarritoPorUsuario(Usuario usuario);
}
