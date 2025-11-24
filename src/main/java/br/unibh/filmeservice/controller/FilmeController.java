package br.unibh.filmeservice.controller;

import br.unibh.filmeservice.dto.FilmeCreateDTO;
import br.unibh.filmeservice.dto.FilmeResponseDTO;
import br.unibh.filmeservice.service.FilmeService;
import br.unibh.filmeservice.service.ImagensService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/filmes")
@Tag(name = "Filmes", description = "API para gerenciamento de filmes")
@SecurityRequirement(name = "bearerAuth")
public class FilmeController {
    private final FilmeService filmeService;
    private final ImagensService imagensService;
    private final ObjectMapper objectMapper;

    public FilmeController(FilmeService filmeService, ObjectMapper objectMapper, ImagensService imagesService) {
        this.objectMapper = objectMapper;
        this.filmeService = filmeService;
        this.imagensService = imagesService;
    }

    @Operation(
            summary = "Obter a capa do filme",
            description = "Retorna a imagem da capa do filme em formato JPEG"
    )
    @GetMapping(value = "/{id}/capa", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getCapa(@PathVariable Long id) {
        byte[] imagemBytes = imagensService.getCapaFilme(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imagemBytes);
    }
    @Operation(
            summary = "Obter o poster do filme",
            description = "Retorna a imagem do poster do filme em formato JPEG"
    )
    @GetMapping(value = "/{id}/poster", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getPoster(@PathVariable Long id) {
        byte[] imagemBytes = imagensService.getPosterFilme(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imagemBytes);
    }

    @Operation(
            summary = "Criar um novo filme",
            description = "Adiciona um novo filme ao sistema com todas as suas informações (título, descrição, gêneros, etc.)"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 1. INFORMA QUE É MULTIPART
    public ResponseEntity<FilmeResponseDTO> createFilme(
            @RequestPart("filme") String filmeDtoAsString ,
            @RequestPart("capa") MultipartFile capaFile,
            @RequestPart(value = "poster", required = false) MultipartFile posterFile
    ) throws Exception {

        FilmeCreateDTO req = objectMapper.readValue(filmeDtoAsString, FilmeCreateDTO.class);
        FilmeResponseDTO filmeResponse = filmeService.createFilme(req, capaFile, posterFile);
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

    @Operation(
            summary = "Obter os 10 filmes mais curtidos",
            description = "Retorna uma lista dos 10 filmes com o maior número de curtidas"
    )
    @GetMapping("/top10")
    public ResponseEntity<List<FilmeResponseDTO>> getTop10() {
        List<FilmeResponseDTO> filmes = filmeService.getTop10FilmesMaisCurtidos();
        return ResponseEntity.ok(filmes);
    }

    @Operation(
            summary = "Obter os filmes mais recentes",
            description = "Retorna uma lista dos filmes adicionados mais recentemente ao sistema"
    )
    @GetMapping("/recentes")
    public ResponseEntity<List<FilmeResponseDTO>> getRecentes() {
        List<FilmeResponseDTO> filmes = filmeService.getFilmesRecentes();
        return ResponseEntity.ok(filmes);
    }

    @Operation(
            summary = "Obter filmes por gênero",
            description = "Retorna uma lista paginada de filmes que pertencem a um gênero específico"
    )
    @GetMapping("/genero/{nomeGenero}")
    public ResponseEntity<Page<FilmeResponseDTO>> getPorGenero(
            @PathVariable String nomeGenero,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<FilmeResponseDTO> filmesPage = filmeService.getFilmesPorGenero(nomeGenero, page, size);
        return ResponseEntity.ok(filmesPage);
    }

    @GetMapping("/adicionados/{userId}")
    public ResponseEntity<Page<FilmeResponseDTO>> getFilmesAdicionadosPor(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<FilmeResponseDTO> filmesPages = filmeService.getFilmesAdicionadosPor(userId,page,size);
        return ResponseEntity.ok(filmesPages);
    }

    @PostMapping("/{idFilme}/like")
    public ResponseEntity<Void> likeFilme(
            @PathVariable Long idFilme,
            @RequestParam String idUser
    ) {
        filmeService.giveLikes(idFilme, idUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idFilme}/unlike")
    public ResponseEntity<Void> unlikeFilme(
            @PathVariable Long idFilme,
            @RequestParam String idUser
    ) {
        filmeService.undoLikes(idFilme, idUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{idFilme}/check-like")
    public ResponseEntity<Map<String, Boolean>> checkLike(
            @PathVariable Long idFilme,
            @RequestParam String idUser
    ) {
        boolean isLiked = filmeService.usuarioCurtiuFilme(idFilme, idUser);
        return ResponseEntity.ok(Map.of("isLiked", isLiked));
    }

}