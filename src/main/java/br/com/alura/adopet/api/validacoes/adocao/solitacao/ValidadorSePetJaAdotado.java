package br.com.alura.adopet.api.validacoes.adocao.solitacao;

import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.validacoes.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class ValidadorSePetJaAdotado implements ValidacaoSolitacaoAdocao {

    @Override
    public void validar(Adocao adocao) {
        if(adocao.getPet().getAdotado()) {
            throw new ValidacaoException("Pet já foi adotado!");
        }
    }

}
