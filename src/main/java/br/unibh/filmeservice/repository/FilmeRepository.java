package br.unibh.filmeservice.repository;

import br.unibh.filmeservice.entity.Filme;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface FilmeRepository extends JpaRepository<Filme, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT f FROM Filme f WHERE f.id = :filmeId")
    Optional<Filme> findByIdForUpdate(Long filmeId);

    Page<Filme> findByUserId(String userId, Pageable pageable);
}
