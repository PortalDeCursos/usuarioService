package PortalTADS.usuarioService.controller;

import PortalTADS.usuarioService.model.dto.professor.professorRequestDTO;
import PortalTADS.usuarioService.model.dto.professor.professorResponseDTO;
import PortalTADS.usuarioService.service.professorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

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

}
