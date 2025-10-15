package br.unibh.filmeservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Imagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;

    @Enumerated(EnumType.STRING)
    private ImagemType imagemType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filme_id" , nullable = false)
    private Filme filme;
}
