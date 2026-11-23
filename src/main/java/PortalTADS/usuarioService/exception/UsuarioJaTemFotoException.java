package PortalTADS.usuarioService.exception;

public class UsuarioJaTemFotoException extends RuntimeException {
    public UsuarioJaTemFotoException(Long id) {
        super("Usuário com ID " + id + " já possui uma foto cadastrada");
    }
}
