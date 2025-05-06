package com.example.ecommerce.repository;

import com.example.ecommerce.model.Carrito;
import com.example.ecommerce.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IcarritoRepository extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByUsuario(Usuario usuario);
}
