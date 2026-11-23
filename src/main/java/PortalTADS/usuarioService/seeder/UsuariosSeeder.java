package PortalTADS.usuarioService.seeder;

import PortalTADS.usuarioService.enums.RoleUsuario;
import PortalTADS.usuarioService.enums.TitulacaoProfessor;
import PortalTADS.usuarioService.repository.AlunoRepository;
import PortalTADS.usuarioService.repository.entity.*;
import PortalTADS.usuarioService.repository.CoordenadorRepository;
import PortalTADS.usuarioService.repository.ProfessorRepository;
import PortalTADS.usuarioService.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class UsuariosSeeder {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner criarUsuariosIniciais(
            UsuarioRepository usuarioRepository,
            AlunoRepository alunoRepository,
            ProfessorRepository professorRepository,
            CoordenadorRepository coordenadorRepository) {

        return args -> criarDadosIniciais(
                usuarioRepository,
                alunoRepository,
                professorRepository,
                coordenadorRepository
        );
    }

    @Transactional
    public void criarDadosIniciais(
            UsuarioRepository usuarioRepository,
            AlunoRepository alunoRepository,
            ProfessorRepository professorRepository,
            CoordenadorRepository coordenadorRepository) {

        // ADMIN
        UsuarioEntity admin = criarUsuarioSeNaoExistir(
                usuarioRepository,
                "Administrador",
                "admin@portaltads.com",
                RoleUsuario.ADMIN
        );

        // ALUNO
        UsuarioEntity usuarioAluno = criarUsuarioSeNaoExistir(
                usuarioRepository,
                "Aluno Inicial",
                "aluno@portaltads.com",
                RoleUsuario.ALUNO
        );

        if (usuarioAluno != null
                && !alunoRepository.existsByUsuario_Id(usuarioAluno.getId())) {

            AlunoEntity aluno = new AlunoEntity();
            aluno.setUsuario(usuarioAluno);
            aluno.setCursoId(1L);

            alunoRepository.save(aluno);
        }

        // PROFESSOR
        UsuarioEntity usuarioProfessor = criarUsuarioSeNaoExistir(
                usuarioRepository,
                "Professor Inicial",
                "professor@portaltads.com",
                RoleUsuario.PROFESSOR
        );

        if (usuarioProfessor != null
                && !professorRepository.existsByUsuario_Id(usuarioProfessor.getId())) {

            ProfessorEntity professor = new ProfessorEntity();
            professor.setUsuario(usuarioProfessor);
            professor.setCursoId(1L);
            professor.setTitulacao(TitulacaoProfessor.MESTRADO);
            professor.setDescricao("Professor de teste");

            professorRepository.save(professor);
        }

        // COORDENADOR
        UsuarioEntity usuarioCoordenador = criarUsuarioSeNaoExistir(
                usuarioRepository,
                "Coordenador Inicial",
                "coordenador@portaltads.com",
                RoleUsuario.COORDENADOR
        );

        if (usuarioCoordenador != null
                && !coordenadorRepository
                .existsByProfessor_Usuario_Id(usuarioCoordenador.getId())) {

            // O coordenador referencia um professor.
            // Como o usuário tem a role COORDENADOR, criamos
            // também o registro de professor correspondente.
            ProfessorEntity professorCoordenador =
                    professorRepository.findByUsuario_Id( usuarioCoordenador.getId())
                            .orElse(null);

            if (professorCoordenador == null) {
                professorCoordenador = new ProfessorEntity();
                professorCoordenador.setUsuario(usuarioCoordenador);
                professorCoordenador.setCursoId(1L);
                professorCoordenador.setTitulacao(
                        TitulacaoProfessor.MESTRADO
                );
                professorCoordenador.setDescricao(
                        "Professor responsável pela coordenação"
                );

                professorCoordenador =
                        professorRepository.save(professorCoordenador);
            }

            CoordenadorEntity coordenador = new CoordenadorEntity();
            coordenador.setProfessor(professorCoordenador);
            coordenador.setCursoId(1L);

            coordenadorRepository.save(coordenador);
        }
    }

    private UsuarioEntity criarUsuarioSeNaoExistir(
            UsuarioRepository usuarioRepository,
            String nome,
            String email,
            RoleUsuario role) {

        if (usuarioRepository.existsByEmail(email)) {
            return null;
        }

        UsuarioEntity usuario = new UsuarioEntity();

        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode("suasenha"));
        usuario.setTipoUsuario(role);

        return usuarioRepository.save(usuario);
    }



}
