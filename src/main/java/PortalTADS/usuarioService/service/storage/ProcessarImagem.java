package PortalTADS.usuarioService.service.storage;

import PortalTADS.usuarioService.exception.imagemInvalidaException;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ProcessarImagem {

    private static final String TIPO_PNG = "image/png";

    private final Tika tika = new Tika();

    /** Valida que o arquivo é PNG pelo conteúdo e devolve os bytes redimensionados. */
    public byte[] paraPng(MultipartFile arquivo, int lado, long maxBytes)   {
        // Erro de programação do chamador: não é culpa do usuário final
        if (lado <= 0) {
            throw new IllegalArgumentException("A dimensão deve ser positiva");
        }

        // Erros de entrada do cliente
        if (arquivo == null || arquivo.isEmpty()) {
            throw new imagemInvalidaException("Nenhum arquivo foi enviado");
        }
        if (arquivo.getSize() > maxBytes) {
            throw new imagemInvalidaException("Arquivo maior que o limite permitido");
        }

        String tipoReal;
        try (InputStream in = arquivo.getInputStream()) {
            tipoReal = tika.detect(in);
        } catch (IOException e) {
            throw new imagemInvalidaException("Não foi possível ler a imagem enviada", e);
        }
        if (!TIPO_PNG.equals(tipoReal)) {
            throw new imagemInvalidaException("Apenas PNG é permitido");
        }

        try (InputStream in = arquivo.getInputStream()) {
            ByteArrayOutputStream saida = new ByteArrayOutputStream();
            Thumbnails.of(in)
                    .size(lado, lado)
                    .outputFormat("png")
                    .toOutputStream(saida);
            return saida.toByteArray();
        } catch (IOException e) {
            // Falha ao decodificar: o Thumbnailator costuma lançar IIOException (subclasse de IOException)
            throw new imagemInvalidaException("Não foi possível processar a imagem enviada", e);
        }
    }
}
