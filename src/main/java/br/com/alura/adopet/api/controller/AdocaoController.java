package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dtos.AdocaoSolicitacaoDto;
import br.com.alura.adopet.api.dtos.AdocaoAprovacaoDto;
import br.com.alura.adopet.api.dtos.AdocaoReprovacaoDto;
import br.com.alura.adopet.api.service.AdocaoService;
import br.com.alura.adopet.api.validacoes.ValidacaoException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/adocoes")
public class AdocaoController {

    @Autowired
    private AdocaoService adocaoService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> solicitar(@RequestBody @Valid AdocaoSolicitacaoDto adocaoSolicitacaoDto) {
        try {
            adocaoService.solicitar(adocaoSolicitacaoDto);
            return ResponseEntity.ok("Adoção solicitada com sucesso.");
        } catch(ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/aprovar")
    @Transactional
    public ResponseEntity<String> aprovar(@RequestBody @Valid AdocaoAprovacaoDto adocaoAprovacaoDto) {
        try {
            adocaoService.aprovar(adocaoAprovacaoDto);
            return ResponseEntity.ok("Adoção aprovada.");
        } catch(ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/reprovar")
    @Transactional
    public ResponseEntity<String> reprovar(@RequestBody @Valid AdocaoReprovacaoDto adocaoReprovacaoDto) {
        try {
            adocaoService.reprovar(adocaoReprovacaoDto);
            return ResponseEntity.ok("Sua adoção foi reprovada. Justificativa: " + adocaoReprovacaoDto.justificativa());
        } catch(ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
