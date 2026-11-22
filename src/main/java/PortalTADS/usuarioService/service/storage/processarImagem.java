package PortalTADS.usuarioService.service.storage;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class processarImagem {

    private static final String TIPO_PNG = "image/png";

    private final Tika tika = new Tika();

    /** Valida que o arquivo é PNG pelo conteúdo e devolve os bytes redimensionados. */
    public byte[] paraPng(MultipartFile arquivo, int lado, long maxBytes) throws IOException {
        if (arquivo.isEmpty() || arquivo.getSize() > maxBytes) {
            throw new IllegalArgumentException("Arquivo vazio ou maior que o limite permitido");
        }

        String tipoReal;
        try (InputStream in = arquivo.getInputStream()) {
            tipoReal = tika.detect(in);
        }
        if (!TIPO_PNG.equals(tipoReal)) {
            throw new IllegalArgumentException("Apenas PNG é permitido");
        }

        try (InputStream in = arquivo.getInputStream()) {
            ByteArrayOutputStream saida = new ByteArrayOutputStream();
            Thumbnails.of(in)
                    .size(lado, lado)
                    .outputFormat("png")
                    .toOutputStream(saida);
            return saida.toByteArray();
        }
    }
}
