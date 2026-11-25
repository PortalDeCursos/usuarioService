package PortalTADS.usuarioService.seeder;

import PortalTADS.usuarioService.enums.RoleUsuario;
import PortalTADS.usuarioService.enums.TitulacaoProfessor;
import PortalTADS.usuarioService.repository.*;
import PortalTADS.usuarioService.repository.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Profile("test")
@RequiredArgsConstructor
public class UsuariosSeederTeste implements CommandLineRunner {

    private static final String SENHA = "suasenha";
    private static final String DOMINIO = "@portaltads.com";
    private static final long CURSO_TESTE = 1L;

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final CoordenadorRepository coordenadorRepository;

    @Override
    @Transactional
    public void run(String... args) {

        obterOuCriarUsuario("Administrador Teste", "admin" + DOMINIO, RoleUsuario.ADMIN);


        UsuarioEntity aluno = obterOuCriarUsuario(
                "Aluno Teste", "aluno.teste" + DOMINIO, RoleUsuario.ALUNO);
        garantirAluno(aluno);

        UsuarioEntity professor = obterOuCriarUsuario(
                "Professor Teste", "professor.teste" + DOMINIO, RoleUsuario.PROFESSOR);
        garantirProfessor(professor, "Professor de teste");

        UsuarioEntity coordenador = obterOuCriarUsuario(
                "Coordenador Teste", "coordenador.teste" + DOMINIO, RoleUsuario.COORDENADOR);
        ProfessorEntity profCoordenador = garantirProfessor(coordenador, "Coordenador de teste");
        garantirCoordenador(profCoordenador);
    }

    private UsuarioEntity obterOuCriarUsuario(String nome, String email, RoleUsuario role) {
        return usuarioRepository.findByEmail(email).orElseGet(() -> {
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(passwordEncoder.encode(SENHA));
            usuario.setTipoUsuario(role);
            return usuarioRepository.save(usuario);
        });
    }

    private void garantirAluno(UsuarioEntity usuario) {
        if (alunoRepository.existsByUsuario_Id(usuario.getId())) {
            return;
        }
        AlunoEntity aluno = new AlunoEntity();
        aluno.setUsuario(usuario);
        aluno.setCursoId(CURSO_TESTE);
        alunoRepository.save(aluno);
    }

    private ProfessorEntity garantirProfessor(UsuarioEntity usuario, String descricao) {
        return professorRepository.findByUsuario_Id(usuario.getId()).orElseGet(() -> {
            ProfessorEntity professor = new ProfessorEntity();
            professor.setUsuario(usuario);
            professor.setCursoId(CURSO_TESTE);
            professor.setTitulacao(TitulacaoProfessor.MESTRADO);
            professor.setDescricao(descricao);
            return professorRepository.save(professor);
        });
    }

    private void garantirCoordenador(ProfessorEntity professor) {
        if (coordenadorRepository.existsByProfessor_Usuario_Id(professor.getUsuario().getId())) {
            return;
        }
        CoordenadorEntity coordenador = new CoordenadorEntity();
        coordenador.setProfessor(professor);
        coordenador.setCursoId(CURSO_TESTE);
        coordenadorRepository.save(coordenador);
    }
}