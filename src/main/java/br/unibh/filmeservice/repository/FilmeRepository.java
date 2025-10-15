package br.unibh.filmeservice.repository;

import br.unibh.filmeservice.entity.Filme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmeRepository extends JpaRepository<Filme, Long> {
}
