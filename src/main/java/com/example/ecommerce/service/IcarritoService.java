package com.example.ecommerce.service;

import com.example.ecommerce.model.Carrito;
import com.example.ecommerce.model.ItemCarrito;
import com.example.ecommerce.model.Usuario;

import java.util.List;

public interface IcarritoService {

    Carrito crearCarritoParaUsuario(Usuario usuario);

    Carrito obtenerCarritoPorUsuario(Usuario usuario);

    void agregarProductoAlCarrito(Long usuarioId, Long productoId, int cantidad);

    List<ItemCarrito> obtenerItemsDelUsuario(Long usuarioId);

    double calcularTotal(Long usuarioId);

    void finalizarCompra(Long usuarioId);

    void vaciarCarritoDeUsuario(Long usuarioId);

    void eliminarItemDelCarrito(Long usuarioId, Long productoId);
}