package PortalTADS.usuarioService.repository;

import PortalTADS.usuarioService.enums.RoleUsuario;
import PortalTADS.usuarioService.repository.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    boolean existsByTipoUsuario(RoleUsuario admin);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);

    List<UsuarioEntity> findByNomeContainingIgnoreCase(String nome);



    Optional<UsuarioEntity> findByEmail(String username);
}
