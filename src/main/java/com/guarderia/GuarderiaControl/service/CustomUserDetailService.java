package com.guarderia.GuarderiaControl.service;

import com.guarderia.GuarderiaControl.model.Usuario;
import com.guarderia.GuarderiaControl.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        Set<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                .collect(Collectors.toSet());

        return new User(usuario.getUsername(), usuario.getPassword(), authorities);
    }
}

