package com.example.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El SKU es obligatorio")
    @Column(unique = true)
    private String sku;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @Positive(message = "El precio debe ser mayor a cero")
    private Double precio;

    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    //@NotBlank(message = "La URL de imagen es obligatoria")
    private String imagenUrl;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fechaElaboracion;

    private boolean borrado;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<ItemCarrito> items;

}
