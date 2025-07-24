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
class ValidadorTutorJaAdotandoTest {

    @InjectMocks
    private ValidadorTutorJaAdotando validadorTutorJaAdotando;

    @Mock
    private AdocaoRepository repository;

    @Mock
    private Adocao adocao;

    @Mock
    private Tutor tutor;

    @Test
    @DisplayName("Deve lançar exceção se tutor já possuir uma avaliação em andamento")
    void validarCenario01() {
        BDDMockito.when(adocao.getTutor()).thenReturn(tutor);
        BDDMockito.when(tutor.getId()).thenReturn(1L);
        BDDMockito.when(repository.tutorJaAguardandoAvaliacao(tutor.getId())).thenReturn(true);

        Assertions.assertThrows(ValidacaoException.class, () -> validadorTutorJaAdotando.validar(adocao));
    }

    @Test
    @DisplayName("Não deve lançar exceção se tutor não possuir uma avaliação em andamento")
    void validarCenario02() {
        BDDMockito.when(adocao.getTutor()).thenReturn(tutor);
        BDDMockito.when(adocao.getTutor().getId()).thenReturn(1L);
        BDDMockito.when(repository.tutorJaAguardandoAvaliacao(adocao.getTutor().getId())).thenReturn(false);

        Assertions.assertDoesNotThrow(() -> validadorTutorJaAdotando.validar(adocao));
    }

}
