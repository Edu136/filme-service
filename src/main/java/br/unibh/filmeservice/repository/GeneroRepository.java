package br.unibh.filmeservice.repository;

import br.unibh.filmeservice.entity.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneroRepository extends JpaRepository<Genero, Long> {
}
