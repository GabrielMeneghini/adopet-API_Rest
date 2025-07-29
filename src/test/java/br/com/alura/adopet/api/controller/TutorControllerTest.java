package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dtos.TutorAtualizacaoDto;
import br.com.alura.adopet.api.dtos.TutorCadastroDto;
import br.com.alura.adopet.api.service.TutorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<TutorCadastroDto> tutorCadastroDtoJacksonTester;
    @Autowired
    private JacksonTester<TutorAtualizacaoDto> tutorAtualizacaoDtoJacksonTester;

    @MockBean
    private TutorService tutorService;

    @Test
    @DisplayName("Deve retornar código 400 se dados estiverem INCORRETOS")
    void cadastrar01() throws Exception {
        var json = "{}";

        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/tutores")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }
    @Test
    @DisplayName("Deve retornar código 200 se dados estiverem CORRETOS")
    void cadastrar02() throws Exception {
        TutorCadastroDto dto = new TutorCadastroDto("João da Silva", "(11)91234-5678", "joao.silva@example.com");

        var response = mockMvc.perform(
                MockMvcRequestBuilders.post("/tutores")
                        .content(tutorCadastroDtoJacksonTester.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(200, response.getStatus());
        assertEquals("Tutor cadastrado com sucesso!", response.getContentAsString());
    }

    @Test
    @DisplayName("Deve retornar código 400 se dados estiverem INCORRETOS")
    void atualizar01() throws Exception {
        var json = "{}";

        var response = mockMvc.perform(
                MockMvcRequestBuilders.put("/tutores")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(400, response.getStatus());
    }
    @Test
    @DisplayName("Deve retornar código 200 se dados estiverem CORRETOS")
    void atualizar02() throws Exception {
        TutorAtualizacaoDto dto = new TutorAtualizacaoDto(1L, "João Atualizado", "(11)98765-4321", "joao.atualizado@example.com");

        mockMvc.perform(MockMvcRequestBuilders.put("/tutores")
                        .content(tutorAtualizacaoDtoJacksonTester.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

    }

}
