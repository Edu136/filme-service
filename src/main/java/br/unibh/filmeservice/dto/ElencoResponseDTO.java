package br.unibh.filmeservice.dto;

import br.unibh.filmeservice.entity.ElencoFuncao;

public record ElencoResponseDTO(
        Long id,
        String personagem,
        ElencoFuncao funcao,
        Long filmeId,
        Long pessoaId
) {
}
