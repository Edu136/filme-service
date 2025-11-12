package br.unibh.filmeservice.repository;

import br.unibh.filmeservice.entity.Favorite;
import br.unibh.filmeservice.entity.Filme;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    Optional<List<Favorite>> findByIdUser(String idUser);

    boolean existsByIdUserAndFilmeId(String idUser, Long idFilme);

    Optional<Favorite> findByIdUserAndFilmeId(String idUser, Long idFilme);

    Integer countByFilmeId(Long filmeId);

}
