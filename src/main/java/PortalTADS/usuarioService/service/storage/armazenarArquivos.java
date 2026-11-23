package PortalTADS.usuarioService.service.storage;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import org.springframework.core.io.Resource;

@Component
public class armazenarArquivos {

    private final Path diretorio;


    public armazenarArquivos(@Value("${app.upload-dir}") String uploadPasta) throws IOException {
        this.diretorio = Path.of(uploadPasta).toAbsolutePath().normalize();
        Files.createDirectories(this.diretorio);
    }

    public Optional<Resource> buscarArquivo(String nome) {
        if (nome == null) return Optional.empty();
        Path arquivo = resolver(nome);
        if (!Files.isRegularFile(arquivo)) return Optional.empty();
        return Optional.of(new FileSystemResource(arquivo));
    }

    public String atualizar(String nomeAntigo, String prefixo, String extensao, byte[] conteudo) {
        String novoNome = salvar(prefixo, extensao, conteudo);
        apagar(nomeAntigo);
        return novoNome;
    }

    /** Grava o conteúdo com um nome gerado pelo servidor e devolve esse nome. */
    public String salvar(String prefixo, String extensao, byte[] conteudo) {
        String nome = "%s-%s.%s".formatted(prefixo, UUID.randomUUID(), extensao);
        try {
            Files.write(resolver(nome), conteudo);
            return nome;
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao gravar o arquivo", e);
        }
    }

    public void apagar(String nome) {
        if (nome == null) return;
        try {
            Files.deleteIfExists(resolver(nome));
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao apagar o arquivo", e);
        }
    }


    private Path resolver(String nome) {
        Path destino = diretorio.resolve(nome).normalize();
        if (!destino.startsWith(diretorio)) {
            throw new IllegalArgumentException("Nome de arquivo inválido");
        }
        return destino;
    }
}
