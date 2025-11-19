package br.unibh.filmeservice.service;

import br.unibh.filmeservice.dto.FavoriteCreateDTO;
import br.unibh.filmeservice.dto.FavoriteResponseDTO;
import br.unibh.filmeservice.entity.Favorite;
import br.unibh.filmeservice.entity.Filme;
import br.unibh.filmeservice.repository.FavoriteRepository;
import br.unibh.filmeservice.repository.FilmeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final FilmeService filmeService;
    private final FilmeRepository filmeRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, FilmeService filmeService, FilmeRepository filmeRepository) {
        this.filmeService = filmeService;
        this.favoriteRepository = favoriteRepository;
        this.filmeRepository = filmeRepository;
    }

    @Transactional
    public FavoriteResponseDTO addFavorite(FavoriteCreateDTO favoriteCreateDTO) {
        Favorite novoFavorite = new Favorite();

        if(favoriteRepository.existsByIdUserAndFilmeId(favoriteCreateDTO.idUser(), favoriteCreateDTO.idFilme())) {
            throw new IllegalArgumentException("Filme já está nos favoritos do usuário.");
        }

        novoFavorite.setIdUser(favoriteCreateDTO.idUser());
        novoFavorite.setFilme(filmeService.findById(favoriteCreateDTO.idFilme()));
        novoFavorite.setCreatedAt(LocalDateTime.now());
        favoriteRepository.save(novoFavorite);

        atualizarNumeroLikes(favoriteCreateDTO.idFilme());

        return new FavoriteResponseDTO(
                novoFavorite.getId(),
                novoFavorite.getIdUser(),
                novoFavorite.getFilme().getId(),
                novoFavorite.getFilme().getTitulo()
        );
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponseDTO> getFavoritesByUserId(String userId) {
        List<Favorite> favorite = favoriteRepository.findByIdUser(userId)
                .orElse(Collections.emptyList());

        return favorite.stream()
                .map(fav -> new FavoriteResponseDTO(
                        fav.getId(),
                        fav.getIdUser(),
                        fav.getFilme().getId(),
                        fav.getFilme().getTitulo()
                ))
                .toList();
    }

    @Transactional
    public void removeFavorite(String userId, Long movieId) {
        Favorite favorite = favoriteRepository.findByIdUserAndFilmeId(userId, movieId)
                .orElseThrow(() -> new IllegalArgumentException("Favorito não encontrado para o usuário e filme fornecidos."));
        favoriteRepository.delete(favorite);
        atualizarNumeroLikes(movieId);
    }

    @Transactional
    public void atualizarNumeroLikes(Long filmeId) {
        Integer numeroLikes = favoriteRepository.countByFilmeId(filmeId);

        Filme filme = filmeRepository.findByIdForUpdate(filmeId)
                .orElseThrow(() -> new IllegalArgumentException("Filme não encontrado com o ID: " + filmeId));

        filme.setNumeroLikes(numeroLikes);

    }

    @Transactional
    public boolean isFavorite(String userId, Long movieId) {
        return favoriteRepository.existsByIdUserAndFilmeId(userId, movieId);
    }

}
