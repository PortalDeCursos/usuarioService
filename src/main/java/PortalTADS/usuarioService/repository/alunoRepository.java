package PortalTADS.usuarioService.repository;

import PortalTADS.usuarioService.repository.entity.alunoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface alunoRepository extends JpaRepository<alunoEntity, Long> {

    @Query("""
        SELECT a
        FROM alunoEntity a
        JOIN a.usuario u
        WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
        """)
    List<alunoEntity> buscarPorNomeUsuario(@Param("nome") String nome);

    List<alunoEntity> findByCursoId(Long cursoId);


    Optional<alunoEntity> findById(Long id);
}
