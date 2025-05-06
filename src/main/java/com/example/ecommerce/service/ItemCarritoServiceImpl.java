package com.example.ecommerce.service;


import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Carrito;
import com.example.ecommerce.model.ItemCarrito;
import com.example.ecommerce.model.Producto;
import com.example.ecommerce.repository.IproductoRepository;
import com.example.ecommerce.repository.ItemCarritoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemCarritoServiceImpl implements ItemCarritoService {

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private IproductoRepository productoRepository;

    @Override
    public List<ItemCarrito> listarItemsPorCarrito(Carrito carrito) {
        return itemCarritoRepository.findByCarrito(carrito);
    }

    @Override
    @Transactional
    public void agregarProductoAlCarrito(Carrito carrito, Producto producto, int cantidad) {
        validarProductoParaAgregar(producto, cantidad);

        ItemCarrito itemExistente = itemCarritoRepository
                .findByCarritoAndProducto(carrito, producto)
                .orElse(null);

        if (itemExistente != null) {
            int nuevaCantidad = itemExistente.getCantidad() + cantidad;
            if (nuevaCantidad > producto.getStock()) {
                throw new BadRequestException("No hay suficiente stock para aumentar la cantidad");
            }
            itemExistente.setCantidad(nuevaCantidad);
            itemCarritoRepository.save(itemExistente);
        } else {
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(cantidad);
            itemCarritoRepository.save(nuevoItem);
        }
    }

    @Override
    @Transactional
    public void eliminarProductoDelCarrito(Carrito carrito, Producto producto) {
        ItemCarrito item = itemCarritoRepository
                .findByCarritoAndProducto(carrito, producto)
                .orElseThrow(() -> new ResourceNotFoundException("El producto no está en el carrito"));

        itemCarritoRepository.delete(item);
    }

    @Override
    @Transactional
    public void vaciarCarrito(Carrito carrito) {
        itemCarritoRepository.deleteByCarrito(carrito);

    }

    private void validarProductoParaAgregar(Producto producto, int cantidad) {
        if (producto == null) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }

        if (cantidad <= 0) {
            throw new BadRequestException("La cantidad debe ser mayor a cero");
        }

        if (producto.getStock() < cantidad) {
            throw new BadRequestException("Stock insuficiente para el producto: " + producto.getNombre());
        }
    }
}

