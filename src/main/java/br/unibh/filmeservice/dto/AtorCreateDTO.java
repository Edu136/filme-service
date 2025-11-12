package br.unibh.filmeservice.dto;

import jakarta.validation.constraints.NotNull;

public record AtorCreateDTO(
        @NotNull(message = "O nome do ator é obrigatório")
        String nome,
        @NotNull(message = "O personagem é obrigatório")
        String personagem,
        @NotNull(message = "A url da foto é obrigatória")
        String fotoUrl
) {
}
