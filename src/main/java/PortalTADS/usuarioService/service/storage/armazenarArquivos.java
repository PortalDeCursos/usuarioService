package PortalTADS.usuarioService.service.storage;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class armazenarArquivos {

    private final Path diretorio;


    public armazenarArquivos(@Value("${app.upload-dir}") String uploadPasta) throws IOException {
        this.diretorio = Path.of(uploadPasta).toAbsolutePath().normalize();
        Files.createDirectories(this.diretorio);
    }


    /** Grava o conteúdo com um nome gerado pelo servidor e devolve esse nome. */
    public String salvar(String prefixo, String extensao, byte[] conteudo) throws IOException {
        String nome = "%s-%s.%s".formatted(prefixo, UUID.randomUUID(), extensao);
        Files.write(resolver(nome), conteudo);
        return nome;
    }

    public void apagar(String nome) throws IOException {
        if (nome == null) {
            return;
        }
        Files.deleteIfExists(resolver(nome));
    }


    private Path resolver(String nome) {
        Path destino = diretorio.resolve(nome).normalize();
        if (!destino.startsWith(diretorio)) {
            throw new IllegalArgumentException("Nome de arquivo inválido");
        }
        return destino;
    }
}
