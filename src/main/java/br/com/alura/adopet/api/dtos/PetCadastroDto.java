package br.com.alura.adopet.api.dtos;

import br.com.alura.adopet.api.model.TipoPet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetCadastroDto(@NotNull TipoPet tipo,
                             @NotBlank String nome,
                             @NotBlank String raca,
                             @NotNull Integer idade,
                             @NotBlank String cor,
                             Float peso,
                             @NotNull Long abrigoId
                             ) {
}
