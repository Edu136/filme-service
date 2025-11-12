package br.unibh.filmeservice.controller;

import br.unibh.filmeservice.dto.FavoriteCreateDTO;
import br.unibh.filmeservice.dto.FavoriteResponseDTO;
import br.unibh.filmeservice.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@Tag(name = "Favoritos", description = "API para gerenciamento de filmes favoritos dos usuários")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @Operation(
            summary = "Adicionar filme aos favoritos",
            description = "Adiciona um filme à lista de favoritos de um usuário"
    )
    @PostMapping("/add")
    public ResponseEntity<FavoriteResponseDTO> addFavorite(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para adicionar filme aos favoritos",
                    required = true
            )
            @RequestBody @Valid FavoriteCreateDTO favoriteCreateDTO) {
        FavoriteResponseDTO response = favoriteService.addFavorite(favoriteCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Buscar favoritos por ID do usuário",
            description = "Retorna a lista de todos os filmes favoritos de um usuário específico"
    )
    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteResponseDTO>> getFavoritesByUserId(
            @Parameter(description = "ID do usuário", required = true, example = "user123")
            @PathVariable String userId) {
        List<FavoriteResponseDTO> response = favoriteService.getFavoritesByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Remover filme dos favoritos",
            description = "Remove um filme específico da lista de favoritos de um usuário"
    )
    @DeleteMapping("/remove/{userId}/{movieId}")
    public ResponseEntity<Void> removeFavorite(
            @Parameter(description = "ID do usuário", required = true, example = "user123")
            @PathVariable String userId,
            @Parameter(description = "ID do filme", required = true, example = "1")
            @PathVariable Long movieId) {
        favoriteService.removeFavorite(userId, movieId);
        return ResponseEntity.noContent().build();
    }
}