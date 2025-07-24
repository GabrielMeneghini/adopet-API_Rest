package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dtos.AdocaoAprovacaoDto;
import br.com.alura.adopet.api.dtos.AdocaoReprovacaoDto;
import br.com.alura.adopet.api.dtos.AdocaoSolicitacaoDto;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.adocao.solitacao.ValidacaoSolitacaoAdocao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdocaoService {

    @Autowired
    private AdocaoRepository adocaoRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private List<ValidacaoSolitacaoAdocao> validacoes;

    public void solicitar(AdocaoSolicitacaoDto dto) {
        var tutor = tutorRepository.getReferenceById(dto.idTutor());
        var pet = petRepository.getReferenceById(dto.idPet());
        var adocao = new Adocao(tutor, pet, dto.motivo());
        tutor.getAdocoes().add(adocao);

        validacoes.forEach(v -> v.validar(adocao));

        adocaoRepository.save(adocao);

        emailService.enviarEmail(adocao.getPet().getAbrigo().getEmail(),
                "Solicitação de adoção",
                "Olá " + adocao.getPet().getAbrigo().getNome()
                        + "!\n\nUma solicitação de adoção foi registrada hoje para o pet: "
                        + adocao.getPet().getNome() + ". \nFavor avaliar para aprovação ou reprovação.");
    }

    public void aprovar(AdocaoAprovacaoDto adocaoAprovacaoDto) {
        var adocao = adocaoRepository.getReferenceById(adocaoAprovacaoDto.idAdocao());

        adocao.marcarComoAprovado();
        adocao.getPet().marcarComoAdotado();

        emailService.enviarEmail(adocao.getTutor().getEmail(),
                "Adoção aprovada",
                "Parabéns " +adocao.getTutor().getNome() +"!\n\nSua adoção do pet " +adocao.getPet().getNome() +", solicitada em " +adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +", foi aprovada.\nFavor entrar em contato com o abrigo " +adocao.getPet().getAbrigo().getNome() +" para agendar a busca do seu pet.");
    }

    public void reprovar(AdocaoReprovacaoDto adocaoReprovacaoDto) {
        var adocao = adocaoRepository.getReferenceById(adocaoReprovacaoDto.idAdocao());

        adocao.marcarComoReprovado(adocaoReprovacaoDto.justificativa());

        emailService.enviarEmail(adocao.getTutor().getEmail(),
                "Adoção reprovada",
                "Olá " +adocao.getTutor().getNome() +"!\n\nInfelizmente sua adoção do pet " +adocao.getPet().getNome()
                        + ", solicitada em " +adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                        + ", foi reprovada pelo abrigo " +adocao.getPet().getAbrigo().getNome() +" com a seguinte justificativa: "
                        + adocao.getJustificativaStatus());
    }

}

