package br.unibh.filmeservice.controller;

import br.unibh.filmeservice.dto.FilmeCreateDTO;
import br.unibh.filmeservice.dto.FilmeResponseDTO;
import br.unibh.filmeservice.service.FilmeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/filmes")
public class FilmeController {
    private final FilmeService filmeService;

    public FilmeController(FilmeService filmeService) {
        this.filmeService = filmeService;
    }

    @PostMapping("/add")
    public ResponseEntity<FilmeResponseDTO> adicionarFilme(@RequestBody @Valid FilmeCreateDTO req) {
        FilmeResponseDTO filmeResponse = filmeService.adicionarFilme(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(filmeResponse);
    }
}
