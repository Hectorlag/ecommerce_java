package com.example.ecommerce.repository;

import com.example.ecommerce.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IproductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findBySku(String sku);

    //Búsqueda por nombre o categoria
    List<Producto> findByNombreContainingIgnoreCaseOrCategoriaContainingIgnoreCase(String nombre, String categoria);

    //Obtener los productos no eliminados
    List<Producto> findByBorradoFalse();


}
