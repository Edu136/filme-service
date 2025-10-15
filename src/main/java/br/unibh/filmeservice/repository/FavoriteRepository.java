package br.unibh.filmeservice.repository;

import br.unibh.filmeservice.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
}
