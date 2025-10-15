package br.unibh.filmeservice.service;

import br.unibh.filmeservice.dto.FilmeCreateDTO;
import br.unibh.filmeservice.dto.FilmeResponseDTO;
import br.unibh.filmeservice.entity.Filme;
import br.unibh.filmeservice.entity.Genero;
import br.unibh.filmeservice.repository.FilmeRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class FilmeService {
    private final FilmeRepository filmeRepository;
    private final GeneroService generoService;

    public FilmeService(FilmeRepository filmeRepository, GeneroService generoService) {
        this.filmeRepository = filmeRepository;
        this.generoService = generoService;
    }

    public FilmeResponseDTO adicionarFilme(FilmeCreateDTO req){
        Filme novoFilme = new Filme();
        novoFilme.setTitulo(req.titulo());
        novoFilme.setDescricao(req.descricao());
        novoFilme.setDataLancamento(req.dataLancamento());
        novoFilme.setDuracaoMinutos(req.duracaoMinutos());

        Set<Long> generosId = req.generosId();
        Set<Genero> generosValidos = new HashSet<>();
        Set<Long> listIdsValidos = new HashSet<>();

        for (Long generoId : generosId) {
            if (generoService.findById(generoId) != null) {
                generosValidos.add(generoService.findById(generoId));
                listIdsValidos.add(generoId);
            } else {
                throw new RuntimeException("Gênero com ID " + generoId + " não existe.");
            }
        }
        novoFilme.setGeneros(generosValidos);
        filmeRepository.save(novoFilme);
        return new FilmeResponseDTO(
                novoFilme.getId(),
                novoFilme.getTitulo(),
                novoFilme.getDescricao(),
                novoFilme.getDuracaoMinutos(),
                novoFilme.getDataLancamento(),
                listIdsValidos
        );

    }

    public Filme findById(Long id){
        return filmeRepository.findById(id).orElse(null);
    }

}
