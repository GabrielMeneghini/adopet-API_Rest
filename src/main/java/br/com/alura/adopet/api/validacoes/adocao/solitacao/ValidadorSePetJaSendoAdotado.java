package br.com.alura.adopet.api.validacoes.adocao.solitacao;

import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorSePetJaSendoAdotado implements ValidacaoSolitacaoAdocao {

    @Autowired
    private AdocaoRepository repository;

    @Override
    public void validar(Adocao adocao) {
        if(repository.petJaAguardandoAvaliacao(adocao.getPet().getId())) {
            throw new ValidacaoException("Pet já está aguardando avaliação para ser adotado!");
        }
    }


}
