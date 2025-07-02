package br.com.alura.adopet.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdocaoSolicitacaoDto(@NotNull Long idtutor,
                                   @NotNull Long idPet,
                                   @NotBlank String motivo) {}
