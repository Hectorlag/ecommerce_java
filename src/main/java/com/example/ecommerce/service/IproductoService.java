package com.example.ecommerce.service;

import com.example.ecommerce.model.Producto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IproductoService {

    List<Producto> listarTodos();

    Producto buscarPorId(Long id);

    Producto crearProducto(Producto producto, MultipartFile imagenFile);

    Producto actualizarProducto(Long id, Producto productoActualizado, MultipartFile imagenFile);

    void eliminarLogico(Long id);

    List<Producto> buscarPorNombreOCategoria(String nombre, String categoria);

    String guardarImagen(MultipartFile imagenFile);

}
