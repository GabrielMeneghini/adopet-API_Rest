package br.com.alura.adopet.api.validacoes.tutor.cadastro;

import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorDadosUnicosTutor implements ValidacaoCadastroTutor {

    @Autowired
    private TutorRepository tutorRepository;

    @Override
    public void validar(Tutor tutor) {
        if(tutorRepository.existsByEmail(tutor.getEmail())) {
            throw new ValidacaoException("Email já cadastrado para outro tutor!");
        }

        if(tutorRepository.existsByTelefone(tutor.getTelefone())) {
            throw new ValidacaoException("Telefone já cadastrado para outro tutor!");
        }
    }
}
