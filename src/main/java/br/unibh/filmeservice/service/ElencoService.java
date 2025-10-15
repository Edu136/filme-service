package br.unibh.filmeservice.service;

import br.unibh.filmeservice.dto.ElencoCreateDTO;
import br.unibh.filmeservice.dto.ElencoResponseDTO;
import br.unibh.filmeservice.entity.ElencoFuncao;
import br.unibh.filmeservice.entity.Elenco;
import br.unibh.filmeservice.entity.Filme;
import br.unibh.filmeservice.entity.Pessoa;
import br.unibh.filmeservice.repository.ElencoRepository;
import org.springframework.stereotype.Service;

@Service
public class ElencoService {
    private final ElencoRepository elencoRepository;
    private final FilmeService filmeService;
    private final PessoaService pessoaService;

    public ElencoService(ElencoRepository elencoRepository, FilmeService filmeService, PessoaService pessoaService) {
        this.filmeService = filmeService;
        this.pessoaService = pessoaService;
        this.elencoRepository = elencoRepository;
    }

    public ElencoResponseDTO adicionarElenco(ElencoCreateDTO request) {
        Elenco novoElenco = new Elenco();
        ElencoFuncao funcao = request.funcao();

        novoElenco.setFuncao(funcao);

        if(funcao == ElencoFuncao.ATOR) {
            novoElenco.setPersonagem(request.personagem());
        } else {
            novoElenco.setPersonagem(null);
        }

        Filme filme = filmeService.findById(request.filmeId());
        Pessoa pessoa = pessoaService.findById(request.pessoaId());

        novoElenco.setFilme(filme);
        novoElenco.setPessoa(pessoa);

        elencoRepository.save(novoElenco);

        return new ElencoResponseDTO(
                novoElenco.getId(),
                novoElenco.getPersonagem(),
                novoElenco.getFuncao(),
                novoElenco.getFilme().getId(),
                novoElenco.getPessoa().getId()
        );
    }
}
