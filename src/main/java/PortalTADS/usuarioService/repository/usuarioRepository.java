package PortalTADS.usuarioService.repository;

import PortalTADS.usuarioService.repository.entity.usuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface usuarioRepository extends JpaRepository<usuarioEntity, Long> {

}
