package br.unibh.filmeservice.service;

import br.unibh.filmeservice.dto.AtorCreateDTO;
import br.unibh.filmeservice.dto.AtorResponseDTO;
import br.unibh.filmeservice.dto.ElencoCreateDTO;
import br.unibh.filmeservice.dto.ElencoResponseDTO;
import br.unibh.filmeservice.entity.Ator;
import br.unibh.filmeservice.entity.Elenco;
import br.unibh.filmeservice.entity.Filme;
import br.unibh.filmeservice.repository.ElencoRepository;
import br.unibh.filmeservice.repository.FilmeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElencoService {

    private final ElencoRepository elencoRepository;
    private final FilmeRepository filmeRepository;

    public ElencoService(ElencoRepository elencoRepository, FilmeRepository filmeRepository) {
        this.elencoRepository = elencoRepository;
        this.filmeRepository = filmeRepository;
    }

    @Transactional
    public ElencoResponseDTO createElencoParaFilme(Long filmeId, ElencoCreateDTO dto) {
        Filme filme = filmeRepository.findById(filmeId)
                .orElseThrow(() -> new EntityNotFoundException("Filme com ID " + filmeId + " não encontrado."));

        if (elencoRepository.findByFilmeId(filmeId).isPresent()) {
            throw new IllegalStateException("Filme com ID " + filmeId + " já possui um elenco.");
        }

        Elenco elenco = new Elenco();
        elenco.setDiretor(dto.diretor());
        elenco.setFilme(filme);

        if (dto.atores() != null) {
            List<Ator> atores = dto.atores().stream()
                    .map(atorDTO -> atorDtoToEntity(atorDTO, elenco))
                    .collect(Collectors.toList());
            elenco.setAtores(atores);
        }

        Elenco elencoSalvo = elencoRepository.save(elenco);

        filme.setElenco(elencoSalvo);

        return toResponseDTO(elencoSalvo);
    }

    @Transactional(readOnly = true)
    public ElencoResponseDTO findElencoById(Long id) {
        Elenco elenco = elencoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Elenco com ID " + id + " não encontrado."));
        return toResponseDTO(elenco);
    }

    @Transactional(readOnly = true)
    public ElencoResponseDTO findElencoByFilmeId(Long filmeId) {
        Elenco elenco = elencoRepository.findByFilmeId(filmeId)
                .orElseThrow(() -> new EntityNotFoundException("Elenco para o filme com ID " + filmeId + " não encontrado."));
        return toResponseDTO(elenco);
    }

    @Transactional
    public ElencoResponseDTO updateElenco(Long id, ElencoCreateDTO dto) {
        Elenco elencoExistente = elencoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Elenco com ID " + id + " não encontrado."));

        elencoExistente.setDiretor(dto.diretor());

        elencoExistente.getAtores().clear();

        if (dto.atores() != null) {
            List<Ator> novosAtores = dto.atores().stream()
                    .map(atorDTO -> atorDtoToEntity(atorDTO, elencoExistente))
                    .collect(Collectors.toList());
            elencoExistente.getAtores().addAll(novosAtores);
        }

        Elenco elencoAtualizado = elencoRepository.save(elencoExistente);
        return toResponseDTO(elencoAtualizado);
    }

    @Transactional
    public void deleteElenco(Long id) {
        Elenco elenco = elencoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Elenco com ID " + id + " não encontrado."));

        Filme filme = elenco.getFilme();
        if (filme != null) {
            filme.setElenco(null);
        }

        elencoRepository.delete(elenco);
    }

    private ElencoResponseDTO toResponseDTO(Elenco elenco) {
        List<AtorResponseDTO> atorDTOs = elenco.getAtores().stream()
                .map(ator -> new AtorResponseDTO(
                        ator.getId(),
                        ator.getNome(),
                        ator.getPersonagem(),
                        ator.getFotoUrl()
                ))
                .collect(Collectors.toList());

        return new ElencoResponseDTO(
                elenco.getId(),
                elenco.getDiretor(),
                elenco.getFilme() != null ? elenco.getFilme().getId() : null,
                atorDTOs
        );
    }

    private Ator atorDtoToEntity(AtorCreateDTO dto, Elenco elenco) {
        Ator ator = new Ator();
        ator.setNome(dto.nome());
        ator.setPersonagem(dto.personagem());
        ator.setFotoUrl(dto.fotoUrl());
        ator.setElenco(elenco);
        return ator;
    }
}