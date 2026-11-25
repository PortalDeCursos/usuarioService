package PortalTADS.usuarioService.seeder;

import PortalTADS.usuarioService.enums.RoleUsuario;
import PortalTADS.usuarioService.enums.TitulacaoProfessor;
import PortalTADS.usuarioService.repository.*;
import PortalTADS.usuarioService.repository.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * Seed de desenvolvimento.
 *
 * Premissas:
 * - A tabela "cursos" (serviço de cursos) já foi populada com ids de 1 a 110.
 *   Este seeder NÃO valida isso.
 * - Os usuários com email real (pjpedro150, pjpedro158 e pjpedro152) ficam
 *   todos no curso CURSO_EMAILS_REAIS.
 *
 * As fotos ficam em src/main/resources/seed/fotos e são copiadas para app.upload-dir
 * na primeira execução.
 */
@Component
@RequiredArgsConstructor
public class UsuariosSeeder implements CommandLineRunner {

    private static final String SENHA = "suasenha";
    private static final String DOMINIO = "@portalcuros.com";
    private static final String PASTA_FOTOS_SEED = "classpath:seed/fotos/*.png";

    private static final int TOTAL_CURSOS = 110;
    private static final int TOTAL_ALUNOS = 220;
    private static final int TOTAL_PROFESSORES = 40;

    // Curso onde ficam os usuários com email real (para teste)
    private static final long CURSO_EMAILS_REAIS = 1L;

    // Semente fixa: rodar de novo sorteia as mesmas titulações na mesma ordem
    private static final long SEMENTE = 42L;

    @Value("${app.upload-dir}")
    private String uploadDir;

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final CoordenadorRepository coordenadorRepository;
    private final FotoRepository fotoRepository;

    // Preenchido em run() com os nomes das fotos copiadas
    private String[] fotos;

    @Override
    @Transactional
    public void run(String... args) throws IOException {
        fotos = copiarFotosParaUpload();

        Random random = new Random(SEMENTE);
        TitulacaoProfessor[] titulacoes = TitulacaoProfessor.values();

        // ADMIN
        UsuarioEntity admin = obterOuCriarUsuario(
                "Administrador", "silva.neto1@escolar.ifrn.edu.br", RoleUsuario.ADMIN);
        garantirFoto(admin, fotoDo(0));

        // COORDENADORES: um por curso (1 a 110).
        // Cada coordenador tem um registro de professor do mesmo curso.
        for (int c = 1; c <= TOTAL_CURSOS; c++) {
            String email = (c == 1) ? "pjpedro150@gmail.com" : "coordenador" + c + DOMINIO;
            UsuarioEntity usuario = obterOuCriarUsuario(
                    "Coordenador " + c, email, RoleUsuario.COORDENADOR);
            garantirFoto(usuario, fotoDo(c));

            long cursoId = cursoDoCoordenador(c);
            TitulacaoProfessor titulacao = sortearTitulacao(random, titulacoes);
            ProfessorEntity professor = garantirProfessor(
                    usuario, cursoId, titulacao, "Coordenador do curso " + cursoId);
            garantirCoordenador(professor, cursoId);
        }

        // PROFESSORES: 40
        for (int j = 1; j <= TOTAL_PROFESSORES; j++) {
            String email = (j == 1) ? "pjpedro158@gmail.com" : "professor" + j + DOMINIO;
            UsuarioEntity usuario = obterOuCriarUsuario(
                    "Professor " + j, email, RoleUsuario.PROFESSOR);
            garantirFoto(usuario, fotoDo(j));

            TitulacaoProfessor titulacao = sortearTitulacao(random, titulacoes);
            garantirProfessor(usuario, cursoDoProfessor(j), titulacao, "Professor de teste");
        }

        // ALUNOS: 220
        for (int i = 1; i <= TOTAL_ALUNOS; i++) {
            String email = (i == 1) ? "pjpedro152@gmail.com" : "aluno" + i + DOMINIO;
            UsuarioEntity usuario = obterOuCriarUsuario(
                    "Aluno " + i, email, RoleUsuario.ALUNO);
            garantirFoto(usuario, fotoDo(i));
            garantirAluno(usuario, cursoDoAluno(i));
        }
    }

    // ---------- cópia das fotos ----------

    /**
     * Copia as fotos de classpath:seed/fotos para app.upload-dir.
     * Não sobrescreve arquivos que já existem na pasta de destino.
     * Devolve os nomes dos arquivos copiados, em ordem alfabética.
     */
    private String[] copiarFotosParaUpload() throws IOException {
        Resource[] recursos = new PathMatchingResourcePatternResolver()
                .getResources(PASTA_FOTOS_SEED);

        if (recursos.length == 0) {
            throw new IllegalStateException(
                    "Nenhuma foto encontrada em seed/fotos (src/main/resources/seed/fotos)");
        }

        Arrays.sort(recursos, Comparator.comparing(Resource::getFilename));

        Path destino = Path.of(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(destino);

        String[] nomes = new String[recursos.length];

        for (int i = 0; i < recursos.length; i++) {
            String nome = recursos[i].getFilename();
            nomes[i] = nome;

            Path arquivoDestino = destino.resolve(nome);
            if (Files.notExists(arquivoDestino)) {
                try (InputStream in = recursos[i].getInputStream()) {
                    Files.copy(in, arquivoDestino);
                }
            }
        }

        return nomes;
    }

    private String fotoDo(int indice) {
        return fotos[indice % fotos.length];
    }

    // ---------- regras de distribuição ----------

    private long cursoDoCoordenador(int c) {
        return (c == 1) ? CURSO_EMAILS_REAIS : c;
    }

    private long cursoDoProfessor(int j) {
        return (j == 1) ? CURSO_EMAILS_REAIS : j;
    }

    private long cursoDoAluno(int i) {
        return (i == 1) ? CURSO_EMAILS_REAIS : ((i - 1) % TOTAL_CURSOS) + 1;
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