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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private static final Logger logger = LoggerFactory.getLogger(CarritoController.class);

    @Autowired
    private IcarritoService carritoService;

    @Autowired
    private IproductoService productoService;

    @Autowired
    private ItemCarritoService itemCarritoService;

    @Autowired
    private IusuarioService usuarioService;

    // Ver el carrito del usuario logueado
    @GetMapping
    public String verCarrito(Model model, Authentication authentication) {
        Usuario usuario = getUsuarioAutenticado(authentication);
        Carrito carrito = carritoService.obtenerCarritoPorUsuario(usuario);
        List<ItemCarrito> items = itemCarritoService.listarItemsPorCarrito(carrito);

        double totalCarrito = calcularTotal(items);

        logger.info("🛒 Mostrando carrito ID {} para usuario ID {}", carrito.getId(), usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("carrito", carrito);
        model.addAttribute("items", items);
        model.addAttribute("totalCarrito", totalCarrito);

        return "carrito/ver";
    }

    // Mostrar productos para agregar al carrito
    @GetMapping("/agregar-productos")
    public String mostrarProductosParaAgregarAlCarrito(Model model, Authentication authentication) {
        Usuario usuario = getUsuarioAutenticado(authentication);
        List<Producto> productos = productoService.listarTodos();
        model.addAttribute("productos", productos);
        model.addAttribute("usuarioId", usuario.getId());
        return "producto/listado-carrito";
    }

    // Agregar producto al carrito
    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam Long productoId,
                                   @RequestParam int cantidad,
                                   RedirectAttributes redirectAttributes,
                                   Authentication authentication) {
        Usuario usuario = getUsuarioAutenticado(authentication);
        Producto producto = productoService.buscarPorId(productoId);
        Carrito carrito = carritoService.obtenerCarritoPorUsuario(usuario);

        try {
            itemCarritoService.agregarProductoAlCarrito(carrito, producto, cantidad);
            logger.info("✅ Producto '{}' agregado al carrito ID {} del usuario ID {}", producto.getNombre(), carrito.getId(), usuario.getId());
            redirectAttributes.addFlashAttribute("success", "Se agregó correctamente el producto: " + producto.getNombre());
        } catch (BadRequestException e) {
            logger.warn("⚠️ No se pudo agregar producto al carrito: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorStock", e.getMessage());
            return "redirect:/carrito/agregar-productos";
        }

        return "redirect:/carrito";
    }

    // Vaciar carrito
    @GetMapping("/vaciar")
    public String vaciarCarrito(Authentication authentication) {
        Usuario usuario = getUsuarioAutenticado(authentication);
        carritoService.vaciarCarritoDeUsuario(usuario.getId());
        logger.info("🧹 Carrito del usuario ID {} vaciado.", usuario.getId());
        return "redirect:/carrito";
    }

    // Eliminar producto del carrito
    @GetMapping("/eliminar-item")
    public String eliminarItemDelCarrito(@RequestParam Long productoId,
                                         Authentication authentication) {
        Usuario usuario = getUsuarioAutenticado(authentication);
        carritoService.eliminarItemDelCarrito(usuario.getId(), productoId);
        logger.info("❌ Producto ID {} eliminado del carrito del usuario ID {}", productoId, usuario.getId());
        return "redirect:/carrito";
    }

    // Finalizar compra
    @PostMapping("/finalizar")
    public String finalizarCompra(RedirectAttributes redirectAttributes, Authentication authentication) {
        Usuario usuario = getUsuarioAutenticado(authentication);
        try {
            carritoService.finalizarCompra(usuario.getId());
            redirectAttributes.addFlashAttribute("exito", "¡Compra realizada con éxito!");
            logger.info("💰 Compra finalizada por el usuario ID {}", usuario.getId());
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            logger.warn("❌ Error al finalizar compra: {}", e.getMessage());
        }
        return "redirect:/carrito";
    }

    private Usuario getUsuarioAutenticado(Authentication authentication) {
        String username = authentication.getName();
        return usuarioService.buscarPorNombre(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en sesión"));
    }

    private double calcularTotal(List<ItemCarrito> items) {
        return items.stream()
                .mapToDouble(item -> item.getCantidad() * item.getProducto().getPrecio())
                .sum();
    }
}