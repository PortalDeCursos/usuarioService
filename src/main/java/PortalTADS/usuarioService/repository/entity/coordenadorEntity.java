package PortalTADS.usuarioService.repository.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coordenador")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class coordenadorEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private usuarioEntity usuario;

    @Column(name = "curso_id")
    private Long cursoId;
}
