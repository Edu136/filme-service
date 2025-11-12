package br.unibh.filmeservice.dto;

import jakarta.validation.constraints.NotNull;

public record FavoriteCreateDTO(
        @NotNull(message = "O idUser não pode ser nulo.")
        String idUser,
        @NotNull(message = "O idFilme não pode ser nulo.")
        Long idFilme
) {
}
