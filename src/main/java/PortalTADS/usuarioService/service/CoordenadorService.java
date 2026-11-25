package PortalTADS.usuarioService.service;

import PortalTADS.usuarioService.client.CursoClient;
import PortalTADS.usuarioService.enums.RoleUsuario;
import PortalTADS.usuarioService.exception.AcessoProibidoException;
import PortalTADS.usuarioService.exception.CoordenadorJaCadastratoException;
import PortalTADS.usuarioService.exception.UsuarioNaoEncontradoException;
import PortalTADS.usuarioService.model.Coordenador;
import PortalTADS.usuarioService.model.dto.coordenador.CoordenadorRequestDTO;
import PortalTADS.usuarioService.model.dto.coordenador.CoordenadorResponseDTO;
import PortalTADS.usuarioService.model.mapper.CoordenadorMapper;
import PortalTADS.usuarioService.repository.CoordenadorRepository;
import PortalTADS.usuarioService.repository.entity.CoordenadorEntity;
import PortalTADS.usuarioService.repository.entity.ProfessorEntity;
import PortalTADS.usuarioService.repository.ProfessorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CoordenadorService {

    private final CoordenadorRepository coordenadorRepository;
    private final ProfessorRepository professorRepository;
    private final CoordenadorMapper coordenadorMapper;
    private final CursoClient cursoClient;

    @Transactional
    public CoordenadorResponseDTO criarCoordenador(CoordenadorRequestDTO request) {

        if (coordenadorRepository.existsByProfessorId(request.professorId())) {
            throw new CoordenadorJaCadastratoException(request.professorId());
        }
        cursoClient.existeCurso(request.cursoId());
        Coordenador coordenadorModel = coordenadorMapper.toModel(request);


        ProfessorEntity professor = professorRepository.findById(request.professorId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(request.professorId()));

        professor.getUsuario().setTipoUsuario(RoleUsuario.COORDENADOR);
        CoordenadorEntity entity = coordenadorMapper.toEntity(coordenadorModel);
        entity.setProfessor(professor);
        CoordenadorEntity salvo = coordenadorRepository.save(entity);
        return coordenadorMapper.toResponse(salvo);
    }

    public CoordenadorResponseDTO buscarCoordenadorPorId(Long id) {
        CoordenadorEntity entity = coordenadorRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        return coordenadorMapper.toResponse(entity);
    }

    public List<CoordenadorResponseDTO> listarCoordenadores() {
        return coordenadorMapper.toResponseList(coordenadorRepository.findAll());
    }

    public List<CoordenadorResponseDTO> buscarCoordenadoresPorNome(String nome) {
        return coordenadorMapper.toResponseList(coordenadorRepository.buscarPorNomeUsuario(nome));
    }

    public List<CoordenadorResponseDTO> buscarPorCurso(Long cursoId) {
        return coordenadorMapper.toResponseList(coordenadorRepository.findByCursoId(cursoId));
    }

    @Transactional
    public CoordenadorResponseDTO atualizarCoordenador(Long id, CoordenadorRequestDTO request) {
        CoordenadorEntity entity = coordenadorRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        if (coordenadorRepository.existsByProfessorIdAndIdNot(request.professorId(), id)) {
            throw new CoordenadorJaCadastratoException(id);
        }
        cursoClient.existeCurso(request.cursoId());
        ProfessorEntity professor = professorRepository.findById(request.professorId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(request.professorId()));

        entity.setProfessor(professor);
        entity.setCursoId(request.cursoId());

        CoordenadorEntity atualizado = coordenadorRepository.save(entity);
        return coordenadorMapper.toResponse(atualizado);
    }

    @Transactional
    public CoordenadorResponseDTO atualizarPerfilProprio(String subEmail, CoordenadorRequestDTO request) {
        CoordenadorEntity entity = coordenadorRepository.findByProfessor_Usuario_Email(subEmail)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(subEmail));

        String emailDoProfessor = entity.getProfessor().getUsuario().getEmail();
        if (!emailDoProfessor.equals(subEmail)) {
            throw new AcessoProibidoException("Você não tem permissão para atualizar este perfil.");
        }
        cursoClient.existeCurso(request.cursoId());

        entity.setCursoId(request.cursoId());

        CoordenadorEntity atualizado = coordenadorRepository.save(entity);
        return coordenadorMapper.toResponse(atualizado);
    }

    @Transactional
    public void deletarCoordenador(Long id) {
        CoordenadorEntity entity = coordenadorRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        coordenadorRepository.delete(entity);
    }
}
