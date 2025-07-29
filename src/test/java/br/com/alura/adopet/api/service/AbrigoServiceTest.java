package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dtos.AbrigoCadastroDto;
import br.com.alura.adopet.api.dtos.PetCadastroDto;
import br.com.alura.adopet.api.dtos.PetDetalhamentoDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoException;
import br.com.alura.adopet.api.validacoes.abrigo.cadastro.ValidacaoCadastroAbrigo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AbrigoServiceTest {

    @InjectMocks
    private AbrigoService abrigoService;

    @Mock
    private AbrigoRepository abrigoRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private Abrigo abrigo;

    @Captor
    private ArgumentCaptor<Abrigo> abrigoArgumentCaptor;

    @Captor
    private ArgumentCaptor<Pet> petArgumentCaptor;

    @Spy
    private List<ValidacaoCadastroAbrigo> validacoes = new ArrayList<>();
        @Mock
        private ValidacaoCadastroAbrigo validacao1;


    @Test
    @DisplayName("Deve chamar lista com todos os ABRIGOS")
    void listarAbrigos01() {
        abrigoService.listarAbrigos();

        Mockito.verify(abrigoRepository).findAll();
    }

    @Test
    @DisplayName("Deve salvar ABRIGO no Banco de Dados se dados estiverem corretos")
    void cadastrar01() {
        var dto = new AbrigoCadastroDto("nomeTeste", "00911110000", "emailTeste");

        abrigoService.cadastrar(dto);

        Mockito.verify(abrigoRepository).save(abrigoArgumentCaptor.capture());
        Abrigo abrigoSalvo = abrigoArgumentCaptor.getValue();

        Assertions.assertEquals(dto.nome(), abrigoSalvo.getNome());
        Assertions.assertEquals(dto.telefone(), abrigoSalvo.getTelefone());
        Assertions.assertEquals(dto.email(), abrigoSalvo.getEmail());
    }
    @Test
    @DisplayName("Deve chamar validadores de cadastro de abrigo")
    void cadastrar02() {
        var dto = new AbrigoCadastroDto("nomeTeste", "00911110000", "emailTeste");
        validacoes.add(validacao1);

        abrigoService.cadastrar(dto);

        Mockito.verify(validacao1).validar(abrigoArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Deve lançar exceção se lista de PETS estiver VAZIA")
    void listarPets01() {
       String idOuNome = "nomeTeste";

       BDDMockito.given(abrigoRepository.findByNome(idOuNome)).willReturn(Optional.of(abrigo));
       BDDMockito.given(abrigo.getPets()).willReturn(Collections.emptyList());

       Assertions.assertThrows(ValidacaoException.class, () -> abrigoService.listarPets(idOuNome));
    }
    @Test
    @DisplayName("Deve retornar lista de PETS convertida para DTO, se lista não estiver vazia")
    void listarPets02() {
        // Arrange
        String idOuNome = "nomeTeste";

        var mockPet01 = Mockito.mock(Pet.class);
        var mockPet02 = Mockito.mock(Pet.class);
        List<Pet> pets = List.of(mockPet01, mockPet02);

        BDDMockito.given(abrigoRepository.findByNome(idOuNome)).willReturn(Optional.of(abrigo));
        BDDMockito.given(abrigo.getPets()).willReturn(pets);

        // Act
        List<PetDetalhamentoDto> petsDTO = abrigoService.listarPets(idOuNome);

        // Assert
        Assertions.assertEquals(petsDTO.size(), pets.size());
    }
    @Test
    @DisplayName("Deve lançar exceção se ID do abrigo não existir")
    void listarPets03() {
        String idOuNome = "1";
        Long id = 1L;
        BDDMockito.given(abrigoRepository.findById(id)).willReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> abrigoService.listarPets(idOuNome));
    }
    @Test
    @DisplayName("Deve lançar exceção se NOME do abrigo não existir")
    void listarPets04() {
        String idOuNome = "nomeTeste";
        BDDMockito.given(abrigoRepository.findByNome(idOuNome)).willReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> abrigoService.listarPets(idOuNome));
    }

    @Test
    @DisplayName("Deve salvar PET no Banco de Dados se dados estiverem corretos")
    void cadastrarPet01() {
        String idOuNome = "nomeTeste";
        var dto = new PetCadastroDto(TipoPet.GATO, "nomeTeste", "raçaTeste", 3, "corTeste", 7F);
        BDDMockito.given(abrigoRepository.findByNome(idOuNome)).willReturn(Optional.of(abrigo));

        abrigoService.cadastrarPet(idOuNome, dto);

        Mockito.verify(petRepository).save(petArgumentCaptor.capture());
        var pet = petArgumentCaptor.getValue();
        Assertions.assertEquals(dto.nome(), pet.getNome());
        Assertions.assertEquals(dto.raca(), pet.getRaca());
        Assertions.assertEquals(dto.idade(), pet.getIdade());
        Assertions.assertEquals(dto.cor(), pet.getCor());
        Assertions.assertEquals(dto.peso(), pet.getPeso());
        Assertions.assertEquals(dto.tipo(), pet.getTipo());
    }
    @Test
    @DisplayName("Deve adicionar o novo PET a lista de pets do ABRIGO")
    void cadastrarPet02() {
        String idOuNome = "nomeTeste";
        var dto = new PetCadastroDto(TipoPet.GATO, "nomeTeste", "raçaTeste", 3, "corTeste", 7F);
        BDDMockito.given(abrigoRepository.findByNome(idOuNome)).willReturn(Optional.of(abrigo));

        List<Pet> pets = Mockito.mock(List.class);
        BDDMockito.given(abrigo.getPets()).willReturn(pets);

        abrigoService.cadastrarPet(idOuNome, dto);

        Mockito.verify(abrigo.getPets()).add(petArgumentCaptor.capture());
    }
}