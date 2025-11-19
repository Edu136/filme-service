package br.unibh.filmeservice.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewResponseDTO(
        Long id,
        Integer rating,
        String comment,
        String idUser,
        String username,
        Long filmeId,
        String filmeTitle,
        Integer qtdCurtidas,
        LocalDateTime criadoEm,
        List<String> usuariosCurtiram
) {
}
