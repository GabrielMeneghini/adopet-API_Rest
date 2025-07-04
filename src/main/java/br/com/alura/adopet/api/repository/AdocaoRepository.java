package br.com.alura.adopet.api.repository;

import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdocaoRepository extends JpaRepository<Adocao, Long> {

    @Query("""
            SELECT COUNT(a) > 0 FROM Adocao a
            WHERE a.pet.id = :idPet AND a.status LIKE 'AGUARDANDO_AVALIACAO'
            """)
    boolean petJaAguardandoAvaliacao(Long idPet);

    @Query("""
            SELECT COUNT(a) > 0 FROM Adocao a
            WHERE a.tutor.id = :idTutor AND a.status LIKE 'AGUARDANDO_AVALIACAO'
            """)
    boolean tutorJaAguardandoAvaliacao(Long idTutor);

    @Query("""
            SELECT COUNT(a) > 4 FROM Adocao a
            WHERE a.tutor.id = :idTutor AND a.status LIKE 'APROVADO'
            """)
    boolean tutorMaximoAdocoes(Long idTutor);

}
