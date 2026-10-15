package PortalTADS.usuarioService.seeder;

import PortalTADS.usuarioService.enums.roleUsuario;
import PortalTADS.usuarioService.repository.entity.usuarioEntity;
import PortalTADS.usuarioService.repository.usuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class admSeeder {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner criarAdminInicial(usuarioRepository usuarioRepository) {
        return args -> {

            if (usuarioRepository.existsByTipoUsuario(roleUsuario.ADMIN)) {
                return;
            }

            usuarioEntity admin = new usuarioEntity();

            admin.setNome("Administrador");
            admin.setEmail("admin@portaltads.com");
            admin.setUsername("admin");
            admin.setSenha(passwordEncoder.encode("suasenha"));
            admin.setTipoUsuario(roleUsuario.ADMIN);

            usuarioRepository.save(admin);
        };
    }

}
