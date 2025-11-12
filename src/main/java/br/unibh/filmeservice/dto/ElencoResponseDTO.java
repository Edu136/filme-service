package br.unibh.filmeservice.dto;

import java.util.List;

public record ElencoResponseDTO(
        Long id,
        String diretor,
        Long filmeId,
        List<AtorResponseDTO> atores
) {
}
