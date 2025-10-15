package br.unibh.filmeservice.service;

import br.unibh.filmeservice.dto.PessoaCreateDTO;
import br.unibh.filmeservice.entity.Pessoa;
import br.unibh.filmeservice.repository.PessoaRepository;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {
    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public PessoaCreateDTO adicionarPessoa(PessoaCreateDTO pessoaCreateDTO) {
        Pessoa novaPessoa = new Pessoa();
        novaPessoa.setNome(pessoaCreateDTO.nome());
        pessoaRepository.save(novaPessoa);
        return new PessoaCreateDTO(novaPessoa.getNome());
    }

    public Pessoa findById(Long pessoaId) {
        return pessoaRepository.findById(pessoaId).orElseThrow(() -> new RuntimeException("Pessoa com ID " + pessoaId + " não existe."));
    }


}
