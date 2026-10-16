package PortalTADS.usuarioService.service;

import PortalTADS.usuarioService.model.mapper.usuarioMapper;
import PortalTADS.usuarioService.repository.entity.usuarioEntity;
import PortalTADS.usuarioService.repository.usuarioRepository;
import PortalTADS.usuarioService.security.userDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class userDetailsServiceImpl implements UserDetailsService {

    private final usuarioRepository usuarioRepository;
    private final usuarioMapper usuarioMapper;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        usuarioEntity usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado")
                );

        return new userDetails(usuarioMapper.toModel(usuario));
    }
}
