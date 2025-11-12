package br.unibh.filmeservice.controller;

import br.unibh.filmeservice.dto.GeneroCreateDTO;
import br.unibh.filmeservice.dto.GeneroResponseDTO;
import br.unibh.filmeservice.service.GeneroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/generos")
@Tag(name = "Gêneros", description = "API para gerenciamento de gêneros de filmes")
public class GeneroController {
    private final GeneroService generoService;

    public GeneroController(GeneroService generoService) {
        this.generoService = generoService;
    }

    @Operation(
            summary = "Listar todos os gêneros",
            description = "Retorna uma lista completa de todos os gêneros cadastrados no sistema"
    )
    @GetMapping("/list")
    public ResponseEntity<List<GeneroResponseDTO>> listGeneros() {
        List<GeneroResponseDTO> generos = generoService.listAllGeneros();
        return ResponseEntity.ok(generos);
    }

    @Operation(
            summary = "Adicionar um novo gênero",
            description = "Cria um novo gênero no sistema (ex: Ação, Drama, Comédia, Terror, etc.)"
    )
    @PostMapping("/add")
    public ResponseEntity<GeneroResponseDTO> addGenero(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do gênero a ser criado",
                    required = true
            )
            @Valid @RequestBody GeneroCreateDTO request) {
        GeneroResponseDTO novoGenero = generoService.addGenero(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoGenero);
    }

    @Operation(
            summary = "Deletar um gênero",
            description = "Remove um gênero do sistema através do seu ID. Atenção: pode falhar se houver filmes associados a este gênero"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGenero(
            @Parameter(description = "ID do gênero", required = true, example = "1")
            @PathVariable Long id) {
        generoService.deleteGenero(id);
        return ResponseEntity.noContent().build();
    }
}