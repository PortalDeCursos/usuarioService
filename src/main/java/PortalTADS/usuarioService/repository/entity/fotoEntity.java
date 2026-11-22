package PortalTADS.usuarioService.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "foto_perfil")
@Entity
public class fotoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "usuario_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_foto_perfil_usuario")
    )
    private usuarioEntity usuario;

    @Column(name = "nomeArquivo", nullable = false, length = 500)
    private String nomeArquivo;

}
