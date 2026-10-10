package PortalTADS.usuarioService.controller;

import PortalTADS.usuarioService.model.dto.professor.professorRequestDTO;
import PortalTADS.usuarioService.model.dto.professor.professorResponseDTO;
import PortalTADS.usuarioService.repository.professorRepository;
import PortalTADS.usuarioService.service.professorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/professores")
@AllArgsConstructor
public class professorController {

    private final professorService professorService;

    @PostMapping
    public ResponseEntity<professorResponseDTO> criarProfessor(@RequestBody professorRequestDTO request) {

        professorResponseDTO  response = professorService.criarProfessor(request);
        URI localizacao = URI.create("/professores/" + response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).location(localizacao).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<professorResponseDTO> buscarProfessorPorId(@PathVariable Long id) {
        professorResponseDTO response = professorService.buscarProfessorPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<professorResponseDTO>> buscarProfessores() {
        List<professorResponseDTO> professores = professorService.listarProfessores();
        return ResponseEntity.ok(professores);
    }

    @GetMapping("/busca")
    public ResponseEntity<List<professorResponseDTO>> buscarProfessoresPorNome(@RequestParam String nome) {
        List<professorResponseDTO> professores = professorService.buscarProfessoresPorNome(nome);
        return ResponseEntity.ok(professores);
    }



}
