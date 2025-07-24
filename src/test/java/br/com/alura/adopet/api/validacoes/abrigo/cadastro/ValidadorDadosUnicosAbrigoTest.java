package br.com.alura.adopet.api.validacoes.abrigo.cadastro;

import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.repository.AbrigoRepository;
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
class ValidadorDadosUnicosAbrigoTest {

    @InjectMocks
    private ValidadorDadosUnicosAbrigo validadorDadosUnicosAbrigo;

    @Mock
    private AbrigoRepository abrigoRepository;

    @Mock
    private Abrigo abrigo;

    @Test
    @DisplayName("Não deve lançar exceção se dados forem únicos no Banco de Dados")
    void validarCenario01() {
        Assertions.assertDoesNotThrow(() -> validadorDadosUnicosAbrigo.validar(abrigo));
    }

    @Test
    @DisplayName("Deve lançar exceção se NOME não for único no Banco de Dados")
    void validarCenario02() {
        BDDMockito.given(abrigoRepository.existsByNome(abrigo.getNome())).willReturn(true);

        Assertions.assertThrows(ValidacaoException.class, () -> validadorDadosUnicosAbrigo.validar(abrigo));
    }

    @Test
    @DisplayName("Deve lançar exceção se TELEFONE não for único no Banco de Dados")
    void validarCenario03() {
        BDDMockito.given(abrigoRepository.existsByTelefone(abrigo.getTelefone())).willReturn(true);

        Assertions.assertThrows(ValidacaoException.class, () -> validadorDadosUnicosAbrigo.validar(abrigo));
    }

    @Test
    @DisplayName("Deve lançar exceção se EMAIL não for único no Banco de Dados")
    void validarCenario04() {
        BDDMockito.given(abrigoRepository.existsByEmail(abrigo.getEmail())).willReturn(true);

        Assertions.assertThrows(ValidacaoException.class, () -> validadorDadosUnicosAbrigo.validar(abrigo));
    }
}