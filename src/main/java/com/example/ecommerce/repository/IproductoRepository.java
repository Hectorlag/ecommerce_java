package com.example.ecommerce.repository;

import com.example.ecommerce.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IproductoRepository extends JpaRepository<Producto, Long> {



    //Obtener los productos no eliminados
    List<Producto> findByBorradoFalse();

    List<Producto> findByNombreContainingIgnoreCaseAndBorradoFalse(String nombre);

    List<Producto> findByCategoriaContainingIgnoreCaseAndBorradoFalse(String categoria);

}
