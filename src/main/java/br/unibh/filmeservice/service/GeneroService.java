package br.unibh.filmeservice.service;

import br.unibh.filmeservice.dto.GeneroCreateDTO;
import br.unibh.filmeservice.dto.GeneroResponseDTO;
import br.unibh.filmeservice.entity.Genero;
import br.unibh.filmeservice.mapper.GeneroMapper;
import br.unibh.filmeservice.repository.GeneroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneroService {
    private final GeneroRepository generoRepository;
    private final GeneroMapper generoMapper;

    public GeneroService(GeneroRepository generoRepository, GeneroMapper generoMapper) {
        this.generoMapper = generoMapper;
        this.generoRepository = generoRepository;
    }

    public List<GeneroResponseDTO> listAllGeneros() {
        List<Genero> generos = generoRepository.findAll();
        return generos
                .stream()
                .map(generoMapper::toResponseDto)
                .toList();
    }

    public GeneroResponseDTO addGenero(GeneroCreateDTO request) {
        Genero novoGenero = generoMapper.toEntity(request);
        Genero generoCriado = generoRepository.save(novoGenero);
        return generoMapper.toResponseDto(generoCriado);
    }

    public void deleteGenero(Long id) {
        if (!generoRepository.existsById(id)) {
            throw new RuntimeException("Gênero com ID " + id + " não encontrado");
        }
        generoRepository.deleteById(id);
    }
}
