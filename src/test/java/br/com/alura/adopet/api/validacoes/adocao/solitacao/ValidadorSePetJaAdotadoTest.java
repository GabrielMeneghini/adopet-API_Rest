package br.com.alura.adopet.api.validacoes.adocao.solitacao;

import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
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
class ValidadorSePetJaAdotadoTest {

    @InjectMocks
    private ValidadorSePetJaAdotado validadorSePetJaAdotado;

    @Mock
    private Adocao adocao;

    @Mock
    private Pet pet;

    @Test
    @DisplayName("Não deve lançar exceção se pet não tiver sido adotado")
    void validarCenario01() {
        // Arrange
        BDDMockito.given(adocao.getPet()).willReturn(pet);
        BDDMockito.given(pet.getAdotado()).willReturn(false);

        // Act + Assert
        Assertions.assertDoesNotThrow(() -> validadorSePetJaAdotado.validar(adocao));
    }

    @Test
    @DisplayName("Deve lançar exceção se pet já tiver sido adotado")
    void validarCenario02() {
        // Arrange
        BDDMockito.given(adocao.getPet()).willReturn(pet);
        BDDMockito.given(pet.getAdotado()).willReturn(true);

        // Act + Assert
        Assertions.assertThrows(ValidacaoException.class, () -> validadorSePetJaAdotado.validar(adocao));
    }

}