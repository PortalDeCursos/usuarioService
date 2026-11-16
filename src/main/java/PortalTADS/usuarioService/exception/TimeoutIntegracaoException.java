package PortalTADS.usuarioService.exception;

public class TimeoutIntegracaoException extends RuntimeException {
    public TimeoutIntegracaoException() {
        super("O serviço não respondeu no prazo");
    }

    public TimeoutIntegracaoException(String message) {
        super(message);
    }

    public TimeoutIntegracaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
