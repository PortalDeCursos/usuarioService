package PortalTADS.usuarioService.service;

import PortalTADS.usuarioService.enums.roleUsuario;
import PortalTADS.usuarioService.model.dto.usuarioRequestDTO;
import PortalTADS.usuarioService.model.dto.usuarioResponseDTO;
import PortalTADS.usuarioService.model.mapper.usuarioMapper;
import PortalTADS.usuarioService.model.usuario;
import PortalTADS.usuarioService.repository.entity.usuarioEntity;
import PortalTADS.usuarioService.repository.usuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class usuarioService {

    private final usuarioMapper mapper;
    private final usuarioRepository repository;

    public usuarioResponseDTO salvarUsuario(usuarioRequestDTO record) {
        usuario usuarioModel = mapper.toModel(record);
        usuarioModel.setRole(roleUsuario.USER);
        usuarioEntity entity = mapper.toEntity(usuarioModel);
        repository.save(entity);
        return mapper.toResponse(entity);
    }
}
