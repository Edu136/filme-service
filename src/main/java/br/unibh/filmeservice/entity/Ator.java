package br.unibh.filmeservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String personagem;
    private String fotoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "elenco_id", nullable = false)
    private Elenco elenco;
}