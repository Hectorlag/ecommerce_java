package com.example.ecommerce.service;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Carrito;
import com.example.ecommerce.model.ItemCarrito;
import com.example.ecommerce.model.Producto;
import com.example.ecommerce.model.Usuario;
import com.example.ecommerce.repository.IcarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoServiceImpl implements IcarritoService {

    @Autowired
    private IcarritoRepository carritoRepository;

    @Autowired
    private ItemCarritoService itemCarritoService;

    @Autowired
    private IproductoService productoService;

    @Autowired
    private IusuarioService usuarioService;

    @Override
    public Carrito crearCarritoParaUsuario(Usuario usuario) {
        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        return carritoRepository.save(carrito);
    }

    @Override
    public Carrito obtenerCarritoPorUsuario(Usuario usuario) {
        return carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> crearCarritoParaUsuario(usuario));
    }

    @Override
    public void finalizarCompra(Long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        Carrito carrito = obtenerCarritoPorUsuario(usuario);
        List<ItemCarrito> items = itemCarritoService.listarItemsPorCarrito(carrito);

        if (items.isEmpty()) {
            throw new IllegalStateException("Tu carrito está vacío.");
        }

        for (ItemCarrito item : items) {
            Producto producto = item.getProducto();
            if (producto.getStock() < item.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para el producto: " + producto.getNombre());
            }
        }

        for (ItemCarrito item : items) {
            Producto producto = item.getProducto();
            producto.setStock(producto.getStock() - item.getCantidad());
            productoService.actualizarProducto(producto.getId(), producto, null);
        }

        itemCarritoService.vaciarCarrito(carrito);
    }

    public void vaciarCarritoDeUsuario(Long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        Carrito carrito = obtenerCarritoPorUsuario(usuario);
        itemCarritoService.vaciarCarrito(carrito);
    }

    public void eliminarItemDelCarrito(Long usuarioId, Long productoId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        Carrito carrito = obtenerCarritoPorUsuario(usuario);
        Producto producto = productoService.buscarPorId(productoId);
        itemCarritoService.eliminarProductoDelCarrito(carrito, producto);
    }
}


