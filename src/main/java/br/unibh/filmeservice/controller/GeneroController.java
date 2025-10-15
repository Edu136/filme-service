package br.unibh.filmeservice.controller;

import br.unibh.filmeservice.dto.GeneroCreateDTO;
import br.unibh.filmeservice.dto.GeneroResponseDTO;
import br.unibh.filmeservice.service.GeneroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/generos")
public class GeneroController {
    private final GeneroService generoService;

    public GeneroController(GeneroService generoService) {
        this.generoService = generoService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<GeneroResponseDTO>> listGeneros() {
        List<GeneroResponseDTO> generos = generoService.listAllGeneros();
        return ResponseEntity.ok(generos);
    }

    @PostMapping("/add")
    public ResponseEntity<GeneroResponseDTO> addGenero(@Valid @RequestBody GeneroCreateDTO request) {
        GeneroResponseDTO novoGenero = generoService.addGenero(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoGenero);
    }

}
