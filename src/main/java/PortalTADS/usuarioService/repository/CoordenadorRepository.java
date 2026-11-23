package PortalTADS.usuarioService.repository;

import PortalTADS.usuarioService.repository.entity.CoordenadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoordenadorRepository extends JpaRepository<CoordenadorEntity, Long> {

    @Query("""
        SELECT c
        FROM CoordenadorEntity c
        JOIN c.professor p
        JOIN p.usuario u
        WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
        """)
    List<CoordenadorEntity> buscarPorNomeUsuario(@Param("nome") String nome);

    List<CoordenadorEntity> findByCursoId(Long cursoId);

    boolean existsByProfessorId(Long professorId);

    boolean existsByProfessorIdAndIdNot(Long professorId, Long id);

    boolean existsByProfessor_Usuario_Id(Long id);

    java.util.Optional<CoordenadorEntity> findByProfessor_Usuario_Email(String email);
}
