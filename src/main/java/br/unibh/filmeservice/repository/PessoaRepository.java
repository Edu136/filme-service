package br.unibh.filmeservice.repository;

import br.unibh.filmeservice.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
}
