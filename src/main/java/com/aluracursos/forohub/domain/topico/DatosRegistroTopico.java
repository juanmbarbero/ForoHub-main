package com.aluracursos.forohub.domain.topico;

import com.aluracursos.forohub.domain.respuesta.DatosRegistroRespuesta;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record DatosRegistroTopico(
        @NotBlank
        String titulo,
        @NotBlank
        String mensaje,
        Status status,
        Long autorId,
        @NotBlank
        String autor,
        @NotBlank
        String curso,

        List<DatosRegistroRespuesta> respuestas
        ) {
}
