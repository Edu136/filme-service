package br.unibh.filmeservice.service;

import br.unibh.filmeservice.dto.FilmeCreateDTO;
import br.unibh.filmeservice.dto.FilmeResponseDTO;
import br.unibh.filmeservice.entity.Filme;
import br.unibh.filmeservice.entity.Genero;
import br.unibh.filmeservice.repository.FilmeRepository;
import br.unibh.filmeservice.repository.GeneroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmeService {
    private final FilmeRepository filmeRepository;
    private final GeneroRepository generoRepository;

    public FilmeService(FilmeRepository filmeRepository, GeneroRepository generoRepository) {
        this.filmeRepository = filmeRepository;
        this.generoRepository = generoRepository;
    }


    @Transactional
    public FilmeResponseDTO adicionarFilme(FilmeCreateDTO req) {

        Set<Genero> generosEncontrados = new HashSet<>();
        Set<Long> generosId = req.generosId();

        if (generosId != null && !generosId.isEmpty()) {
            List<Genero> generosList = generoRepository.findAllById(generosId);

            if (generosList.size() != generosId.size()) {
                Set<Long> foundIds = generosList.stream().map(Genero::getId).collect(Collectors.toSet());
                Set<Long> missingIds = new HashSet<>(generosId);
                missingIds.removeAll(foundIds);

                throw new EntityNotFoundException("Gênero(s) com ID(s) " + missingIds + " não encontrado(s).");
            }

            generosEncontrados = new HashSet<>(generosList);
        }

        Filme novoFilme = new Filme();
        novoFilme.setTitulo(req.titulo());
        novoFilme.setDescricao(req.descricao());
        novoFilme.setDataLancamento(req.dataLancamento());
        novoFilme.setDuracaoMinutos(req.duracaoMinutos());
        novoFilme.setUrlCapa(req.capaUrl());
        novoFilme.setGeneros(generosEncontrados);
        novoFilme.setRatingMedia(0.0);
        novoFilme.setNumeroLikes(0);
        novoFilme.setUserId(req.idUser());

        Filme filmeSalvo = filmeRepository.save(novoFilme);

        return toResponseDTO(filmeSalvo);
    }


    @Transactional(readOnly = true)
    public Page<FilmeResponseDTO> findFilmes(int page, int size , String userId) {
        Pageable pageable = PageRequest.of(page, size);

        if (userId != null && !userId.isEmpty()) {
            return filmeRepository.findByUserId(userId, pageable).map(this::toResponseDTO);
        } else{
            return filmeRepository.findAll(pageable).map(this::toResponseDTO);
        }
    }

    @Transactional(readOnly = true)
    public FilmeResponseDTO getFilmeById(Long id) {
        Filme filme = findById(id);
        return toResponseDTO(filme);
    }

    @Transactional
    public void deleteFilme(Long id) {
        Filme filme = findById(id);
        filmeRepository.delete(filme);
    }


    @Transactional(readOnly = true)
    public Filme findById(Long id) {
        return filmeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filme com ID " + id + " não encontrado."));
    }


    private FilmeResponseDTO toResponseDTO(Filme filme) {
        Set<Long> idsGeneros = filme.getGeneros().stream()
                .map(Genero::getId)
                .collect(Collectors.toSet());

        return new FilmeResponseDTO(
                filme.getId(),
                filme.getTitulo(),
                filme.getDescricao(),
                filme.getDuracaoMinutos(),
                filme.getDataLancamento(),
                idsGeneros,
                filme.getUrlCapa(),
                filme.getNumeroLikes(),
                filme.getRatingMedia(),
                filme.getUserId()
        );
    }
}