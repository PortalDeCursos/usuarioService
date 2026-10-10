package PortalTADS.usuarioService.repository;

import PortalTADS.usuarioService.repository.entity.professorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface professorRepository extends JpaRepository<professorEntity, Long> {

    @Query("""
        SELECT p
        FROM professorEntity p
        JOIN p.usuario u
        WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
        """)
    List<professorEntity> buscarPorNomeUsuario(
            @Param("nome") String nome
    );

}
