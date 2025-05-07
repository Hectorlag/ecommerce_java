package com.example.ecommerce.service;

import com.example.ecommerce.model.Carrito;
import com.example.ecommerce.model.Usuario;

public interface IcarritoService {

    Carrito crearCarritoParaUsuario(Usuario usuario);

    Carrito obtenerCarritoPorUsuario(Usuario usuario);

    void finalizarCompra(Long usuarioId);

    void vaciarCarritoDeUsuario(Long usuarioId);

    void eliminarItemDelCarrito(Long usuarioId, Long productoId);


}
