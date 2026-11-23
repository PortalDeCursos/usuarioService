package PortalTADS.usuarioService.repository;

import PortalTADS.usuarioService.repository.entity.AlunoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<AlunoEntity, Long> {

    @Query("""
        SELECT a
        FROM AlunoEntity a
        JOIN a.usuario u
        WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
        """)
    List<AlunoEntity> buscarPorNomeUsuario(@Param("nome") String nome);

    List<AlunoEntity> findByCursoId(Long cursoId);


    Optional<AlunoEntity> findById(Long id);


    boolean existsByUsuario_Id(Long id);

    Optional<AlunoEntity> findByUsuario_Email(String email);
}
