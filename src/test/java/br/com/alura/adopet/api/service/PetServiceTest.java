package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dtos.PetDetalhamentoDto;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @InjectMocks
    private PetService petService;

    @Mock
    private PetRepository petRepository;

    @Test
    @DisplayName("Deve lançar exceção se lista de pets estiver vazia")
    void listarPetsDisponiveis01() {
        BDDMockito.given(petRepository.findByAdotadoFalse()).willReturn(Collections.emptyList());

        Assertions.assertThrows(ValidacaoException.class, () -> petService.listarPetsDisponiveis());
    }
    @Test
    @DisplayName("Deve retorna lista de pets disponíveis")
    void listarPetsDisponiveis02() {
        var pet1 = Mockito.mock(Pet.class);
        var pet2  = Mockito.mock(Pet.class);
        var pet3 = Mockito.mock(Pet.class);
        List<Pet> pets = new ArrayList<>(List.of(pet1, pet2, pet3).stream().toList());
        BDDMockito.given(petRepository.findByAdotadoFalse()).willReturn(pets);

        List<PetDetalhamentoDto> listaDTOresultado = petService.listarPetsDisponiveis();

        Assertions.assertFalse(listaDTOresultado.isEmpty());
        Assertions.assertEquals(3, listaDTOresultado.size());
    }

}