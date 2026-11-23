package PortalTADS.usuarioService.exception;

public class imagemInvalidaException extends RuntimeException {
    public imagemInvalidaException(String mensagem) {
        super(mensagem);
    }

    public imagemInvalidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
