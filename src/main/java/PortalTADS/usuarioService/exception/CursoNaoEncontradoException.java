package PortalTADS.usuarioService.exception;

public class CursoNaoEncontradoException extends RuntimeException {
    public CursoNaoEncontradoException() {
        super("O curso informado não pode ser associado ao usuário");
    }

    public CursoNaoEncontradoException(String message) {
        super(message);
    }
}
