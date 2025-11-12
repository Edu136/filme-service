package br.unibh.filmeservice.controller;

import br.unibh.filmeservice.dto.ElencoCreateDTO;
import br.unibh.filmeservice.dto.ElencoResponseDTO;
import br.unibh.filmeservice.service.ElencoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/elencos")
@Tag(name = "Elenco", description = "API para gerenciamento de elencos de filmes")
public class ElencoController {

    private final ElencoService elencoService;

    public ElencoController(ElencoService elencoService) {
        this.elencoService = elencoService;
    }

    @Operation(
            summary = "Criar um novo elenco para um filme",
            description = "Cria um novo elenco associado a um filme específico através do ID do filme"
    )
    @PostMapping("/filmes/{filmeId}")
    public ResponseEntity<ElencoResponseDTO> createElenco(
            @Parameter(description = "ID do filme", required = true)
            @PathVariable Long filmeId,
            @Valid @RequestBody ElencoCreateDTO elencoCreateDTO) {

        ElencoResponseDTO elencoCriado = elencoService.createElencoParaFilme(filmeId, elencoCreateDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/api/elencos/{id}")
                .buildAndExpand(elencoCriado.id()).toUri();

        return ResponseEntity.created(location).body(elencoCriado);
    }

    @Operation(
            summary = "Buscar elenco por ID",
            description = "Retorna os detalhes de um elenco específico através do seu ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ElencoResponseDTO> getElencoById(
            @Parameter(description = "ID do elenco", required = true)
            @PathVariable Long id) {
        ElencoResponseDTO elenco = elencoService.findElencoById(id);
        return ResponseEntity.ok(elenco);
    }

    @Operation(
            summary = "Buscar elenco por ID do filme",
            description = "Retorna o elenco associado a um filme específico"
    )
    @GetMapping("/api/filmes/{filmeId}/elenco")
    public ResponseEntity<ElencoResponseDTO> getElencoByFilmeId(
            @Parameter(description = "ID do filme", required = true)
            @PathVariable Long filmeId) {
        ElencoResponseDTO elenco = elencoService.findElencoByFilmeId(filmeId);
        return ResponseEntity.ok(elenco);
    }

    @Operation(
            summary = "Atualizar um elenco",
            description = "Atualiza os dados de um elenco existente"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ElencoResponseDTO> updateElenco(
            @Parameter(description = "ID do elenco", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ElencoCreateDTO elencoCreateDTO) {

        ElencoResponseDTO elencoAtualizado = elencoService.updateElenco(id, elencoCreateDTO);
        return ResponseEntity.ok(elencoAtualizado);
    }

    @Operation(
            summary = "Deletar um elenco",
            description = "Remove um elenco do sistema através do seu ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteElenco(
            @Parameter(description = "ID do elenco", required = true)
            @PathVariable Long id) {
        elencoService.deleteElenco(id);
        return ResponseEntity.noContent().build();
    }
}