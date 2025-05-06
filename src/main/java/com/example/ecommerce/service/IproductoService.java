package com.example.ecommerce.service;

import com.example.ecommerce.model.Producto;

import java.util.List;

public interface IproductoService {

    List<Producto> listarTodos();

    Producto buscarPorId(Long id);

    Producto crear(Producto producto);

    Producto editar(Long id, Producto productoActualizado);

    void eliminarLogico(Long id);

    List<Producto> buscarPorNombreOCategoria(String criterio);
}
