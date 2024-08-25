package com.example.lite_erp.infra.security;

import com.example.lite_erp.entities.usuario.Usuario;
import com.example.lite_erp.entities.usuario.UsuarioRepository;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = this.repository.findByNomeUsuario(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Extraindo a role do campo nome_categoria
        var authorities = Collections.singletonList(new SimpleGrantedAuthority(usuario.getRole()));

        return new org.springframework.security.core.userdetails.User(usuario.getNomeUsuario(), usuario.getSenha(), new ArrayList<>());
    }
}
