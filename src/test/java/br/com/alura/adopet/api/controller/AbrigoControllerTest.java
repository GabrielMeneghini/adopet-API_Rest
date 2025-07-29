package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dtos.AbrigoCadastroDto;
import br.com.alura.adopet.api.dtos.AbrigoDetalhamentoDto;
import br.com.alura.adopet.api.dtos.PetCadastroDto;
import br.com.alura.adopet.api.dtos.PetDetalhamentoDto;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.validacoes.ValidacaoException;
import jakarta.persistence.EntityNotFoundException;
import jdk.jfr.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AbrigoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<AbrigoCadastroDto> abrigoCadastroDtoJacksonTester;
    @Autowired
    private JacksonTester<PetCadastroDto> petCadastroDtoJacksonTester;

    @MockBean
    private AbrigoService abrigoService;

    @Test
    @DisplayName("Deve devolver código 400 se lista estiver vazia")
    void listarAbrigos01() throws Exception {
        BDDMockito.when(abrigoService.listarAbrigos()).thenReturn(List.of());

        var response = mockMvc.perform(
                MockMvcRequestBuilders.get("/abrigos")
        ).andReturn().getResponse();

        Assertions.assertEquals(400, response.getStatus());
    }
    @Test
    @DisplayName("Deve devolver código 200 se houver elementos na lista")
    void listarAbrigos02() throws Exception {
        List<AbrigoDetalhamentoDto> abrigos = List.of(
                new AbrigoDetalhamentoDto(1L, "nomeTeste1", "11900001111", "email1@test.com"),
                new AbrigoDetalhamentoDto(2L, "nomeTeste2", "11900002222", "email2@test.com"),
                new AbrigoDetalhamentoDto(3L, "nomeTeste3", "11900003333", "email3@test.com"));
        BDDMockito.when(abrigoService.listarAbrigos()).thenReturn(abrigos);

        var response = mockMvc.perform(
            MockMvcRequestBuilders.get("/abrigos")
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Deve devolver código 400 se dados estiverem INCORRETOS")
    void cadastrarAbrigo01() throws Exception {
        var json = "{}";

        var response = mockMvc.perform(
                        post("/abrigos")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(400, response.getStatus());
    }
    @Test
    @DisplayName("Deve devolver código 200 se dados estiverem CORRETOS")
    void cadastrarAbrigo02() throws Exception {
        var dto = new AbrigoCadastroDto("nomeTeste", "11900001111", "test@email.com");

        var response = mockMvc.perform(
            post("/abrigos")
            .content(abrigoCadastroDtoJacksonTester.write(dto).getJson())
            .contentType(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals("Abrigo cadastrado com sucesso", response.getContentAsString());

    }

    @Test
    @DisplayName("Deve devolver código 404 se abrigo NÃO for encontrado")
    void listarPets01() throws Exception {
        var idOuNome = "abrigoTeste";
        BDDMockito.when(abrigoService.listarPets(idOuNome)).thenThrow(new EntityNotFoundException("Abrigo " + idOuNome + " não encontrado."));

        var response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/abrigos/" + idOuNome + "/pets"))
                .andReturn().getResponse();

        Assertions.assertEquals(404, response.getStatus());
    }
    @Test
    @DisplayName("Deve devolver código 200 se abrigo for encontrado")
    void listarPets02() throws Exception {
        var idOuNome = "abrigoTeste";
        List<PetDetalhamentoDto> pets = List.of(
                new PetDetalhamentoDto(1L, TipoPet.GATO, "nomePet1", "raça1", 1, "Cor1"),
                new PetDetalhamentoDto(2L, TipoPet.CACHORRO, "nomePet2", "raça2", 2, "Cor2"),
                new PetDetalhamentoDto(3L, TipoPet.GATO, "nomePet3", "raça3", 3, "Cor3")
        );
        BDDMockito.when(abrigoService.listarPets(idOuNome)).thenReturn(pets);

        var response = mockMvc.perform(
                        MockMvcRequestBuilders.get("/abrigos/" + idOuNome + "/pets"))
                .andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("Deve devolver código 400 se dados forem INCORRETOS")
    void cadastrarPet01() throws Exception {
        var idOuNome = "1";
        var json = "{}";

        var response = mockMvc.perform(
                post("/abrigos/" + idOuNome + "/pets")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

        Assertions.assertEquals(400, response.getStatus());
    }
    @Test
    @DisplayName("Deve devolver código 404 se abrigo NÃO for encontrado")
    void cadastrarPet02() throws Exception {
        var idOuNome = "1";
        var dto = new PetCadastroDto(TipoPet.GATO, "Mingau", "Persa", 2, "Branco", null);
        BDDMockito.willThrow(new EntityNotFoundException("Abrigo com id: " + idOuNome + " não encontrado."))
                .given(abrigoService)
                .cadastrarPet(idOuNome, dto);

        var response = mockMvc.perform(
                        post("/abrigos/" + idOuNome + "/pets")
                                .content(petCadastroDtoJacksonTester.write(dto).getJson())
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(404, response.getStatus());
    }
    @Test
    @DisplayName("Deve devolver código 200 se dados forem CORRETOS")
    void cadastrarPet03() throws Exception {
        var idOuNome = "1";
        var dto = new PetCadastroDto(TipoPet.GATO, "Mingau", "Persa", 2, "Branco", null);

        var response = mockMvc.perform(
                        post("/abrigos/" + idOuNome + "/pets")
                                .content(petCadastroDtoJacksonTester.write(dto).getJson())
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());
    }
}