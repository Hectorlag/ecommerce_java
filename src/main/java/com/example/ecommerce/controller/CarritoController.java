package com.example.ecommerce.controller;


import com.example.ecommerce.exception.BadRequestException;
import com.example.ecommerce.model.Carrito;
import com.example.ecommerce.model.ItemCarrito;
import com.example.ecommerce.model.Producto;
import com.example.ecommerce.model.Usuario;
import com.example.ecommerce.service.IcarritoService;
import com.example.ecommerce.service.IproductoService;
import com.example.ecommerce.service.ItemCarritoService;
import com.example.ecommerce.service.IusuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private IcarritoService carritoService;

    @Autowired
    private IproductoService productoService;

    @Autowired
    private ItemCarritoService itemCarritoService;

    @Autowired
    private IusuarioService usuarioService;

    // Ver el carrito de un usuario
    @GetMapping("/{usuarioId}")
    public String verCarrito(@PathVariable Long usuarioId, Model model) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        Carrito carrito = carritoService.obtenerCarritoPorUsuario(usuario);
        List<ItemCarrito> items = itemCarritoService.listarItemsPorCarrito(carrito);

        double totalCarrito = calcularTotal(items);

        model.addAttribute("usuario", usuario);
        model.addAttribute("carrito", carrito);
        model.addAttribute("items", items);
        model.addAttribute("totalCarrito", totalCarrito);

        return "carrito/ver";
    }

    // Mostrar productos para agregar al carrito
    @GetMapping("/agregar-productos")
    public String mostrarProductosParaAgregarAlCarrito(@RequestParam Long usuarioId, Model model) {
        List<Producto> productos = productoService.listarTodos();
        model.addAttribute("productos", productos);
        model.addAttribute("usuarioId", usuarioId);
        return "producto/listado-carrito";
    }

    // Agregar producto al carrito (esta lógica sigue en el service de items)
    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam Long usuarioId,
                                   @RequestParam Long productoId,
                                   @RequestParam int cantidad,
                                   RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        Producto producto = productoService.buscarPorId(productoId);
        Carrito carrito = carritoService.obtenerCarritoPorUsuario(usuario);

        try {
            itemCarritoService.agregarProductoAlCarrito(carrito, producto, cantidad);
            redirectAttributes.addFlashAttribute("success", "Se agregó correctamente el producto: " + producto.getNombre());
        } catch (BadRequestException e) {
            redirectAttributes.addFlashAttribute("errorStock", e.getMessage());
            return "redirect:/carrito/agregar-productos?usuarioId=" + usuarioId;
        }

        return "redirect:/carrito/" + usuarioId;
    }

    // Vaciar carrito
    @GetMapping("/vaciar/{usuarioId}")
    public String vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarritoDeUsuario(usuarioId);
        return "redirect:/carrito/" + usuarioId;
    }

    // Eliminar producto del carrito
    @GetMapping("/eliminar-item")
    public String eliminarItemDelCarrito(@RequestParam Long usuarioId,
                                         @RequestParam Long productoId) {
        carritoService.eliminarItemDelCarrito(usuarioId, productoId);
        return "redirect:/carrito/" + usuarioId;
    }

    // Finalizar compra
    @PostMapping("/finalizar/{usuarioId}")
    public String finalizarCompra(@PathVariable Long usuarioId, RedirectAttributes redirectAttributes) {
        try {
            carritoService.finalizarCompra(usuarioId);
            redirectAttributes.addFlashAttribute("exito", "¡Compra realizada con éxito!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/carrito/" + usuarioId;
    }

    private double calcularTotal(List<ItemCarrito> items) {
        return items.stream()
                .mapToDouble(item -> item.getCantidad() * item.getProducto().getPrecio())
                .sum();
    }
}
