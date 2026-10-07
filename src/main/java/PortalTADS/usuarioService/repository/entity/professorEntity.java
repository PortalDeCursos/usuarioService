package PortalTADS.usuarioService.repository.entity;

import PortalTADS.usuarioService.enums.titulacaoProfessor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "professor")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class professorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private usuarioEntity usuario;

    @Column(name = "curso_id")
    private Long cursoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private titulacaoProfessor titulacao;

    @Column
    private String descricao;
}
