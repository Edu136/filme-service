package br.unibh.filmeservice.dto;

import br.unibh.filmeservice.entity.Filme;

public record ReviewResponseDTO(
        Long id,
        Integer rating,
        String comment,
        String idUser,
        Long filmeId,
        String filmeTitle

) {
}
