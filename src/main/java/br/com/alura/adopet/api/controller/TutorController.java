package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dtos.TutorAtualizacaoDto;
import br.com.alura.adopet.api.dtos.TutorCadastroDto;
import br.com.alura.adopet.api.service.TutorService;
import br.com.alura.adopet.api.validacoes.ValidacaoException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tutores")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestBody @Valid TutorCadastroDto dto) {
        try {
            tutorService.cadastrarTutor(dto);
            return ResponseEntity.ok("Tutor cadastrado com sucesso!");
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    @Transactional
    public ResponseEntity<String> atualizar(@RequestBody @Valid TutorAtualizacaoDto dto) {
        tutorService.atualizarTutor(dto);
        return ResponseEntity.ok().build();
    }

}
