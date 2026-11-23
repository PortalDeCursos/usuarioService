package PortalTADS.usuarioService.repository;

import PortalTADS.usuarioService.enums.TitulacaoProfessor;
import PortalTADS.usuarioService.repository.entity.ProfessorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<ProfessorEntity, Long> {

    @Query("""
        SELECT p
        FROM ProfessorEntity p
        JOIN p.usuario u
        WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
        """)
    List<ProfessorEntity> buscarPorNomeUsuario(
            @Param("nome") String nome
    );

    List<ProfessorEntity> findByTitulacao(TitulacaoProfessor titulacao);


    boolean existsByUsuario_Id(Long id);

    Optional<ProfessorEntity> findByUsuario_Id(Long id);

    Optional<ProfessorEntity> findByUsuario_Email(String email);
}
