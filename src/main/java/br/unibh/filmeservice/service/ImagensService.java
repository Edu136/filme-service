package br.unibh.filmeservice.service;

import br.unibh.filmeservice.entity.ImagensFilme;
import br.unibh.filmeservice.repository.ImagensFilmeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImagensService {
    private final ImagensFilmeRepository imagensFilmeRepository;

    public ImagensService(ImagensFilmeRepository imagensFilmeRepository) {
        this.imagensFilmeRepository = imagensFilmeRepository;
    }

    @Transactional(readOnly = true)
    public byte[] getCapaFilme(Long filmeId) {
        ImagensFilme imagens = imagensFilmeRepository.findById(filmeId)
                .orElseThrow(() -> new RuntimeException("Imagem não encontrada para o id: " + filmeId));

        if (imagens.getCapa() == null) {
            throw new RuntimeException("Capa não disponível para o filme id: " + filmeId);
        }

        return imagens.getCapa();
    }

    @Transactional(readOnly = true)
    public byte[] getPosterFilme(Long filmeId) {
        ImagensFilme imagens = imagensFilmeRepository.findById(filmeId)
                .orElseThrow(() -> new RuntimeException("Imagem não encontrada para o id: " + filmeId));

        if (imagens.getPoster() == null) {
            throw new RuntimeException("Poster não disponível para o filme id: " + filmeId);
        }

        return imagens.getPoster();
    }
}
