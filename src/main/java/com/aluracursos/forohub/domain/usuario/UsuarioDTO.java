

package com.aluracursos.forohub.domain.usuario;

import java.util.Arrays;
import java.util.List;

public record UsuarioDTO(
        Long id,
        String nombre,
        String email
) {
    public UsuarioDTO(Usuario usuario) {
        this(usuario.getId(), usuario.getNombre(), usuario.getEmail());
    }

}

