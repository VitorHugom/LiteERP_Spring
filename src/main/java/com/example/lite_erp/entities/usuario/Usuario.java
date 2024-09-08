package com.example.lite_erp.entities.usuario;

import com.example.lite_erp.entities.categoria_usuario.CategoriasUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_usuario")
    private String nomeUsuario;

    private String email;
    private String senha;
    private Integer categoria_id;
    private String status;

    private String telefone;

    @ManyToOne
    @JoinColumn(name = "categoria_id", insertable = false, updatable = false)
    private CategoriasUsuario categoria;

    public String getRole() {
        return "ROLE_" + this.categoria.getNome_categoria().toUpperCase();
    }
}
