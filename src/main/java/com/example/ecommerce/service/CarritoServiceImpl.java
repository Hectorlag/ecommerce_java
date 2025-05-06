package com.example.ecommerce.service;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Carrito;
import com.example.ecommerce.model.Usuario;
import com.example.ecommerce.repository.IcarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarritoServiceImpl implements IcarritoService{

    @Autowired
    private IcarritoRepository carritoRepository;

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

}

