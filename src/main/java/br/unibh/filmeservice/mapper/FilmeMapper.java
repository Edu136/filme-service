package br.unibh.filmeservice.mapper;

import br.unibh.filmeservice.dto.AtorDTO;
import br.unibh.filmeservice.dto.FilmeCreateDTO;
import br.unibh.filmeservice.dto.FilmeResponseDTO;
import br.unibh.filmeservice.entity.Ator;
import br.unibh.filmeservice.entity.Filme;
import br.unibh.filmeservice.entity.Genero;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface FilmeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numeroLikes", constant = "0")
    @Mapping(target = "ratingMedia", constant = "0.0")
    @Mapping(target = "userId", source = "idUser")
    @Mapping(target = "imagens", ignore = true)
    @Mapping(target = "generos", ignore = true)
    @Mapping(target = "elenco", ignore = true)
    Filme toEntity(FilmeCreateDTO dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "titulo", source = "titulo")
    @Mapping(target = "descricao", source = "descricao")
    @Mapping(target = "duracaoMinutos", source = "duracaoMinutos")
    @Mapping(target = "dataLancamento", source = "dataLancamento")
    @Mapping(target = "generos", source = "generos", qualifiedByName = "generosToIds")
    @Mapping(target = "quantidadeCurtidas", source = "numeroLikes")
    @Mapping(target = "mediaAvaliacoes", source = "ratingMedia")
    @Mapping(target = "capaUrl", source = "id", qualifiedByName = "buildCapaUrl")
    @Mapping(target = "posterUrl", source = "id", qualifiedByName = "buildPosterUrl")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "diretor", source = "elenco.diretor")
    @Mapping(target = "elenco", source = "elenco.atores", qualifiedByName = "mapElenco")
    FilmeResponseDTO toResponseDTO(Filme filme);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numeroLikes", ignore = true)
    @Mapping(target = "ratingMedia", ignore = true)
    @Mapping(target = "userId", source = "idUser")
    @Mapping(target = "generos", ignore = true)
    @Mapping(target = "elenco", ignore = true)
    @Mapping(target = "imagens", ignore = true)
    void updateEntityFromDTO(FilmeCreateDTO dto, @MappingTarget Filme filme);

    @Named("generosToIds")
    default Set<Long> generosToIds(Set<Genero> generos) {
        if (generos == null) {
            return Set.of();
        }
        return generos.stream()
                .map(Genero::getId)
                .collect(Collectors.toSet());
    }

    @Named("buildCapaUrl")
    default String buildCapaUrl(Long filmeId) {
        if (filmeId == null) {
            return null;
        }
        return "/api/filmes/" + filmeId + "/capa";
    }

    @Named("buildPosterUrl")
    default String buildPosterUrl(Long filmeId) {
        if (filmeId == null) {
            return null;
        }
        return "/api/filmes/" + filmeId + "/poster";
    }

    @Named("mapElenco")
    default List<AtorDTO> mapElenco(List<Ator> atores) {
        if (atores == null || atores.isEmpty()) {
            return Collections.emptyList();
        }
        return atores.stream()
                .map(ator -> new AtorDTO(ator.getNome(), ator.getPersonagem())) // Pega nome e papel
                .collect(Collectors.toList());
    }
}