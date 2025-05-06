package com.example.ecommerce.service;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Producto;
import com.example.ecommerce.repository.IproductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ProductoServiceImpl implements IproductoService{

    private static final Logger logger = LoggerFactory.getLogger(ProductoServiceImpl.class);

    @Autowired
    private IproductoRepository productoRepository;

    @Override
    public List<Producto> listarTodos() {
        return productoRepository.findByBorradoFalse();
    }

    @Override
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    @Override
    public Producto crear(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto editar(Long id, Producto productoActualizado) {
        Producto producto = buscarPorId(id);

        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setPrecio(productoActualizado.getPrecio());
        producto.setStock(productoActualizado.getStock());
        producto.setSku(productoActualizado.getSku());
        producto.setImagenUrl(productoActualizado.getImagenUrl());
        producto.setCategoria(productoActualizado.getCategoria());
        producto.setFechaElaboracion(productoActualizado.getFechaElaboracion());

        return productoRepository.save(producto);
    }

    @Override
    public void eliminarLogico(Long id) {
        Producto producto = buscarPorId(id);
        producto.setBorrado(true);
        productoRepository.save(producto);
    }

    @Override
    public List<Producto> buscarPorNombreOCategoria(String nombre, String categoria) {
        boolean tieneNombre = nombre != null && !nombre.isBlank();
        boolean tieneCategoria = categoria != null && !categoria.isBlank();

        if (tieneNombre) {
            return productoRepository.findByNombreContainingIgnoreCaseAndDeletedFalse(nombre);
        } else if (tieneCategoria) {
            return productoRepository.findByCategoriaContainingIgnoreCaseAndDeletedFalse(categoria);
        } else {
            return productoRepository.findByDeletedFalse(); // devuelve todos
        }
    }
}

}

