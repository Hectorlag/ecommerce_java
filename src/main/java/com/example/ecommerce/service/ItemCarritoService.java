package com.example.ecommerce.service;

import com.example.ecommerce.model.Carrito;
import com.example.ecommerce.model.ItemCarrito;
import com.example.ecommerce.model.Producto;

import java.util.List;

public interface ItemCarritoService {

    // Obtener los items de un carrito
    List<ItemCarrito> listarItemsPorCarrito(Carrito carrito);

    // Agregar un producto al carrito (aumenta cantidad si ya existe)
    void agregarProductoAlCarrito(Carrito carrito, Producto producto, int cantidad);

    // Eliminar un producto específico del carrito
    void eliminarProductoDelCarrito(Carrito carrito, Producto producto);

    // Vaciar completamente el carrito
    void vaciarCarrito(Carrito carrito);
}
