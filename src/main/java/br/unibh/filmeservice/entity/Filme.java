package br.unibh.filmeservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDate dataLancamento;
    private Integer duracaoMinutos;
    private Double ratingMedia;
    private Integer numeroLikes;
    private String urlCapa;
    private String userId;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "filme_genero",
            joinColumns = @JoinColumn(name = "filme_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_id")
    )
    private Set<Genero> generos = new HashSet<>();

    @OneToOne(mappedBy = "filme", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Elenco elenco;
}