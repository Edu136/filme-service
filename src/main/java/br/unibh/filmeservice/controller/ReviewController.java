package br.unibh.filmeservice.controller;

import br.unibh.filmeservice.dto.CurtirReviewDTO;
import br.unibh.filmeservice.dto.ReviewCreateDTO;
import br.unibh.filmeservice.dto.ReviewResponseDTO;
import br.unibh.filmeservice.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Avaliações", description = "API para gerenciamento de avaliações (reviews) de filmes")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(
            summary = "Adicionar uma nova avaliação",
            description = "Cria uma nova avaliação para um filme, incluindo nota e comentário do usuário"
    )
    @PostMapping("/add")
    public ResponseEntity<ReviewResponseDTO> addNewReview(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da avaliação a ser criada",
                    required = true
            )
            @RequestBody @Valid ReviewCreateDTO req) {
        ReviewResponseDTO response = reviewService.addNewReview(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Buscar avaliações por ID do filme",
            description = "Retorna todas as avaliações feitas para um filme específico"
    )
    @GetMapping("/{idFilme}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByMovieId(
            @Parameter(description = "ID do filme", required = true, example = "1")
            @PathVariable Long idFilme) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByIdFilme(idFilme);
        return ResponseEntity.ok(reviews);
    }

    @Operation(
            summary = "Deletar uma avaliação",
            description = "Remove uma avaliação do sistema através do seu ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "ID da avaliação", required = true, example = "1")
            @PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Adicionar curtida a uma avaliação",
            description = "Adiciona uma curtida a uma avaliação específica"
    )
    @PostMapping("/{id}/curtir")
    public ResponseEntity<Void> adicionarCurtida(@PathVariable Long id, @Valid @RequestBody CurtirReviewDTO req){
        reviewService.curtirReview(id, req);
        return ResponseEntity.ok().build();
    }
}