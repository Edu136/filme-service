package br.unibh.filmeservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.Set;

public record FilmeCreateDTO(
    @NotNull(message = "O título do filme não pode ser nulo")
    String titulo,
    @NotNull(message = "A descrição do filme não pode ser nula")
    String descricao,
    @NotNull(message = "A data de lançamento não pode ser nula")
    LocalDate dataLancamento,
    @NotNull(message = "A duração em minutos não pode ser nula")
    @Positive(message = "A duração em minutos deve ser um valor positivo")
    Integer duracaoMinutos,
    @NotNull(message = "O filme precisa de pelo menos um gênero")
    Set<Long> generosId
) {
}
