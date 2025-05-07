package com.example.ecommerce.controller;

import com.example.ecommerce.model.Producto;
import com.example.ecommerce.service.IproductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private IproductoService productoService;

    // Listado de productos con búsqueda por nombre o categoría (uno solo)
    @GetMapping
    public String listarProductos(@RequestParam(required = false) String nombre,
                                  @RequestParam(required = false) String categoria,
                                  Model model) {

        List<Producto> productos = productoService.buscarPorNombreOCategoria(nombre, categoria);

        // 👇 Imprimí en consola la URL de imagen de cada producto
        for (Producto producto : productos) {
            System.out.println("Imagen de producto: " + producto.getImagenUrl());
        }

        model.addAttribute("productos", productos);
        model.addAttribute("nombre", nombre);
        model.addAttribute("categoria", categoria);
        return "producto/listado";
    }

    // Formulario para nuevo producto
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return "producto/formulario";
    }

    // Guardar nuevo producto
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto,
                                  @RequestParam("imagen") MultipartFile imagenFile) {

        if (!imagenFile.isEmpty()) {
            try {
                String nombreArchivo = imagenFile.getOriginalFilename();
                Path ruta = Paths.get("src/main/resources/static/img/" + nombreArchivo);
                Files.write(ruta, imagenFile.getBytes());
                producto.setImagenUrl("/img/" + nombreArchivo);
            } catch (IOException e) {
                e.printStackTrace(); // Considerá usar un logger
            }
        }

        productoService.crear(producto);
        return "redirect:/productos";
    }

    // Formulario para editar producto
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Producto producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);
        return "producto/formulario";
    }

    // Actualizar producto existente
    @PostMapping("/actualizar/{id}")
    public String actualizarProducto(@PathVariable Long id,
                                     @ModelAttribute Producto productoActualizado,
                                     @RequestParam("imagen") MultipartFile imagenFile) {

        Producto productoExistente = productoService.buscarPorId(id);

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
            productoActualizado.setImagenUrl(productoExistente.getImagenUrl());
        }

        productoActualizado.setId(id);
        productoService.editar(id, productoActualizado);
        return "redirect:/productos";
    }

    // Eliminación lógica
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoService.eliminarLogico(id);
        return "redirect:/productos";
    }
}
