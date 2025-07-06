package br.com.alura.adopet.api.validacoes.abrigo.cadastro;

import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorDadosUnicosAbrigo implements ValidacaoCadastroAbrigo {

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Override
    public void validar(Abrigo abrigo) {

        if (abrigoRepository.existsByNome(abrigo.getNome())) {
            throw new ValidacaoException("Nome já cadastrado para outro abrigo!");
        }

        if (abrigoRepository.existsByEmail(abrigo.getEmail())) {
            throw new ValidacaoException("Email já cadastrado para outro abrigo!");
        }

        if (abrigoRepository.existsByTelefone(abrigo.getTelefone())) {
            throw new ValidacaoException("Telefone já cadastrado para outro abrigo!");
        }

    }
}
