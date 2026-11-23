package PortalTADS.usuarioService.service;

import PortalTADS.usuarioService.model.mapper.UsuarioMapper;
import PortalTADS.usuarioService.repository.entity.UsuarioEntity;
import PortalTADS.usuarioService.repository.UsuarioRepository;
import PortalTADS.usuarioService.security.UserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        UsuarioEntity usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(username)
                );

        return new UserDetails(usuarioMapper.toModel(usuario));
    }
}
