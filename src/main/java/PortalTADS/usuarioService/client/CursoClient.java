package PortalTADS.usuarioService.client;

import PortalTADS.usuarioService.exception.CursoNaoEncontradoException;
import PortalTADS.usuarioService.exception.ErroIntegracaoException;
import PortalTADS.usuarioService.exception.ServicoCursoIndisponivelException;
import PortalTADS.usuarioService.exception.TimeoutIntegracaoException;
import PortalTADS.usuarioService.model.dto.curso.CursoResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Component
@AllArgsConstructor
public class CursoClient {

    private final WebClient webClient;

    public Boolean existeCurso(Long id) {
        try {
            CursoResponseDTO curso = webClient.get()
                    .uri("/cursos/{id}", id)
                    .retrieve()
                    .onStatus(status -> status.value() == 404, response -> Mono.error(new CursoNaoEncontradoException()))
                    .onStatus(status -> status.value() == 400, response -> Mono.error(new CursoNaoEncontradoException()))
                    .onStatus(status -> status.value() == 503, response -> Mono.error(new ServicoCursoIndisponivelException()))
                    .onStatus(status -> status.value() == 504, response -> Mono.error(new TimeoutIntegracaoException()))
                    .onStatus(HttpStatusCode::isError, response -> Mono.error(new ErroIntegracaoException()))
                    .bodyToMono(CursoResponseDTO.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();

            if (curso == null || curso.getId() == null) {
                throw new CursoNaoEncontradoException();
            }

            return true;
        } catch (CursoNaoEncontradoException | ServicoCursoIndisponivelException | TimeoutIntegracaoException | ErroIntegracaoException e) {
            throw e;
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (e instanceof TimeoutException || cause instanceof TimeoutException) {
                throw new TimeoutIntegracaoException();
            }
            if (e instanceof WebClientRequestException || (cause != null && cause instanceof java.net.ConnectException)) {
                throw new ServicoCursoIndisponivelException("Serviço dependente indisponível", e);
            }
            if (e instanceof WebClientResponseException respEx) {
                if (respEx.getStatusCode() == HttpStatus.NOT_FOUND || respEx.getStatusCode() == HttpStatus.BAD_REQUEST || respEx.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                    throw new CursoNaoEncontradoException();
                } else if (respEx.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                    throw new ServicoCursoIndisponivelException();
                } else if (respEx.getStatusCode() == HttpStatus.GATEWAY_TIMEOUT) {
                    throw new TimeoutIntegracaoException();
                } else {
                    throw new ErroIntegracaoException();
                }
            }
            throw new ErroIntegracaoException("Resposta inesperada do serviço dependente", e);
        }
    }
}
