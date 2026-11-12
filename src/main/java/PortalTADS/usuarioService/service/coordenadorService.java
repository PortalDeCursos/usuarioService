package PortalTADS.usuarioService.service;

import PortalTADS.usuarioService.client.cursoClient;
import PortalTADS.usuarioService.exception.AcessoProibidoException;
import PortalTADS.usuarioService.exception.CoordenadorJaCadastratoException;
import PortalTADS.usuarioService.exception.UsuarioNaoEncontradoException;
import PortalTADS.usuarioService.model.coordenador;
import PortalTADS.usuarioService.model.dto.coordenador.coordenadorRequestDTO;
import PortalTADS.usuarioService.model.dto.coordenador.coordenadorResponseDTO;
import PortalTADS.usuarioService.model.mapper.coordenadorMapper;
import PortalTADS.usuarioService.repository.coordenadorRepository;
import PortalTADS.usuarioService.repository.entity.coordenadorEntity;
import PortalTADS.usuarioService.repository.entity.professorEntity;
import PortalTADS.usuarioService.repository.professorRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class coordenadorService {

    private final coordenadorRepository coordenadorRepository;
    private final professorRepository professorRepository;
    private final coordenadorMapper coordenadorMapper;
    private final cursoClient cursoClient;

    @Transactional
    public coordenadorResponseDTO criarCoordenador(coordenadorRequestDTO request) {

        if (coordenadorRepository.existsByProfessorId(request.professorId())) {
            throw new CoordenadorJaCadastratoException(request.professorId());
        }
        if (!cursoClient.existeCurso(request.cursoId())) {
            throw new IllegalArgumentException("Curso não encontrado.");
        }
        coordenador coordenadorModel = coordenadorMapper.toModel(request);

        professorEntity professor = professorRepository.findById(request.professorId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(request.professorId()));

        coordenadorEntity entity = coordenadorMapper.toEntity(coordenadorModel);
        entity.setProfessor(professor);
        coordenadorEntity salvo = coordenadorRepository.save(entity);
        return coordenadorMapper.toResponse(salvo);
    }

    public coordenadorResponseDTO buscarCoordenadorPorId(Long id) {
        coordenadorEntity entity = coordenadorRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        return coordenadorMapper.toResponse(entity);
    }

    public List<coordenadorResponseDTO> listarCoordenadores() {
        return coordenadorMapper.toResponseList(coordenadorRepository.findAll());
    }

    public List<coordenadorResponseDTO> buscarCoordenadoresPorNome(String nome) {
        return coordenadorMapper.toResponseList(coordenadorRepository.buscarPorNomeUsuario(nome));
    }

    public List<coordenadorResponseDTO> buscarPorCurso(Long cursoId) {
        return coordenadorMapper.toResponseList(coordenadorRepository.findByCursoId(cursoId));
    }

    @Transactional
    public coordenadorResponseDTO atualizarCoordenador(Long id, coordenadorRequestDTO request) {
        coordenadorEntity entity = coordenadorRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        if (coordenadorRepository.existsByProfessorIdAndIdNot(request.professorId(), id)) {
            throw new CoordenadorJaCadastratoException(id);
        }
        if (!cursoClient.existeCurso(request.cursoId())) {
            throw new IllegalArgumentException("Curso não encontrado.");
        }
        professorEntity professor = professorRepository.findById(request.professorId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(request.professorId()));

        entity.setProfessor(professor);
        entity.setCursoId(request.cursoId());

        coordenadorEntity atualizado = coordenadorRepository.save(entity);
        return coordenadorMapper.toResponse(atualizado);
    }

    @Transactional
    public coordenadorResponseDTO atualizarPerfilProprio(String subEmail, coordenadorRequestDTO request) {
        coordenadorEntity entity = coordenadorRepository.findByProfessor_Usuario_Email(subEmail)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(subEmail));

        String emailDoProfessor = entity.getProfessor().getUsuario().getEmail();
        if (!emailDoProfessor.equals(subEmail)) {
            throw new AcessoProibidoException("Você não tem permissão para atualizar este perfil.");
        }
        if (!cursoClient.existeCurso(request.cursoId())) {
            throw new IllegalArgumentException("Curso não encontrado.");
        }

        entity.setCursoId(request.cursoId());

        coordenadorEntity atualizado = coordenadorRepository.save(entity);
        return coordenadorMapper.toResponse(atualizado);
    }

    @Transactional
    public void deletarCoordenador(Long id) {
        coordenadorEntity entity = coordenadorRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        coordenadorRepository.delete(entity);
    }
}
