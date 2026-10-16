package PortalTADS.usuarioService.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
        UserDetails usuario = (UserDetails) authentication.getPrincipal();
        return buildToken(usuario.getUsername());
    }

    private String buildToken(String username) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + tempoExpiracao);

        return Jwts.builder()
                .subject(username)
                .setIssuedAt(agora)
                .setExpiration(expiracao)
                .signWith(getSegredoAssinado())
                .compact();

    }

    private SecretKey getSegredoAssinado() {
        return Keys.hmacShaKeyFor( segredo.getBytes());
    }

    public boolean validarToken(String token) {

        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSegredoAssinado())
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }


    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public long getExpiracao() {
        return tempoExpiracao;
    }
}
