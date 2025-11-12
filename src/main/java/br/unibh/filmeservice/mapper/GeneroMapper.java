package br.unibh.filmeservice.mapper;

import br.unibh.filmeservice.dto.GeneroCreateDTO;
import br.unibh.filmeservice.dto.GeneroResponseDTO;
import br.unibh.filmeservice.entity.Genero;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        imports = {})
public interface GeneroMapper {
    // Genero.nome → GeneroResponseDTO.genero
    @Mapping(target = "genero", source = "nome")
    GeneroResponseDTO toResponseDto(Genero genero);

    // GeneroCreateDTO.genero → Genero.nome
    @Mapping(target = "nome", source = "genero")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "filmes", ignore = true)
    Genero toEntity(GeneroCreateDTO dto);

}
