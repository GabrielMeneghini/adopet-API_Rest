package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dtos.PetDetalhesDto;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetRepository petRepository;

    @GetMapping
    public ResponseEntity<List<PetDetalhesDto>> listarTodosDisponiveis() {
        List<Pet> petsDisponiveis = petRepository.findByAdotadoFalse();

        List<PetDetalhesDto> petsDisponiveisDto = new ArrayList<>();
        for(Pet pet: petsDisponiveis) {
            PetDetalhesDto dto = new PetDetalhesDto(pet);
            petsDisponiveisDto.add(dto);
        }

        return ResponseEntity.ok(petsDisponiveisDto);
    }

}
