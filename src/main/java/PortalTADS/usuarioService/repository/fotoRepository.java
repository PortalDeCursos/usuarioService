package PortalTADS.usuarioService.repository;

import PortalTADS.usuarioService.repository.entity.fotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface fotoRepository extends JpaRepository<fotoEntity, Long> {
    boolean existsByUsuarioId(Long usuarioId);

    Optional<fotoEntity> findByUsuarioId(Long usuarioId);
}
