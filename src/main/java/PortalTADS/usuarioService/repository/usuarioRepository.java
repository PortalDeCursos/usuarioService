package PortalTADS.usuarioService.repository;

import PortalTADS.usuarioService.enums.roleUsuario;
import PortalTADS.usuarioService.repository.entity.usuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface usuarioRepository extends JpaRepository<usuarioEntity, Long> {

    boolean existsByUsername(String admin);
    boolean existsByTipoUsuario(roleUsuario admin);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);

    List<usuarioEntity> findByNomeContainingIgnoreCase(String nome);
}
