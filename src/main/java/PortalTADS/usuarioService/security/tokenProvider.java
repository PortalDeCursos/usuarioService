package PortalTADS.usuarioService.security;


import PortalTADS.usuarioService.enums.roleUsuario;
import PortalTADS.usuarioService.exception.TokenGeracaoException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class tokenProvider {

    @Value("${jwt.expiration}")
    private long tempoExpiracao;

    @Value("${jwt.secret}")
    private String segredo;

    public String gerarToken(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new TokenGeracaoException("A autenticação e o usuário principal não podem ser nulos");
        }
        if (!(authentication.getPrincipal() instanceof userDetails usuario)) {
            throw new TokenGeracaoException("O principal da autenticação deve ser uma instância de userDetails válida");
        }
        if (usuario.getUsername() == null || usuario.getUsername().isBlank()) {
            throw new TokenGeracaoException("O username/e-mail do usuário não pode ser nulo ou vazio");
        }
        if (usuario.getRole() == null || usuario.getRole().isBlank()) {
            throw new TokenGeracaoException("A role do usuário não pode ser nula ou vazia");
        }

        try {
            return buildToken(usuario.getUsername(), usuario.getRole());
        } catch (Exception e) {
            throw new TokenGeracaoException("Erro ao construir o token JWT: " + e.getMessage(), e);
        }
    }


    private String buildToken(String username, String role) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + tempoExpiracao);

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .setIssuedAt(agora)
                .setExpiration(expiracao)
                .signWith(getSegredoAssinado())
                .compact();

    }

    private SecretKey getSegredoAssinado() {
        return Keys.hmacShaKeyFor( segredo.getBytes());
    }

//    public boolean validarToken(String token) {
//
//        try {
//            getClaims(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//
//    }
//
//    private Claims getClaims(String token) {
//        return Jwts.parser()
//                .verifyWith(getSegredoAssinado())
//                .build()
//                .parseClaimsJws(token)
//                .getPayload();
//    }
//
//
//    public String getUsername(String token) {
//        return getClaims(token).getSubject();
//    }
//
//    public String getRole(String token) {
//        return getClaims(token).get("role", String.class);
//    }

    public long getExpiracao() {
        return tempoExpiracao;
    }
}
