package com.example.ecommerce.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class ItemCarrito {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "carrito_id", nullable = false)
        private Carrito carrito;

        @ManyToOne
        @JoinColumn(name = "producto_id", nullable = false)
        private Producto producto;

        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        private Integer cantidad;

}
