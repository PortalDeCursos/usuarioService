package PortalTADS.usuarioService.exception;

public class ErroIntegracaoException extends RuntimeException {
    public ErroIntegracaoException() {
        super("Resposta inesperada do serviço dependente");
    }

    public ErroIntegracaoException(String message) {
        super(message);
    }

    public ErroIntegracaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
