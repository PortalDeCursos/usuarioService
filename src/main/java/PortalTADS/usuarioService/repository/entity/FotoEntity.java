package PortalTADS.usuarioService.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "foto_perfil")
@Entity
public class FotoEntity {

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
    private UsuarioEntity usuario;

    @Column(name = "nome_arquivo", nullable = false, length = 500)
    private String nomeArquivo;

}
