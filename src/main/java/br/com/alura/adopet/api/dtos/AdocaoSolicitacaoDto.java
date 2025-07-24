package br.com.alura.adopet.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdocaoSolicitacaoDto(@NotNull Long idTutor,
                                   @NotNull Long idPet,
                                   @NotBlank String motivo) {}
