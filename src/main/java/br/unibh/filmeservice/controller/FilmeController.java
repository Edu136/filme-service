package br.unibh.filmeservice.controller;

import br.unibh.filmeservice.dto.FilmeCreateDTO;
import br.unibh.filmeservice.dto.FilmeResponseDTO;
import br.unibh.filmeservice.service.FilmeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/filmes")
@Tag(name = "Filmes", description = "API para gerenciamento de filmes")
public class FilmeController {
    private final FilmeService filmeService;

    public FilmeController(FilmeService filmeService) {
        this.filmeService = filmeService;
    }

    @Operation(
            summary = "Criar um novo filme",
            description = "Adiciona um novo filme ao sistema com todas as suas informações (título, descrição, gêneros, etc.)"
    )
    @PostMapping
    public ResponseEntity<FilmeResponseDTO> createFilme(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do filme a ser criado",
                    required = true
            )
            @RequestBody @Valid FilmeCreateDTO req) {
        FilmeResponseDTO filmeResponse = filmeService.adicionarFilme(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(filmeResponse);
    }

    @Operation(
            summary = "Listar filmes com paginação",
            description = "Retorna uma lista paginada de filmes. Opcionalmente pode filtrar por usuário para mostrar apenas filmes criados por um usuário específico"
    )
    @GetMapping
    public ResponseEntity<Page<FilmeResponseDTO>> findFilmes(
            @Parameter(description = "Número da página (começa em 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de itens por página", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "ID do usuário para filtrar filmes (opcional)", example = "user123")
            @RequestParam(name = "userId", required = false) String userId) {

        Page<FilmeResponseDTO> filmesPage = filmeService.findFilmes(page, size, userId);
        return ResponseEntity.ok(filmesPage);
    }

    @Operation(
            summary = "Buscar filme por ID",
            description = "Retorna os detalhes completos de um filme específico através do seu ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<FilmeResponseDTO> findFilmeById(
            @Parameter(description = "ID do filme", required = true, example = "1")
            @PathVariable Long id) {
        FilmeResponseDTO filmeResponse = filmeService.getFilmeById(id);
        return ResponseEntity.ok(filmeResponse);
    }

    @Operation(
            summary = "Deletar um filme",
            description = "Remove um filme do sistema através do seu ID. Esta ação é irreversível"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilme(
            @Parameter(description = "ID do filme", required = true, example = "1")
            @PathVariable Long id) {
        filmeService.deleteFilme(id);
        return ResponseEntity.noContent().build();
    }
}