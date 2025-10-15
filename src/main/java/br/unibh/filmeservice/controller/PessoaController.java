package br.unibh.filmeservice.controller;

import br.unibh.filmeservice.dto.PessoaCreateDTO;
import br.unibh.filmeservice.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {
    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping("/add")
    public ResponseEntity<PessoaCreateDTO> adicionarPessoa(@RequestBody @Valid PessoaCreateDTO pessoaCreateDTO) {
        PessoaCreateDTO pessoaSalva = pessoaService.adicionarPessoa(pessoaCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }
}
