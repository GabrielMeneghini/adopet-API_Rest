package br.com.alura.adopet.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdocaoReprovacaoDto(@NotNull Long idAdocao,
                                  @NotBlank String justificativa) {
}
