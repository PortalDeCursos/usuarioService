package PortalTADS.usuarioService.seeder;

import PortalTADS.usuarioService.enums.roleUsuario;
import PortalTADS.usuarioService.enums.titulacaoProfessor;
import PortalTADS.usuarioService.repository.alunoRepository;
import PortalTADS.usuarioService.repository.entity.alunoEntity;
import PortalTADS.usuarioService.repository.entity.coordenadorEntity;
import PortalTADS.usuarioService.repository.entity.professorEntity;
import PortalTADS.usuarioService.repository.entity.usuarioEntity;
import PortalTADS.usuarioService.repository.coordenadorRepository;
import PortalTADS.usuarioService.repository.professorRepository;
import PortalTADS.usuarioService.repository.usuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class usuariosSeeder {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner criarUsuariosIniciais(
            usuarioRepository usuarioRepository,
            alunoRepository alunoRepository,
            professorRepository professorRepository,
            coordenadorRepository coordenadorRepository) {

        return args -> criarDadosIniciais(
                usuarioRepository,
                alunoRepository,
                professorRepository,
                coordenadorRepository
        );
    }

    @Transactional
    public void criarDadosIniciais(
            usuarioRepository usuarioRepository,
            alunoRepository alunoRepository,
            professorRepository professorRepository,
            coordenadorRepository coordenadorRepository) {

        // ADMIN
        usuarioEntity admin = criarUsuarioSeNaoExistir(
                usuarioRepository,
                "Administrador",
                "admin@portaltads.com",
                roleUsuario.ADMIN
        );

        // ALUNO
        usuarioEntity usuarioAluno = criarUsuarioSeNaoExistir(
                usuarioRepository,
                "Aluno Inicial",
                "aluno@portaltads.com",
                roleUsuario.ALUNO
        );

        if (usuarioAluno != null
                && !alunoRepository.existsByUsuario_Id(usuarioAluno.getId())) {

            alunoEntity aluno = new alunoEntity();
            aluno.setUsuario(usuarioAluno);
            aluno.setCursoId(1L);

            alunoRepository.save(aluno);
        }

        // PROFESSOR
        usuarioEntity usuarioProfessor = criarUsuarioSeNaoExistir(
                usuarioRepository,
                "Professor Inicial",
                "professor@portaltads.com",
                roleUsuario.PROFESSOR
        );

        if (usuarioProfessor != null
                && !professorRepository.existsByUsuario_Id(usuarioProfessor.getId())) {

            professorEntity professor = new professorEntity();
            professor.setUsuario(usuarioProfessor);
            professor.setCursoId(1L);
            professor.setTitulacao(titulacaoProfessor.MESTRADO);
            professor.setDescricao("Professor de teste");

            professorRepository.save(professor);
        }

        // COORDENADOR
        usuarioEntity usuarioCoordenador = criarUsuarioSeNaoExistir(
                usuarioRepository,
                "Coordenador Inicial",
                "coordenador@portaltads.com",
                roleUsuario.COORDENADOR
        );

        if (usuarioCoordenador != null
                && !coordenadorRepository
                .existsByProfessor_Usuario_Id(usuarioCoordenador.getId())) {

            // O coordenador referencia um professor.
            // Como o usuário tem a role COORDENADOR, criamos
            // também o registro de professor correspondente.
            professorEntity professorCoordenador =
                    professorRepository.findByUsuario_Id( usuarioCoordenador.getId())
                            .orElse(null);

            if (professorCoordenador == null) {
                professorCoordenador = new professorEntity();
                professorCoordenador.setUsuario(usuarioCoordenador);
                professorCoordenador.setCursoId(1L);
                professorCoordenador.setTitulacao(
                        titulacaoProfessor.MESTRADO
                );
                professorCoordenador.setDescricao(
                        "Professor responsável pela coordenação"
                );

                professorCoordenador =
                        professorRepository.save(professorCoordenador);
            }

            coordenadorEntity coordenador = new coordenadorEntity();
            coordenador.setProfessor(professorCoordenador);
            coordenador.setCursoId(1L);

            coordenadorRepository.save(coordenador);
        }
    }

    private usuarioEntity criarUsuarioSeNaoExistir(
            usuarioRepository usuarioRepository,
            String nome,
            String email,
            roleUsuario role) {

        if (usuarioRepository.existsByEmail(email)) {
            return null;
        }

        usuarioEntity usuario = new usuarioEntity();

        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode("suasenha"));
        usuario.setTipoUsuario(role);

        return usuarioRepository.save(usuario);
    }



}
