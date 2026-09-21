package PortalTADS.usuarioService.controller;

import PortalTADS.usuarioService.model.dto.usuarioRequestDTO;
import PortalTADS.usuarioService.model.dto.usuarioResponseDTO;
import PortalTADS.usuarioService.service.usuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class usuarioController {

    private final usuarioService service;

    @PostMapping
    public ResponseEntity salvarUsuario(@RequestBody usuarioRequestDTO record) {
        usuarioResponseDTO response = service.salvarUsuario(record);
        URI location = URI.create("/usuarios/" + response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(response);

    }
}
