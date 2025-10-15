package br.unibh.filmeservice.service;

import br.unibh.filmeservice.dto.ImagemCreateDTO;
import br.unibh.filmeservice.dto.ImagemResponseDTO;
import br.unibh.filmeservice.entity.Filme;
import br.unibh.filmeservice.entity.Imagem;
import br.unibh.filmeservice.repository.ImagemRepository;
import org.springframework.stereotype.Service;

@Service
public class ImagemService {
    private final ImagemRepository imagemRepository;
    private final FilmeService filmeService;

    public ImagemService(ImagemRepository imagemRepository, FilmeService filmeService) {
        this.filmeService = filmeService;
        this.imagemRepository = imagemRepository;
    }

    public ImagemResponseDTO adicionarImagem(ImagemCreateDTO imagemCreateDTO) {
        Imagem novaImagem = new Imagem();
        novaImagem.setUrl(imagemCreateDTO.url());
        novaImagem.setImagemType(imagemCreateDTO.tipoImagem());

        Filme filme = filmeService.findById(imagemCreateDTO.filmeId());

        if(filme == null) {
            throw new RuntimeException("Filme com ID " + imagemCreateDTO.filmeId() + " não existe.");
        }

        novaImagem.setFilme(filme);

        imagemRepository.save(novaImagem);
        return new ImagemResponseDTO(
                novaImagem.getId(),
                novaImagem.getUrl(),
                novaImagem.getImagemType(),
                imagemCreateDTO.filmeId()
        );
    }
}
