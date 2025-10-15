package br.unibh.filmeservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor

@Table(name = "favoritos" ,  uniqueConstraints = {
        @UniqueConstraint(columnNames = "idUser, id_filme")
})

public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUser;
    private LocalDateTime createdAt;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_filme", nullable = false)
    private Filme filme;
}
