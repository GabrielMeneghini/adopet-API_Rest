package br.com.alura.adopet.api.validacoes.adocao.solitacao;

import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
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
class ValidadorTutorMaximoDeAdocoesTest {

    @InjectMocks
    private ValidadorTutorMaximoDeAdocoes validadorTutorMaximoDeAdocoes;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private Adocao adocao;

    @Mock
    private Tutor tutor;

    @Test
    @DisplayName("Deve lançar exceção se tutor já tiver 5 adoções aprovadas")
    void validarCenario01() {
        BDDMockito.when(adocao.getTutor()).thenReturn(tutor);
        BDDMockito.when(tutor.getId()).thenReturn(1L);
        BDDMockito.when(adocaoRepository.tutorMaximoAdocoes(tutor.getId())).thenReturn(true);

        Assertions.assertThrows(ValidacaoException.class, () -> validadorTutorMaximoDeAdocoes.validar(adocao));
    }

    @Test
    @DisplayName("Não deve lançar exceção se tutor não tiver 5 adoções aprovadas")
    void validarCenario02() {
        BDDMockito.when(adocao.getTutor()).thenReturn(tutor);
        BDDMockito.when(tutor.getId()).thenReturn(1L);
        BDDMockito.when(adocaoRepository.tutorMaximoAdocoes(tutor.getId())).thenReturn(false);

        Assertions.assertDoesNotThrow(() -> validadorTutorMaximoDeAdocoes.validar(adocao));
    }
}