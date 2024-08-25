package com.example.lite_erp.entities.categoria_usuario;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categorias_usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriasUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome_categoria;

}
