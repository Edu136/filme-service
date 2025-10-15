package br.unibh.filmeservice.repository;

import br.unibh.filmeservice.entity.Elenco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElencoRepository extends JpaRepository<Elenco, Long> {
}
