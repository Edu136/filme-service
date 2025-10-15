package br.unibh.filmeservice.dto;

import jakarta.validation.constraints.NotNull;

public record PessoaCreateDTO(
        @NotNull(message = "O nome não pode ser nulo")
        String nome
) {
}
