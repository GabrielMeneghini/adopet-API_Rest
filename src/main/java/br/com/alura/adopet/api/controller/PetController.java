package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dtos.PetDetalhamentoDto;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.service.PetService;
import br.com.alura.adopet.api.validacoes.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
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
    private PetService petService;

    @GetMapping
    public ResponseEntity<?> listarPetsDisponiveis() {
        try {
            return ResponseEntity.ok(petService.listarPetsDisponiveis());
        } catch (ValidacaoException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
