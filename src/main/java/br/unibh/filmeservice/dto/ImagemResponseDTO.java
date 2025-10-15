package br.unibh.filmeservice.dto;

import br.unibh.filmeservice.entity.ImagemType;

public record ImagemResponseDTO(
        Long id,
        String url,
        ImagemType tipoImagem,
        Long filmeId
) {
}
