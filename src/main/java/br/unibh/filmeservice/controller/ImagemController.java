package br.unibh.filmeservice.controller;

import br.unibh.filmeservice.dto.ImagemCreateDTO;
import br.unibh.filmeservice.dto.ImagemResponseDTO;
import br.unibh.filmeservice.service.ImagemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/imagens")
public class ImagemController {
    private final ImagemService imagemService;

    public ImagemController(ImagemService imagemService) {
        this.imagemService = imagemService;
    }

    @PostMapping("/add")
    public ResponseEntity<ImagemResponseDTO> adicionarImagem(@RequestBody @Valid ImagemCreateDTO request) {
        ImagemResponseDTO imagemResponseDTO = imagemService.adicionarImagem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(imagemResponseDTO);
    }
}
