package com.example.ecommerce.service;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Carrito;
import com.example.ecommerce.model.ItemCarrito;
import com.example.ecommerce.model.Producto;
import com.example.ecommerce.model.Usuario;
import com.example.ecommerce.repository.IcarritoRepository;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
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
    @Lazy
    private IusuarioService usuarioService;

    @Override
    public Carrito crearCarritoParaUsuario(Usuario usuario) {
        return carritoRepository.findByUsuarioId(usuario.getId())
                .orElseGet(() -> {
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuario(usuario);
                    return carritoRepository.save(nuevoCarrito);
                });
    }

    @Override
    public Carrito obtenerCarritoPorUsuario(Usuario usuario) {
        return carritoRepository.findByUsuarioId(usuario.getId())
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

    @Override
    public void vaciarCarritoDeUsuario(Long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        Carrito carrito = obtenerCarritoPorUsuario(usuario);
        itemCarritoService.vaciarCarrito(carrito);
    }

    @Override
    public void eliminarItemDelCarrito(Long usuarioId, Long productoId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        Carrito carrito = obtenerCarritoPorUsuario(usuario);
        Producto producto = productoService.buscarPorId(productoId);
        itemCarritoService.eliminarProductoDelCarrito(carrito, producto);
    }

    @Override
    public void agregarProductoAlCarrito(Long usuarioId, Long productoId, int cantidad) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        Carrito carrito = obtenerCarritoPorUsuario(usuario);
        Producto producto = productoService.buscarPorId(productoId);
        itemCarritoService.agregarProductoAlCarrito(carrito, producto, cantidad);
    }

    @Override
    public List<ItemCarrito> obtenerItemsDelUsuario(Long usuarioId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        Carrito carrito = obtenerCarritoPorUsuario(usuario);
        return itemCarritoService.listarItemsPorCarrito(carrito);
    }

    @Override
    public double calcularTotal(Long usuarioId) {
        List<ItemCarrito> items = obtenerItemsDelUsuario(usuarioId);
        return items.stream()
                .mapToDouble(item -> item.getCantidad() * item.getProducto().getPrecio())
                .sum();
    }
}