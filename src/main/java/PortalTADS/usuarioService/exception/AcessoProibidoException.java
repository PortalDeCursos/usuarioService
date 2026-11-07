package PortalTADS.usuarioService.exception;

public class AcessoProibidoException extends RuntimeException {
    public AcessoProibidoException(String mensagem) {
        super(mensagem);
    }
}

