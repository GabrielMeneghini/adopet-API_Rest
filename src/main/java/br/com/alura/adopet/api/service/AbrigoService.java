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

    public List<AbrigoDetalhamentoDto> listar() {
        var abrigos = abrigoRepository.findAll();
        List<AbrigoDetalhamentoDto> abrigosDto = new ArrayList<>();

        for(Abrigo a: abrigos) {
            abrigosDto.add(new AbrigoDetalhamentoDto(a.getId(), a.getNome(), a.getEmail(), a.getTelefone()));
        }

        return abrigosDto;
    }

    public void cadastar(AbrigoCadastroDto dto) {
        var abrigo = new Abrigo(dto.nome(), dto.email(), dto.telefone());

        validacoes.forEach(v -> v.validar(abrigo));

        abrigoRepository.save(abrigo);
    }

    public List<PetDetalhamentoDto> listarPets(String idOuNome) {
        List<Pet> pets = encontarAbrigoPorIdOuNome(idOuNome).getPets();
        if(pets.isEmpty()) {
            throw new ValidacaoException("Não há pets no abrigo informado.");
        }

        List<PetDetalhamentoDto> petsDto = new ArrayList<>();
        for(Pet p: pets) {
            petsDto.add(new PetDetalhamentoDto(p));
        }

        return petsDto;
    }

    public void cadastrarPet(String idOuNome, PetCadastroDto dto) {
        var abrigo = encontarAbrigoPorIdOuNome(idOuNome);
        Pet pet = new Pet(dto, abrigo);
        abrigo.getPets().add(pet);
        petRepository.save(pet);
    }

    private Abrigo encontarAbrigoPorIdOuNome(String idOuNome) {
        if(idOuNome.matches("\\d+")) {
            Long id = Long.parseLong(idOuNome);
            return abrigoRepository.getReferenceById(id);
        } else {
            String nomeAbrigo = idOuNome;
            return abrigoRepository.findByNome(nomeAbrigo);
        }
    }
}
