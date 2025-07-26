package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dtos.TutorAtualizacaoDto;
import br.com.alura.adopet.api.dtos.TutorCadastroDto;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.tutor.cadastro.ValidacaoCadastroTutor;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    private TutorCadastroDto tutorCadastroDto;

    @InjectMocks
    private TutorService tutorService;

    @Mock
    private Tutor tutor;
    @Mock
    private TutorRepository tutorRepository;

    @Captor
    private ArgumentCaptor<Tutor> tutorArgumentCaptor;

    @Spy
    private List<ValidacaoCadastroTutor> validacoes = new ArrayList<>();
        @Mock
        private ValidacaoCadastroTutor validacao01;

    @BeforeEach
    void setUp() {
        this.tutorCadastroDto = new TutorCadastroDto("nomeTeste", "00911110000", "email@test.com");

        Collections.addAll(validacoes, validacao01);
    }

    @Test
    @DisplayName("Deve chamar validadores de tutor ao cadastrar")
    void cadastrarTutor01() {
        tutorService.cadastrarTutor(tutorCadastroDto);

        then(validacao01).should().validar(tutorArgumentCaptor.capture());
    }
    @Test
    @DisplayName("Deve salvar TUTOR no Banco de Dados se dados estiverem corretos")
    void cadastrarTutor02() {
        tutorService.cadastrarTutor(tutorCadastroDto);

        then(tutorRepository).should().save(tutorArgumentCaptor.capture());
        var tutorSalvo = tutorArgumentCaptor.getValue();
        assertEquals(tutorCadastroDto.nome(), tutorSalvo.getNome());
        assertEquals(tutorCadastroDto.telefone(), tutorSalvo.getTelefone());
        assertEquals(tutorCadastroDto.email(), tutorSalvo.getEmail());
    }

    @Test
    @DisplayName("Deve atualizar apenas NOME se outros dados forem NULOS")
    void atualizarTutor01() {
        var dto = new TutorAtualizacaoDto(1L, "nomeAtualizacaoTeste", null, null);
        when(tutorRepository.getReferenceById(dto.id())).thenReturn(this.tutor);

        tutorService.atualizarTutor(dto);

        then(tutor).should().setNome(dto.nome());
        then(tutor).should(never()).setTelefone(any());
        then(tutor).should(never()).setEmail(any());

        then(tutorRepository).should().save(tutor);
    }
    @Test
    @DisplayName("Deve atualizar apenas TELEFONE se outros dados forem NULOS")
    void atualizarTutor02() {
        var dto = new TutorAtualizacaoDto(1L, null, "999999999", null);
        when(tutorRepository.getReferenceById(dto.id())).thenReturn(this.tutor);

        tutorService.atualizarTutor(dto);

        then(tutor).should().setTelefone(dto.telefone());
        then(tutor).should(never()).setNome(any());
        then(tutor).should(never()).setEmail(any());

        then(tutorRepository).should().save(tutor);
    }
    @Test
    @DisplayName("Deve atualizar apenas EMAIL se outros dados forem NULOS")
    void atualizarTutor03() {
        var dto = new TutorAtualizacaoDto(1L, null, null, "email@test.com");
        when(tutorRepository.getReferenceById(dto.id())).thenReturn(this.tutor);

        tutorService.atualizarTutor(dto);

        then(tutor).should().setEmail(dto.email());
        then(tutor).should(never()).setNome(any());
        then(tutor).should(never()).setTelefone(any());

        then(tutorRepository).should().save(tutor);
    }


}