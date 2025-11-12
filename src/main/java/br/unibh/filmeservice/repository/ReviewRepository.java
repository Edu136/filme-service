package br.unibh.filmeservice.repository;

import br.unibh.filmeservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByFilmeId(Long filmeId);
}
