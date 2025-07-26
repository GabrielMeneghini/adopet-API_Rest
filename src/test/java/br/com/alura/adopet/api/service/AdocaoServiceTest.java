package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dtos.AdocaoAprovacaoDto;
import br.com.alura.adopet.api.dtos.AdocaoReprovacaoDto;
import br.com.alura.adopet.api.dtos.AdocaoSolicitacaoDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.adocao.solitacao.ValidacaoSolitacaoAdocao;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {

    private AdocaoSolicitacaoDto adocaoSolicitacaoDto;

    @InjectMocks
    private AdocaoService adocaoService;

    @Mock
    private Adocao adocao;
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
    @Captor
    private ArgumentCaptor<String> toCaptor;
    @Captor
    private ArgumentCaptor<String> subjectCaptor;
    @Captor
    private ArgumentCaptor<String> messageCaptor;
    @Captor
    private ArgumentCaptor<String> justificativaCaptor;

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

    @BeforeEach
    void setUp() {
        adocaoSolicitacaoDto = new AdocaoSolicitacaoDto(10L, 20L, "Motivo teste");
    }

    @Test
    @DisplayName("Deve salvar adoção se dados forem corretos")
    void solicitar01() {
        //Arrange
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
    void solicitar02() {
        //Arrange
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
    @Test
    @DisplayName("Deve enviar EMAIL de SOLICITAÇÃO")
    void solicitar03() {
        BDDMockito.given(tutorRepository.getReferenceById(adocaoSolicitacaoDto.idTutor())).willReturn(tutor);
        BDDMockito.given(petRepository.getReferenceById(adocaoSolicitacaoDto.idPet())).willReturn(pet);
        BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);
        this.adocao = new Adocao(tutor, pet, adocaoSolicitacaoDto.motivo());

        adocaoService.solicitar(adocaoSolicitacaoDto);

        Mockito.verify(emailService).enviarEmail(toCaptor.capture(), subjectCaptor.capture(), messageCaptor.capture());
    }
    @Test
    @DisplayName("Deve adicionar a nova ADOÇÃO a lista de adoções do TUTOR")
    void solicitar04() {
        BDDMockito.given(tutorRepository.getReferenceById(adocaoSolicitacaoDto.idTutor())).willReturn(tutor);
        BDDMockito.given(petRepository.getReferenceById(adocaoSolicitacaoDto.idPet())).willReturn(pet);
        BDDMockito.given(pet.getAbrigo()).willReturn(abrigo);
        this.adocao = new Adocao(tutor, pet, adocaoSolicitacaoDto.motivo());

        List<Adocao> adocoes = Mockito.mock(List.class);
        BDDMockito.given(tutor.getAdocoes()).willReturn(adocoes);

        adocaoService.solicitar(adocaoSolicitacaoDto);

        Mockito.verify(adocoes).add(adocao);
    }

    @Test
    @DisplayName("Deve marcar ADOÇÃO como APROVADA")
    void aprovar01() {
        // Arrange
        var dto = new AdocaoAprovacaoDto(1L);

        BDDMockito.given(adocaoRepository.getReferenceById(dto.idAdocao())).willReturn(adocao);

        BDDMockito.given(adocao.getData()).willReturn(LocalDateTime.of(2025, Month.APRIL, 7, 15, 33));

        BDDMockito.given(adocao.getPet()).willReturn(this.pet);
        BDDMockito.given(pet.getAbrigo()).willReturn(this.abrigo);

        BDDMockito.given(adocao.getTutor()).willReturn(tutor);

        // Act
        adocaoService.aprovar(dto);

        // Assert
        Mockito.verify(adocao).marcarComoAprovado();
    }
    @Test
    @DisplayName("Deve marcar o PET como ADOTADO")
    void aprovar02() {
        // Arrange
        var dto = new AdocaoAprovacaoDto(1L);

        BDDMockito.given(adocaoRepository.getReferenceById(dto.idAdocao())).willReturn(adocao);

        BDDMockito.given(adocao.getData()).willReturn(LocalDateTime.of(2025, Month.APRIL, 7, 15, 33));

        BDDMockito.given(adocao.getPet()).willReturn(this.pet);
        BDDMockito.given(pet.getAbrigo()).willReturn(this.abrigo);

        BDDMockito.given(adocao.getTutor()).willReturn(tutor);

        // Act
        adocaoService.aprovar(dto);

        // Assert
        Mockito.verify(pet).marcarComoAdotado();
    }
    @Test
    @DisplayName("Deve enviar EMAIL de APROVAÇÃO")
    void aprovar03() {
        // Arrange
        var dto = new AdocaoAprovacaoDto(1L);

        BDDMockito.given(adocaoRepository.getReferenceById(dto.idAdocao())).willReturn(adocao);

        BDDMockito.given(adocao.getData()).willReturn(LocalDateTime.of(2025, Month.APRIL, 7, 15, 33));

        BDDMockito.given(adocao.getPet()).willReturn(this.pet);
        BDDMockito.given(pet.getAbrigo()).willReturn(this.abrigo);
        BDDMockito.given(pet.getNome()).willReturn("nomePetTeste");

        BDDMockito.given(adocao.getTutor()).willReturn(tutor);
        BDDMockito.given(tutor.getNome()).willReturn("nomeTutorTeste");
        BDDMockito.given(tutor.getEmail()).willReturn("email@teste.com");

        // Act
        adocaoService.aprovar(dto);

        // Assert
        Mockito.verify(emailService).enviarEmail(toCaptor.capture(), subjectCaptor.capture(), messageCaptor.capture());
    }

    @Test
    @DisplayName("Deve marcar ADOÇÃO como REPROVADA")
    void reprovar01() {
        // Arrange
        var dto = new AdocaoReprovacaoDto(1L, "Justificativa teste");

        BDDMockito.given(adocaoRepository.getReferenceById(dto.idAdocao())).willReturn(adocao);

        BDDMockito.given(adocao.getData()).willReturn(LocalDateTime.of(2025, Month.APRIL, 7, 15, 33));

        BDDMockito.given(adocao.getPet()).willReturn(this.pet);
        BDDMockito.given(pet.getAbrigo()).willReturn(this.abrigo);

        BDDMockito.given(adocao.getTutor()).willReturn(tutor);

        // Act
        adocaoService.reprovar(dto);

        // Assert
        Mockito.verify(adocao).marcarComoReprovado(justificativaCaptor.capture());
    }
    @Test
    @DisplayName("Deve enviar EMAIL de REPROVAÇÃO")
    void reprovar02() {
        // Arrange
        var dto = new AdocaoReprovacaoDto(1L, "Justificativa teste");

        BDDMockito.given(adocaoRepository.getReferenceById(dto.idAdocao())).willReturn(adocao);

        BDDMockito.given(adocao.getData()).willReturn(LocalDateTime.of(2025, Month.APRIL, 7, 15, 33));

        BDDMockito.given(adocao.getPet()).willReturn(this.pet);
        BDDMockito.given(pet.getAbrigo()).willReturn(this.abrigo);

        BDDMockito.given(adocao.getTutor()).willReturn(tutor);

        // Act
        adocaoService.reprovar(dto);

        // Assert
        Mockito.verify(emailService).enviarEmail(toCaptor.capture(), subjectCaptor.capture(), messageCaptor.capture());
    }
}