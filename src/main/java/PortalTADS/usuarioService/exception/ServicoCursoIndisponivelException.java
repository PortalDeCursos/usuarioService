package PortalTADS.usuarioService.exception;

public class ServicoCursoIndisponivelException extends RuntimeException {
    public ServicoCursoIndisponivelException() {
        super("Serviço dependente indisponível");
    }

    public ServicoCursoIndisponivelException(String message) {
        super(message);
    }

    public ServicoCursoIndisponivelException(String message, Throwable cause) {
        super(message, cause);
    }
}
