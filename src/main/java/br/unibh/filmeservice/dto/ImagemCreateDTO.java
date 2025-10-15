package br.unibh.filmeservice.dto;

import br.unibh.filmeservice.entity.ImagemType;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record ImagemCreateDTO(
        @NotNull(message = "A URL da imagem não pode ser nula")
        @URL(message = "A URL da imagem deve ser válida")
        String url,
        @NotNull(message = "O tipo da imagem não pode ser nulo")
        ImagemType tipoImagem,
        @NotNull(message = "O ID do filme não pode ser nulo")
        Long filmeId
) {
}
