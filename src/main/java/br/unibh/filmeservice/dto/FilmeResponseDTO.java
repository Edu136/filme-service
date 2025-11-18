package br.unibh.filmeservice.dto;

import java.time.LocalDate;
import java.util.Set;

public record FilmeResponseDTO (
    Long id,
    String titulo,
    String descricao,
    Integer duracaoMinutos,
    LocalDate dataLancamento,
    Set<Long> generos,
    String capaUrl,
    String posterUrl,
    Integer quantidadeCurtidas,
    Double mediaAvaliacoes,
    String userId,
    String diretor,
    Set<String> atores
){
}
