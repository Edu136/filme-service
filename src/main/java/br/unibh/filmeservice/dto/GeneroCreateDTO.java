package br.unibh.filmeservice.dto;

import jakarta.validation.constraints.NotNull;

public record GeneroCreateDTO(
    @NotNull(message = "O genero não pode ser nulo.")
    String genero
) {
}
