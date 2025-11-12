package br.unibh.filmeservice.dto;

public record AtorResponseDTO(
        Long id,
        String nome,
        String personagem,
        String fotoUrl
) {
}
