package br.unibh.filmeservice.service;

import br.unibh.filmeservice.dto.AtorCreateDTO;
import br.unibh.filmeservice.dto.FilmeCreateDTO;
import br.unibh.filmeservice.dto.FilmeResponseDTO;
import br.unibh.filmeservice.entity.*;
import br.unibh.filmeservice.mapper.FilmeMapper;
import br.unibh.filmeservice.repository.FilmeRepository;
import br.unibh.filmeservice.repository.GeneroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmeService {
    private final FilmeRepository filmeRepository;
    private final GeneroRepository generoRepository;
    private final FilmeMapper filmeMapper;

    public FilmeService(FilmeRepository filmeRepository, GeneroRepository generoRepository , FilmeMapper filmeMapper) {
        this.filmeRepository = filmeRepository;
        this.generoRepository = generoRepository;
        this.filmeMapper = filmeMapper;
    }


    @Transactional
    public FilmeResponseDTO createFilme(FilmeCreateDTO req, MultipartFile capaFile, MultipartFile posterFile) throws Exception {
        System.out.println("Criando filme com dados: " + req);

        Filme novoFilme = filmeMapper.toEntity(req);

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
        novoFilme.setGeneros(generosEncontrados);

        ImagensFilme imagens = new ImagensFilme();
        if (capaFile != null && !capaFile.isEmpty()) {
            imagens.setCapa(capaFile.getBytes());
        }
        if (posterFile != null && !posterFile.isEmpty()) {
            imagens.setPoster(posterFile.getBytes());
        }

        novoFilme.setImagens(imagens);

        if (req.elenco() != null) {
            Elenco elenco = new Elenco();
            elenco.setDiretor(req.elenco().diretor());

            if (req.elenco().atores() != null) {
                for (AtorCreateDTO atorDto : req.elenco().atores()) {
                    Ator ator = new Ator();
                    ator.setNome(atorDto.nome());
                    ator.setPersonagem(atorDto.personagem());
                    elenco.addAtor(ator);
                }
            }
            novoFilme.setElenco(elenco);
        }
        Filme filmeSalvo = filmeRepository.save(novoFilme);

        return filmeMapper.toResponseDTO(filmeSalvo);
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

        return filmeMapper.toResponseDTO(filme);
    }

    @Transactional(readOnly = true)
    public List<FilmeResponseDTO> getTop10FilmesMaisCurtidos() {
        List<Filme> filmes = filmeRepository.findTop10ByOrderByNumeroLikesDesc();
        return filmes.stream()
                .map(filmeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FilmeResponseDTO> getFilmesRecentes() {
        List<Filme> filmes = filmeRepository.findTop15ByOrderByDataLancamentoDesc();
        return filmes.stream()
                .map(filmeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<FilmeResponseDTO> getFilmesPorGenero(String nomeGenero, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("ratingMedia").descending());
        Page<Filme> filmesPage = filmeRepository.findByGeneros_Nome(nomeGenero, pageable);
        return filmesPage.map(filmeMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<FilmeResponseDTO> getFilmesAdicionadosPor(String userId, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataLancamento").descending());
        Page<Filme> filmesPage = filmeRepository.findByUserId(userId, pageable);
        return filmesPage.map(filmeMapper::toResponseDTO);
    }
}