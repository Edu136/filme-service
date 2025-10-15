package br.unibh.filmeservice.repository;

import br.unibh.filmeservice.entity.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagemRepository extends JpaRepository<Imagem, Long> {
}
