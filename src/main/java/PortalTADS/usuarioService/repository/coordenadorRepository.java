package PortalTADS.usuarioService.repository;

import PortalTADS.usuarioService.repository.entity.coordenadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface coordenadorRepository extends JpaRepository<coordenadorEntity, Long> {

    @Query("""
        SELECT c
        FROM coordenadorEntity c
        JOIN c.usuario u
        WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
        """)
    List<coordenadorEntity> buscarPorNomeUsuario(@Param("nome") String nome);

    List<coordenadorEntity> findByCursoId(Long cursoId);
}
