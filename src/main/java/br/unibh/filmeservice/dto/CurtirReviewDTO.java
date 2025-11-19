package br.unibh.filmeservice.dto;

import jakarta.validation.constraints.NotNull;

public record CurtirReviewDTO(
        @NotNull(message = "ID do user é obrigatório")
        String idUser,
        @NotNull(message = "ID do review é obrigatório")
        Boolean curtir
) {
}
