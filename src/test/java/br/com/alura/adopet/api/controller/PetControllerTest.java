package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dtos.PetDetalhamentoDto;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.service.PetService;
import br.com.alura.adopet.api.validacoes.ValidacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @Test
    @DisplayName("Deve retornar código 404 se não houver PETS cadastrados")
    void listarPetsDisponiveis01() throws Exception {
        when(petService.listarPetsDisponiveis()).thenThrow(new ValidacaoException("Não há pets disponíveis"));

        var response = mockMvc.perform(
                MockMvcRequestBuilders.get("/pets")
        ).andReturn().getResponse();

        assertEquals(404, response.getStatus());
    }
    @Test
    @DisplayName("Deve retornar código 200 se houver PETS cadastrados")
    void listarPetsDisponiveis02() throws Exception {
        List<PetDetalhamentoDto> petsDisponiveis = List.of(
                new PetDetalhamentoDto(1L, TipoPet.GATO, "nomePet1", "raça1", 1, "Cor1"),
                new PetDetalhamentoDto(2L, TipoPet.CACHORRO, "nomePet2", "raça2", 2, "Cor2"),
                new PetDetalhamentoDto(3L, TipoPet.GATO, "nomePet3", "raça3", 3, "Cor3")
        );
        when(petService.listarPetsDisponiveis()).thenReturn(petsDisponiveis);

        var response = mockMvc.perform(
                MockMvcRequestBuilders.get("/pets")
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
    }
}