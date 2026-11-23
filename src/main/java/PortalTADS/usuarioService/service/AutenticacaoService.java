package PortalTADS.usuarioService.service;


import PortalTADS.usuarioService.security.TokenProvider;
import PortalTADS.usuarioService.security.UserDetails;
import PortalTADS.usuarioService.model.dto.login.LoginRequestDTO;
import PortalTADS.usuarioService.model.dto.login.LoginResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AutenticacaoService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication autenticado = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getSenha())
        );
        String token = tokenProvider.gerarToken(autenticado);
        String role = ((UserDetails) autenticado.getPrincipal()).getRole();
        return new LoginResponseDTO(token, tokenProvider.getExpiracao(), role);
    }
}
