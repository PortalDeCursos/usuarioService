package PortalTADS.usuarioService.controller;


import PortalTADS.usuarioService.model.dto.foto.fotoRequestDTO;
import PortalTADS.usuarioService.model.dto.foto.fotoResponseDTO;
import PortalTADS.usuarioService.service.fotoService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.file.Path;

import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("/fotos")
public class fotoController {

    private final fotoService fotoService;

    @PostMapping
    public ResponseEntity<fotoResponseDTO> criarCoordenador(@ModelAttribute fotoRequestDTO request) {
        fotoResponseDTO response = fotoService.salvarFoto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<FileSystemResource> buscarFoto(@PathVariable Long usuarioId) {
        Path arquivo = fotoService.buscarFoto(usuarioId);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(new FileSystemResource(arquivo));
    }
}
