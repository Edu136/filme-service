package br.unibh.filmeservice.dto;


public record FavoriteResponseDTO(
        Long favoriteId,
        String userId,
        Long filmeId,
        String filmeTitulo
) {}