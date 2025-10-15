package br.unibh.filmeservice.service;

import br.unibh.filmeservice.dto.GeneroCreateDTO;
import br.unibh.filmeservice.dto.GeneroResponseDTO;
import br.unibh.filmeservice.entity.Genero;
import br.unibh.filmeservice.repository.GeneroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneroService {
    private final GeneroRepository generoRepository;

    public GeneroService(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    public List<GeneroResponseDTO> listAllGeneros() {
        List<Genero> generos = generoRepository.findAll();
        return generos
                .stream()
                .map(genero -> new GeneroResponseDTO(genero.getId(), genero.getNome()))
                .toList();
    }

    public GeneroResponseDTO addGenero(GeneroCreateDTO request) {
        Genero novoGenero = new Genero();
        novoGenero.setNome(request.genero());
        generoRepository.save(novoGenero);
        return new GeneroResponseDTO(novoGenero.getId(), novoGenero.getNome());
    }

    public Genero findById(Long id) {
        return generoRepository.findById(id).orElse(null);
    }
}
