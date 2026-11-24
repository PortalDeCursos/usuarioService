package PortalTADS.usuarioService.seeder;

import PortalTADS.usuarioService.enums.RoleUsuario;
import PortalTADS.usuarioService.enums.TitulacaoProfessor;
import PortalTADS.usuarioService.repository.*;
import PortalTADS.usuarioService.repository.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

/**
 * Seed de desenvolvimento.
 *
 * Premissa: a tabela "cursos" (serviço de cursos) já foi populada com ids de 1 a 110.
 * Este seeder NÃO valida isso. Se os cursos não existirem, os vínculos ficarão
 * apontando para ids inexistentes, sem erro no banco.
 */
@Component
@RequiredArgsConstructor
public class UsuariosSeeder implements CommandLineRunner {

    private static final String SENHA = "suasenha";
    private static final String DOMINIO = "@portalcuros.com";

    private static final String[] FOTOS = {
            "foto_perfil_1.png",
            "foto_perfil_2.png",
            "foto_perfil_3.png",
            "foto_perfil_4.png",
            "foto_perfil_5.png"
    };

    private static final int TOTAL_CURSOS = 110;
    private static final int TOTAL_ALUNOS = 220;
    private static final int TOTAL_PROFESSORES = 40;

    // Semente fixa: rodar de novo sorteia as mesmas titulações na mesma ordem
    private static final long SEMENTE = 42L;

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final CoordenadorRepository coordenadorRepository;
    private final FotoRepository fotoRepository;

    @Override
    @Transactional
    public void run(String... args) {
        Random random = new Random(SEMENTE);
        TitulacaoProfessor[] titulacoes = TitulacaoProfessor.values();

        // ADMIN
        UsuarioEntity admin = obterOuCriarUsuario(
                "Administrador", "silva.neto1@escolar.ifrn.edu.br", RoleUsuario.ADMIN);
        garantirFoto(admin, FOTOS[0]);

        // COORDENADORES: um por curso (1 a 110).
        // Cada coordenador tem um registro de professor do mesmo curso.
        for (int c = 1; c <= TOTAL_CURSOS; c++) {
            String email = (c == 1) ? "pjpedro150@gmail.com" : "coordenador" + c + DOMINIO;
            UsuarioEntity usuario = obterOuCriarUsuario(
                    "Coordenador " + c, email, RoleUsuario.COORDENADOR);
            garantirFoto(usuario, fotoDo(c));

            TitulacaoProfessor titulacao = sortearTitulacao(random, titulacoes);
            ProfessorEntity professor = garantirProfessor(
                    usuario, c, titulacao, "Coordenador do curso " + c);
            garantirCoordenador(professor, c);
        }

        // PROFESSORES: 40, distribuídos nos cursos 1 a 40
        for (int j = 1; j <= TOTAL_PROFESSORES; j++) {
            String email = (j == 1) ? "pjpedro158@gmail.com" : "professor" + j + DOMINIO;
            UsuarioEntity usuario = obterOuCriarUsuario(
                    "Professor " + j, email, RoleUsuario.PROFESSOR);
            garantirFoto(usuario, fotoDo(j));

            TitulacaoProfessor titulacao = sortearTitulacao(random, titulacoes);
            garantirProfessor(usuario, j, titulacao, "Professor de teste");
        }

        // ALUNOS: 220, dois por curso (ciclo sobre os 110 cursos)
        for (int i = 1; i <= TOTAL_ALUNOS; i++) {
            String email = (i == 1) ? "pjpedro152@gmail.com" : "aluno" + i + DOMINIO;
            UsuarioEntity usuario = obterOuCriarUsuario(
                    "Aluno " + i, email, RoleUsuario.ALUNO);
            garantirFoto(usuario, fotoDo(i));
            garantirAluno(usuario, cursoDoAluno(i));
        }
    }

    // ---------- regras de distribuição ----------

    private long cursoDoAluno(int i) {
        return ((i - 1) % TOTAL_CURSOS) + 1;
    }

    private String fotoDo(int indice) {
        return FOTOS[indice % FOTOS.length];
    }

    private TitulacaoProfessor sortearTitulacao(Random random, TitulacaoProfessor[] titulacoes) {
        // Sempre sorteia, mesmo se o registro já existir, para manter a sequência estável
        return titulacoes[random.nextInt(titulacoes.length)];
    }

    // ---------- helpers "garantir" (idempotentes) ----------

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

    private void garantirFoto(UsuarioEntity usuario, String nomeArquivo) {
        if (fotoRepository.existsByUsuarioId(usuario.getId())) {
            return;
        }
        FotoEntity foto = new FotoEntity();
        foto.setUsuario(usuario);
        foto.setNomeArquivo(nomeArquivo);
        fotoRepository.save(foto);
    }

    private ProfessorEntity garantirProfessor(UsuarioEntity usuario, long cursoId,
                                              TitulacaoProfessor titulacao, String descricao) {
        return professorRepository.findByUsuario_Id(usuario.getId()).orElseGet(() -> {
            ProfessorEntity professor = new ProfessorEntity();
            professor.setUsuario(usuario);
            professor.setCursoId(cursoId);
            professor.setTitulacao(titulacao);
            professor.setDescricao(descricao);
            return professorRepository.save(professor);
        });
    }

    private void garantirCoordenador(ProfessorEntity professor, long cursoId) {
        if (coordenadorRepository.existsByProfessor_Usuario_Id(professor.getUsuario().getId())) {
            return;
        }
        CoordenadorEntity coordenador = new CoordenadorEntity();
        coordenador.setProfessor(professor);
        coordenador.setCursoId(cursoId);
        coordenadorRepository.save(coordenador);
    }

    private void garantirAluno(UsuarioEntity usuario, long cursoId) {
        if (alunoRepository.existsByUsuario_Id(usuario.getId())) {
            return;
        }
        AlunoEntity aluno = new AlunoEntity();
        aluno.setUsuario(usuario);
        aluno.setCursoId(cursoId);
        alunoRepository.save(aluno);
    }
}