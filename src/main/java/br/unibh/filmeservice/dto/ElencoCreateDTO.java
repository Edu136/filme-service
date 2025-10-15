package br.unibh.filmeservice.dto;

import br.unibh.filmeservice.entity.ElencoFuncao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public record ElencoCreateDTO (
        @NotNull(message = "A função do elenco é obrigatória")
        ElencoFuncao funcao,
        String personagem,
        @NotNull(message = "O ID do filme é obrigatório")
        Long filmeId,
        @NotNull(message = "O ID da pessoa é obrigatório")
        Long pessoaId
){

    @AssertTrue(message = "O nome do personagem é obrigatório para a função ATOR.")
    @JsonIgnore
    public boolean isPersonagemValido() {
        if (funcao == null) {
            return true;
        }

        if (funcao.equals(ElencoFuncao.ATOR)) {
            return personagem != null && !personagem.trim().isEmpty();
        }
        return true;
    }
}
