package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dtos.AbrigoCadastroDto;
import br.com.alura.adopet.api.dtos.AbrigoDetalhamentoDto;
import br.com.alura.adopet.api.dtos.PetCadastroDto;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.validacoes.ValidacaoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/abrigos")
public class AbrigoController {

    @Autowired
    private AbrigoService abrigoService;

    @GetMapping
    public ResponseEntity listarAbrigos() {
        try {
            var abrigos = abrigoService.listarAbrigos();
            if (abrigos.isEmpty()) {
                throw new ValidacaoException("Não há abrigos cadastrados.");
            }
            return ResponseEntity.ok(abrigos);
        } catch(ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrarAbrigo(@RequestBody @Valid AbrigoCadastroDto dto) {
        try {
            abrigoService.cadastrar(dto);
            return ResponseEntity.ok("Abrigo cadastrado com sucesso");
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{idOuNome}/pets")
    public ResponseEntity<?> listarPets(@PathVariable String idOuNome) {
        try {
            return ResponseEntity.ok(abrigoService.listarPets(idOuNome));
        } catch(ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Abrigo não encontrado.");
        }
    }

    @PostMapping("/{idOuNome}/pets")
    @Transactional
    public ResponseEntity<String> cadastrarPet(@PathVariable String idOuNome, @RequestBody @Valid PetCadastroDto dto) {
        try {
            abrigoService.cadastrarPet(idOuNome, dto);

            return ResponseEntity.ok("Pet cadastrado com sucesso");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
