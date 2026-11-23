package PortalTADS.usuarioService.exception;

public class FotoNaoEncontradaException extends RuntimeException {
    public FotoNaoEncontradaException(Long usuarioId) {
        super("Foto não encontrada para o usuário com ID: " + usuarioId);
    }
}
