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
    @JoinColumn(name = "professor_id", nullable = false)
    private professorEntity professor;

    @Column(name = "curso_id")
    private Long cursoId;
}
