package br.com.alura.adopet.api.dtos;

public record AbrigoDetalhamentoDto(Long id,
                                    String nome,
                                    String telefone,
                                    String email) {

    public AbrigoDetalhamentoDto(Long id, String nome, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }
}
