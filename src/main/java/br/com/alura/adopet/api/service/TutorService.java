package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dtos.TutorAtualizacaoDto;
import br.com.alura.adopet.api.dtos.TutorCadastroDto;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.tutor.cadastro.ValidacaoCadastroTutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorService {

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private List<ValidacaoCadastroTutor> validacoes;

    public void cadastrarTutor(TutorCadastroDto dto) {
        var tutor = new Tutor(dto);

        validacoes.forEach(v -> v.validar(tutor));

        tutorRepository.save(tutor);
    }

    public void atualizarTutor(TutorAtualizacaoDto dto) {
        var tutor = tutorRepository.getReferenceById(dto.id());

        if(dto.nome()!=null) {
            tutor.setNome(dto.nome());
        }
        if(dto.email()!=null) {
            tutor.setEmail(dto.email());
        }
        if(dto.telefone()!=null) {
            tutor.setTelefone(dto.telefone());
        }

        tutorRepository.save(tutor);
    }

}
