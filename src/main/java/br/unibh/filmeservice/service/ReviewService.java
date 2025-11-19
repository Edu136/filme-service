package br.unibh.filmeservice.service;

import br.unibh.filmeservice.dto.CurtirReviewDTO;
import br.unibh.filmeservice.dto.ReviewCreateDTO;
import br.unibh.filmeservice.dto.ReviewResponseDTO;
import br.unibh.filmeservice.entity.Filme;
import br.unibh.filmeservice.entity.Review;
import br.unibh.filmeservice.repository.FilmeRepository;
import br.unibh.filmeservice.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    private final FilmeService filmeService;
    private final ReviewRepository reviewRepository;
    private final FilmeRepository filmeRepository;

    public ReviewService(FilmeService filmeService, ReviewRepository reviewRepository, FilmeRepository filmeRepository) {
        this.filmeService = filmeService;
        this.reviewRepository = reviewRepository;
        this.filmeRepository = filmeRepository;
    }

    @Transactional
    public ReviewResponseDTO addNewReview(ReviewCreateDTO req){
        Filme filme = filmeService.findById(req.idFilme());

        Review novoReview = new Review();

        novoReview.setRating(req.rating());
        novoReview.setComment(req.comment());
        novoReview.setIdUser(req.idUser());
        novoReview.setFilme(filme);
        novoReview.setQtdCurtidas(0);
        novoReview.setUsername(req.username());

        reviewRepository.save(novoReview);

        atualizarMediaDoFilme(req.idFilme());

        return new ReviewResponseDTO(
                novoReview.getId(),
                novoReview.getRating(),
                novoReview.getComment(),
                novoReview.getIdUser(),
                novoReview.getUsername(),
                novoReview.getFilme().getId(),
                novoReview.getFilme().getTitulo(),
                novoReview.getQtdCurtidas(),
                novoReview.getCriadoEm(),
                novoReview.getUsersIdCurtiram()
        );
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getReviewsByIdFilme(Long idFilme){
        List<Review> lista = reviewRepository.findByFilmeId(idFilme);
        return lista.stream().map(r -> new ReviewResponseDTO(
                r.getId(),
                r.getRating(),
                r.getComment(),
                r.getIdUser(),
                r.getUsername(),
                r.getFilme().getId(),
                r.getFilme().getTitulo(),
                r.getQtdCurtidas(),
                r.getCriadoEm(),
                r.getUsersIdCurtiram()
        )).toList();
    }

    @Transactional
    public void curtirReview(Long id, CurtirReviewDTO req) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review com ID " + id + " não encontrado."));

        List<String> usersCurtiram = review.getUsersIdCurtiram();
        if (usersCurtiram == null) {
            usersCurtiram = new ArrayList<>();
        }

        String userId = req.idUser();

        if (req.curtir()) {
            if (!usersCurtiram.contains(userId)) {
                usersCurtiram.add(userId);
                review.setQtdCurtidas(review.getQtdCurtidas() + 1);
            }
        } else {
            if (usersCurtiram.remove(userId)) {
                review.setQtdCurtidas(Math.max(0, review.getQtdCurtidas() - 1));
            }
        }

        review.setUsersIdCurtiram(usersCurtiram);
        reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review com ID " + id + " não encontrado."));
        reviewRepository.delete(review);
    }

    @Transactional
    public void atualizarMediaDoFilme (Long idFilme) {
        Double novaMedia = getAverageRating(idFilme);
        Filme filme = filmeService.findById(idFilme);
        filme.setRatingMedia(novaMedia);
        filmeRepository.save(filme);
    }

    public Double getAverageRating(Long idFilme) {
        List<Review> reviews = reviewRepository.findByFilmeId(idFilme);

        if (reviews.isEmpty()) {
            return 0.0;
        }

        Double sum = reviews.stream()
                .mapToDouble(Review::getRating)
                .sum();

        return sum / reviews.size();
    }
}
