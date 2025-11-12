package br.unibh.filmeservice.repository;

import br.unibh.filmeservice.entity.Elenco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElencoRepository extends JpaRepository<Elenco, Long> {
    Optional<Elenco> findByFilmeId(Long filmeId);
}
