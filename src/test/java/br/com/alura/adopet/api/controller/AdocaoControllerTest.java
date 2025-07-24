package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dtos.AdocaoAprovacaoDto;
import br.com.alura.adopet.api.dtos.AdocaoReprovacaoDto;
import br.com.alura.adopet.api.dtos.AdocaoSolicitacaoDto;
import br.com.alura.adopet.api.service.AdocaoService;
import org.junit.jupiter.api.Assertions;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AdocaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdocaoService adocaoService;

    @Autowired
    private JacksonTester<AdocaoSolicitacaoDto> jTadocaoSolicitacaoDto;

    @Autowired
    private JacksonTester<AdocaoAprovacaoDto> jTadocaoAprovacaoDto;

    @Autowired
    private JacksonTester<AdocaoReprovacaoDto> jTadocaoReprovacaoDto;

    @Test
    @DisplayName("Deve devolver código 400 para solicitação com erros")
    void solicitarCenario01() throws Exception {
        // Arrange
        var json = "{}";

        // Act
        var response = mockMvc.perform(
                post("/adocoes")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("Deve devolver código 200 para solicitação sem erros")
    void solicitarCenario02() throws Exception {
        // Arrange
        var dto = new AdocaoSolicitacaoDto(42L, 7L, "Tenho experiência com cães idosos e espaço adequado em casa.");

        // Act
        var response = mockMvc.perform(
                post("/adocoes")
                        .content(jTadocaoSolicitacaoDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals("Adoção solicitada com sucesso.", response.getContentAsString());
    }

    @Test
    @DisplayName("Deve devolver código 400 para aprovação com erros no json")
    void aprovarCenario01() throws Exception {
        // Arrange
        var json = "{}";

        // Act
        var response = mockMvc.perform(
                put("/adocoes/aprovar")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("Deve devolver código 200 para aprovação sem erros no json")
    void aprovarCenario02() throws Exception {
        // Arrange
        var dto = new AdocaoAprovacaoDto(7L);

        // Act
        var response = mockMvc.perform(
                put("/adocoes/aprovar")
                        .content(jTadocaoAprovacaoDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals("Adoção aprovada.", response.getContentAsString());
    }

    @Test
    @DisplayName("Deve devolver código 400 para reprovação com erros no json")
    void reprovarCenario01() throws Exception {
        // Arrange
        var json = "{}";

        // Act
        var response = mockMvc.perform(
                put("/adocoes/reprovar")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("Deve devolver código 200 para reprovação sem erros no json")
    void reprovarCenario02() throws Exception {
        // Arrange
        var dto = new AdocaoReprovacaoDto(3L, "Justificativa teste");

        // Act
        var response = mockMvc.perform(
                put("/adocoes/reprovar")
                        .content(jTadocaoReprovacaoDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        Assertions.assertEquals(200, response.getStatus());
        org.assertj.core.api.Assertions.assertThat("Sua adoção foi reprovada. Justificativa: " + dto.justificativa())
                .isEqualTo(response.getContentAsString());
    }

}