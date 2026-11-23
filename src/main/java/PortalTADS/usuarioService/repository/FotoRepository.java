package PortalTADS.usuarioService.repository;

import PortalTADS.usuarioService.repository.entity.FotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FotoRepository extends JpaRepository<FotoEntity, Long> {
    boolean existsByUsuarioId(Long usuarioId);

    Optional<FotoEntity> findByUsuarioId(Long usuarioId);
}
