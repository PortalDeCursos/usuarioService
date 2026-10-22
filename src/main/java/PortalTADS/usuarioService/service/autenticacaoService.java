package PortalTADS.usuarioService.service;


import PortalTADS.usuarioService.security.tokenProvider;
import PortalTADS.usuarioService.model.dto.login.loginRequestDTO;
import PortalTADS.usuarioService.model.dto.login.loginResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class autenticacaoService {

    private final AuthenticationManager authenticationManager;
    private final tokenProvider tokenProvider;

    public loginResponseDTO login(loginRequestDTO loginRequestDTO) throws Exception {
        try {
            Authentication autenticado = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getSenha()));
            String token = tokenProvider.gerarToken(autenticado);
            return new loginResponseDTO(token, tokenProvider.getExpiracao(), tokenProvider.getRole(token));
        } catch (Exception e) {
            throw new Exception("Erro ao autenticar usuário", e);
        }

    }
}
