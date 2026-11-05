package PortalTADS.usuarioService.exception;

public class TokenGeracaoException extends RuntimeException {

    public TokenGeracaoException(String message) {
        super(message);
    }

    public TokenGeracaoException(String message, Throwable cause) {
        super(message, cause);
    }
}

