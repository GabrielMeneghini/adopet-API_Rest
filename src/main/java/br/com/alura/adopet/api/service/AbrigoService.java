package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dtos.AbrigoCadastroDto;
import br.com.alura.adopet.api.dtos.AbrigoDetalhamentoDto;
import br.com.alura.adopet.api.dtos.PetCadastroDto;
import br.com.alura.adopet.api.dtos.PetDetalhamentoDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoException;
import br.com.alura.adopet.api.validacoes.abrigo.cadastro.ValidacaoCadastroAbrigo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AbrigoService {

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private List<ValidacaoCadastroAbrigo> validacoes;

    public List<AbrigoDetalhamentoDto> listarAbrigos() {
        return abrigoRepository.findAll().stream().map(AbrigoDetalhamentoDto::new).toList();
    }

    public void cadastrar(AbrigoCadastroDto dto) {
        var abrigo = new Abrigo(dto.nome(), dto.email(), dto.telefone());

        validacoes.forEach(v -> v.validar(abrigo));

        abrigoRepository.save(abrigo);
    }

    public List<PetDetalhamentoDto> listarPets(String idOuNome) {
        List<Pet> pets = encontrarAbrigoPorIdOuNome(idOuNome).getPets();
        if(pets.isEmpty()) {
            throw new ValidacaoException("Não há pets no abrigo informado.");
        }

        return pets.stream().map(p -> new PetDetalhamentoDto(p)).toList();
    }

    public void cadastrarPet(String idOuNome, PetCadastroDto dto) {
        var abrigo = encontrarAbrigoPorIdOuNome(idOuNome);
        Pet pet = new Pet(dto, abrigo);
        abrigo.getPets().add(pet);
        petRepository.save(pet);
    }

    private Abrigo encontrarAbrigoPorIdOuNome(String idOuNome) {
        if(idOuNome.matches("\\d+")) {
            Long id = Long.parseLong(idOuNome);
            return abrigoRepository.findById(id).orElseThrow(() -> new ValidacaoException("Abrigo com id: " + idOuNome + " não encontrado."));
        } else {
            String nomeAbrigo = idOuNome;
            return abrigoRepository.findByNome(nomeAbrigo).orElseThrow(() -> new ValidacaoException("Abrigo " + nomeAbrigo + " não encontrado."));
        }
    }

}
