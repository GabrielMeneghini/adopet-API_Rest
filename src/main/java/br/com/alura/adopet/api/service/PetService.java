package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dtos.PetDetalhamentoDto;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public List<PetDetalhamentoDto> listarPetsDisponiveis() {
        List<Pet> petsDisponiveis = petRepository.findByAdotadoFalse();
        if(petsDisponiveis.isEmpty()) {
            throw new ValidacaoException("Não há pets disponíveis");
        }

        List<PetDetalhamentoDto> petsDisponiveisDto = new ArrayList<>();
        for(Pet pet: petsDisponiveis) {
            PetDetalhamentoDto dto = new PetDetalhamentoDto(pet);
            petsDisponiveisDto.add(dto);
        }

        return petsDisponiveisDto;
    }

}
