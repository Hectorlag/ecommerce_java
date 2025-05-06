package com.example.ecommerce.controller;


import com.example.ecommerce.model.Producto;
import com.example.ecommerce.service.IproductoService;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private IproductoService productoService;

    // Mostrar listado de productos
    @GetMapping
    public String listarProductos(@RequestParam(required = false) String nombre,
                                  @RequestParam(required = false) String categoria,
                                  Model model) {

        List<Producto> productos = productoService.buscarPorNombreOCategoria(nombre, categoria);

        model.addAttribute("productos", productos);
        model.addAttribute("nombre", nombre);
        model.addAttribute("categoria", categoria);
        return "producto/listado";
    }

    // Mostrar formulario para nuevo producto
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "producto/formulario";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto,
                                  @RequestParam("imagen") MultipartFile imagenFile) {

        if (!imagenFile.isEmpty()) {
            try {
                // Ruta donde se guardarán las imágenes (por ejemplo: /src/main/resources/static/img/)
                String nombreArchivo = imagenFile.getOriginalFilename();
                Path ruta = Paths.get("src/main/resources/static/img/" + nombreArchivo);
                Files.write(ruta, imagenFile.getBytes());

                // Guardamos la URL relativa de la imagen en la base de datos
                producto.setImagenUrl("/img/" + nombreArchivo);

            } catch (IOException e) {
                e.printStackTrace(); // o log.error(...)
            }
        }

        productoService.crear(producto);
        return "redirect:/productos";
    }


    // Mostrar formulario para editar producto existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Producto producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);
        return "producto/formulario";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarProducto(@PathVariable Long id,
                                     @ModelAttribute Producto productoActualizado,
                                     @RequestParam("imagen") MultipartFile imagenFile) {

        // Paso 1: Obtener el producto original
        Producto productoExistente = productoService.buscarPorId(id);

        // Paso 2: Manejo de imagen
        if (!imagenFile.isEmpty()) {
            try {
                String nombreArchivo = imagenFile.getOriginalFilename();
                Path ruta = Paths.get("src/main/resources/static/img/" + nombreArchivo);
                Files.write(ruta, imagenFile.getBytes());

                productoActualizado.setImagenUrl("/img/" + nombreArchivo);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Si no se subió nueva imagen, conservar la anterior
            productoActualizado.setImagenUrl(productoExistente.getImagenUrl());
        }

        // Paso 3: Conservar datos que no vienen del formulario
        productoActualizado.setId(id);

        productoService.editar(id, productoActualizado);
        return "redirect:/productos";
    }


    // Eliminar producto (lógico)
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminarLogico(id);
        return "redirect:/productos";
    }
}