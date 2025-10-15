package br.unibh.filmeservice.controller;

import br.unibh.filmeservice.dto.ElencoCreateDTO;
import br.unibh.filmeservice.dto.ElencoResponseDTO;
import br.unibh.filmeservice.service.ElencoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/elencos")
public class ElencoController {
    private final ElencoService elencoService;

    public ElencoController(ElencoService elencoService) {
        this.elencoService = elencoService;
    }

    @PostMapping("/add")
    public ResponseEntity<ElencoResponseDTO> adicionarElenco(@RequestBody @Valid ElencoCreateDTO request) {
        ElencoResponseDTO novoElenco = elencoService.adicionarElenco(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoElenco);
    }
}
