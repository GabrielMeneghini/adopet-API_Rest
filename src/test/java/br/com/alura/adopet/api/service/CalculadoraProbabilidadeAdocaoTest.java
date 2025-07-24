package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dtos.PetCadastroDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.ProbabilidadeAdocao;
import br.com.alura.adopet.api.model.TipoPet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalculadoraProbabilidadeAdocaoTest {

    @Test
    @DisplayName("Deve retornar probabilidade BAIXA para pets com idade avançada e peso alto")
    void calcularCenario01() {
        // Arrange
        var abrigo = new Abrigo("brigoTeste1","abrigoTeste1@gmail.com","0011110000");
        var pet = new Pet(new PetCadastroDto(TipoPet.GATO, "petTeste1", "Siames", 10, "laranja", 11.0f), abrigo);

        // Act
        var calculadora = new CalculadoraProbabilidadeAdocao();
        var probabilidade = calculadora.calcular(pet);

        // Assert
        Assertions.assertEquals(ProbabilidadeAdocao.BAIXA, probabilidade);
    }

    @Test
    @DisplayName("Deve retornar probabilidade ALTA para pets com idade baixa e peso baixo")
    void calcularCenario02() {
        var abrigo = new Abrigo("brigoTeste1","abrigoTeste1@gmail.com","0011110000");
        var pet = new Pet(new PetCadastroDto(TipoPet.GATO, "petTeste1", "Siames", 4, "laranja", 4.0f), abrigo);

        var calculadora = new CalculadoraProbabilidadeAdocao();
        var probabilidade = calculadora.calcular(pet);

        Assertions.assertEquals(ProbabilidadeAdocao.ALTA, probabilidade);
    }

    @Test
    @DisplayName("Deve retornar probabilidade MEDIA para pets com idade avançada e peso baixo")
    void calcularCenario03() {
        var abrigo = new Abrigo("brigoTeste1","abrigoTeste1@gmail.com","0011110000");
        var pet = new Pet(new PetCadastroDto(TipoPet.GATO, "petTeste1", "Siames", 17, "laranja", 3.0f), abrigo);

        var calculadora = new CalculadoraProbabilidadeAdocao();
        var probabilidade = calculadora.calcular(pet);

        Assertions.assertEquals(ProbabilidadeAdocao.MEDIA, probabilidade);
    }

}