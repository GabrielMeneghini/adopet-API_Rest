package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dtos.AdocaoSolicitacaoDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.adocao.solitacao.ValidacaoSolitacaoAdocao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {

    private AdocaoSolicitacaoDto adocaoSolicitacaoDto;

    private Adocao adocao;

    @InjectMocks
    private AdocaoService adocaoService;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private Tutor tutor;

    @Mock
    private Pet pet;

    @Mock
    private Abrigo abrigo;

    @Captor
    private ArgumentCaptor<Adocao> adocaoArgumentCaptor;

    @Spy
    private List<ValidacaoSolitacaoAdocao> validacoes = new ArrayList<>();
        @Mock
        private ValidacaoSolitacaoAdocao validador1;
        @Mock
        private ValidacaoSolitacaoAdocao validador2;
        @Mock
        private ValidacaoSolitacaoAdocao validador3;
        @Mock
        private ValidacaoSolitacaoAdocao validador4;

    @Test
    @DisplayName("Deve salvar adoção se dados forem corretos")
    void solicitarCenario01() {
        //Arrange
        this.adocaoSolicitacaoDto = new AdocaoSolicitacaoDto(10L, 20L, "Motivo teste");
        BDDMockito.given(tutorRepository.getReferenceById(adocaoSolicitacaoDto.idTutor())).willReturn(tutor);
        BDDMockito.given(petRepository.getReferenceById(adocaoSolicitacaoDto.idPet())).willReturn(pet);
        BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);

        //Act
        adocaoService.solicitar(adocaoSolicitacaoDto);

        //Assert
        BDDMockito.then(adocaoRepository).should().save(adocaoArgumentCaptor.capture());
        var adocaoSalva = adocaoArgumentCaptor.getValue();
        Assertions.assertEquals(tutor, adocaoSalva.getTutor());
        Assertions.assertEquals(pet, adocaoSalva.getPet());
        Assertions.assertEquals(adocaoSolicitacaoDto.motivo(), adocaoSalva.getMotivo());
    }

    @Test
    @DisplayName("Deve chamar validadores de adoção ao solicitar")
    void solicitarCenario02() {
        //Arrange
        this.adocaoSolicitacaoDto = new AdocaoSolicitacaoDto(10L, 20L, "Motivo teste");
        BDDMockito.given(tutorRepository.getReferenceById(adocaoSolicitacaoDto.idTutor())).willReturn(tutor);
        BDDMockito.given(petRepository.getReferenceById(adocaoSolicitacaoDto.idPet())).willReturn(pet);
        BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);
        this.adocao = new Adocao(tutor, pet, adocaoSolicitacaoDto.motivo());

        Collections.addAll(validacoes, validador1, validador2, validador3, validador4);

        //Act
        adocaoService.solicitar(adocaoSolicitacaoDto);

        //Assert
        BDDMockito.then(validador1).should().validar(adocao);
        BDDMockito.then(validador2).should().validar(adocao);
        BDDMockito.then(validador3).should().validar(adocao);
        BDDMockito.then(validador4).should().validar(adocao);
    }

}