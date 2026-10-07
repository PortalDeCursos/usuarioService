package PortalTADS.usuarioService.repository;

import PortalTADS.usuarioService.repository.entity.professorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface professorRepository extends JpaRepository<professorEntity, Long> {

}
