package br.com.alura.adopet.api.dtos;

import br.com.alura.adopet.api.model.Abrigo;

public record AbrigoDetalhamentoDto(Long id,
                                    String nome,
                                    String telefone,
                                    String email) {

    public AbrigoDetalhamentoDto(Abrigo a) {
        this(a.getId(), a.getNome(), a.getTelefone(), a.getEmail());
    }

}
