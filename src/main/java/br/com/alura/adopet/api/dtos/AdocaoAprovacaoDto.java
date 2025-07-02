package br.com.alura.adopet.api.dtos;

import jakarta.validation.constraints.NotNull;

public record AdocaoAprovacaoDto(@NotNull Long idAdocao) {
}
