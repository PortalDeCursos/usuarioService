package PortalTADS.usuarioService.exception;

public class CoordenadorJaCadastratoException extends RuntimeException {
    public CoordenadorJaCadastratoException(Long id) {
        super("Professor com ID " + id + " já está cadastrado como coordenador");
    }
}
