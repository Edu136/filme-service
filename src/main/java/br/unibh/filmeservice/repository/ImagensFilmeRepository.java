package br.unibh.filmeservice.repository;

import br.unibh.filmeservice.entity.ImagensFilme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagensFilmeRepository extends JpaRepository<ImagensFilme, Long> {
}