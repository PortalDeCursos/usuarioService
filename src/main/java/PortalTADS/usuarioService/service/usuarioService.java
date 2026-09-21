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

    public usuarioResponseDTO buscarUsuario(Long id) {
        usuarioEntity entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return mapper.toResponse(entity);
    }

    public usuarioResponseDTO atualizarUsuario(Long id, usuarioRequestDTO record) {
        usuarioEntity entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuario usuarioModel = mapper.toModel(record);
        usuarioModel.setId(entity.getId());
        usuarioModel.setRole(roleUsuario.USER);
        usuarioEntity updatedEntity = mapper.toEntity(usuarioModel);
        repository.save(updatedEntity);
        return mapper.toResponse(updatedEntity);
    }
}
