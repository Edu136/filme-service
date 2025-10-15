package br.unibh.filmeservice.repository;

import br.unibh.filmeservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
