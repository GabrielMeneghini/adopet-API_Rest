package br.com.alura.adopet.api.validacoes.tutor.cadastro;

import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidadorDadosUnicosTutorTest {

    @InjectMocks
    private ValidadorDadosUnicosTutor validadorDadosUnicosTutor;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private Tutor tutor;

    @Test
    @DisplayName("Não deve lançar exceção se dados forem únicos no Banco de Dados")
    void validarCenario01() {
        Assertions.assertDoesNotThrow(() -> validadorDadosUnicosTutor.validar(tutor));
    }

    @Test
    @DisplayName("Deve lançar exceção se EMAIL não for único no Banco de Dados")
    void validarCenario02() {
        BDDMockito.given(tutorRepository.existsByEmail(tutor.getEmail())).willReturn(true);

        Assertions.assertThrows(ValidacaoException.class, () -> validadorDadosUnicosTutor.validar(tutor));
    }

    @Test
    @DisplayName("Deve lançar exceção se TELEFONE não for único no Banco de Dados")
    void validarCenario03() {
        BDDMockito.given(tutorRepository.existsByTelefone(tutor.getTelefone())).willReturn(true);

        Assertions.assertThrows(ValidacaoException.class, () -> validadorDadosUnicosTutor.validar(tutor));
    }

}