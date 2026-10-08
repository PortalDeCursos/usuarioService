package PortalTADS.usuarioService.seeder;

import PortalTADS.usuarioService.enums.roleUsuario;
import PortalTADS.usuarioService.repository.entity.usuarioEntity;
import PortalTADS.usuarioService.repository.usuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class admSeeder {

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
            admin.setSenha("123456");
            admin.setTipoUsuario(roleUsuario.ADMIN);

            usuarioRepository.save(admin);
        };
    }

}
