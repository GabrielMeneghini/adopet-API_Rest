package br.com.alura.adopet.api.validacoes.adocao.solitacao;

import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
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
class ValidadorSePetJaSendoAdotadoTest {

    @InjectMocks
    private ValidadorSePetJaSendoAdotado validador;

    @Mock
    private AdocaoRepository repository;

    @Mock
    private Adocao adocao;

    @Mock
    private Pet pet;

    @Test
    @DisplayName("Não deve retornar exceção se pet não estiver sendo adotado")
    void validarCenario01() {
        BDDMockito.given(adocao.getPet()).willReturn(pet);
        BDDMockito.given(pet.getId()).willReturn(1L);
        BDDMockito.given(repository.petJaAguardandoAvaliacao(pet.getId())).willReturn(false);

        Assertions.assertDoesNotThrow(() -> validador.validar(adocao));
    }

    @Test
    @DisplayName("Deve retornar exceção se pet já estiver sendo adotado")
    void validarCenario02() {
        BDDMockito.given(adocao.getPet()).willReturn(pet);
        BDDMockito.given(pet.getId()).willReturn(1L);
        BDDMockito.given(repository.petJaAguardandoAvaliacao(pet.getId())).willReturn(true);

        Assertions.assertThrows(ValidacaoException.class, () -> validador.validar(adocao));
    }

}