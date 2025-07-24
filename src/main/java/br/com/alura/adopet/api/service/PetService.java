package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dtos.PetDetalhamentoDto;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public List<PetDetalhamentoDto> listarPetsDisponiveis() {
        List<PetDetalhamentoDto> dtoPetsDisponiveis = petRepository.findByAdotadoFalse().stream().map(PetDetalhamentoDto::new).toList();

        if(dtoPetsDisponiveis.isEmpty()) {
            throw new ValidacaoException("Não há pets disponíveis");
        }

        return dtoPetsDisponiveis;
    }

}
