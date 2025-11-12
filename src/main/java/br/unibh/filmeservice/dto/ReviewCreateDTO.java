package br.unibh.filmeservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateDTO(
        @NotNull(message = "O rating é não pode ser nulo")
        @Min(message = "O rating deve ser no mínimo 0", value = 0)
        @Max(message = "O rating deve ser no máximo 10", value = 5)
        Integer rating,
        @NotNull(message = "O comentário não pode ser nulo")
        String comment,
        @NotNull(message = "O id do usuário não pode ser nulo")
        String idUser,
        @NotNull(message = "O id do filme não pode ser nulo")
        Long idFilme
) {
}
