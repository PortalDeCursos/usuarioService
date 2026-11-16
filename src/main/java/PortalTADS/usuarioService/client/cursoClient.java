package PortalTADS.usuarioService.client;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@AllArgsConstructor
public class cursoClient {

    private final WebClient webClient;

    public Boolean existeCurso(Long id) {

        return webClient.get()
                .uri("/{id}/existe", id)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
}
