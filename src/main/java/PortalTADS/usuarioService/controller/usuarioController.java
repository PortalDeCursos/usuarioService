package PortalTADS.usuarioService.controller;

import PortalTADS.usuarioService.model.dto.usuarioRequestDTO;
import PortalTADS.usuarioService.model.dto.usuarioResponseDTO;
import PortalTADS.usuarioService.service.usuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
//////
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

    @GetMapping("/{id}")
    public ResponseEntity<usuarioResponseDTO> buscarUsuario(@PathVariable Long id) {
        usuarioResponseDTO response = service.buscarUsuario(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<usuarioResponseDTO> atualizarUsuario(@PathVariable Long id, @RequestBody usuarioRequestDTO record) {
        usuarioResponseDTO response = service.atualizarUsuario(id, record);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
