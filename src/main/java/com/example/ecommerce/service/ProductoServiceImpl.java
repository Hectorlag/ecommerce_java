package com.example.ecommerce.service;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Producto;
import com.example.ecommerce.repository.IproductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

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
    public Producto crearProducto(Producto producto, MultipartFile imagenFile) {
        String urlImagen = guardarImagen(imagenFile);
        if (urlImagen != null) {
            producto.setImagenUrl(urlImagen);
        }
        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizarProducto(Long id, Producto productoActualizado, MultipartFile imagenFile) {
        Producto producto = buscarPorId(id);

        // Imagen: si se sube una nueva válida, la usamos; si no, se mantiene la anterior
        String urlImagen = guardarImagen(imagenFile);
        if (urlImagen != null) {
            producto.setImagenUrl(urlImagen);
        }

        // Actualizar campos
        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setPrecio(productoActualizado.getPrecio());
        producto.setStock(productoActualizado.getStock());
        producto.setSku(productoActualizado.getSku());
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
            logger.info("Buscando productos por nombre: {}", nombre);
            return productoRepository.findByNombreContainingIgnoreCaseAndBorradoFalse(nombre);
        } else if (tieneCategoria) {
            logger.info("Buscando productos por categoría: {}", categoria);
            return productoRepository.findByCategoriaContainingIgnoreCaseAndBorradoFalse(categoria);
        } else {
            logger.info("Sin filtros aplicados. Listando todos los productos no eliminados.");
            return productoRepository.findByBorradoFalse();
        }
    }


    @Override
    public String guardarImagen(MultipartFile archivo) {
        if (archivo == null || !esImagenValida(archivo)) {
            return null;
        }

        try {
            // Determinar extensión según tipo MIME
            String extension = switch (archivo.getContentType()) {
                case "image/jpeg" -> ".jpg";
                case "image/png" -> ".png";
                case "image/webp" -> ".webp";
                default -> ""; // No debería pasar por la validación
            };

            String nombreUnico = UUID.randomUUID().toString() + extension;
            Path ruta = Paths.get("uploads/img/" + nombreUnico);
            Files.createDirectories(ruta.getParent());
            Files.write(ruta, archivo.getBytes());

            return "/img/" + nombreUnico;

        } catch (IOException e) {
            e.printStackTrace(); // Ideal: usar logger
        }

        return null;
    }
    
    private boolean esImagenValida(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            return false;
        }

        List<String> tiposPermitidos = List.of("image/jpeg", "image/png", "image/webp");
        String tipoMime = archivo.getContentType();

        return tipoMime != null && tiposPermitidos.contains(tipoMime);
    }


}

