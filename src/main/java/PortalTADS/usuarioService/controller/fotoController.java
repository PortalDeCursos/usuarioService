package PortalTADS.usuarioService.controller;


import PortalTADS.usuarioService.model.dto.foto.fotoRequestDTO;
import PortalTADS.usuarioService.model.dto.foto.fotoResponseDTO;
import PortalTADS.usuarioService.service.fotoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("/fotos")
public class fotoController {

    private final fotoService fotoService;

    @PostMapping
    public ResponseEntity<fotoResponseDTO> criarCoordenador(@RequestBody @Valid fotoRequestDTO request) {
        fotoResponseDTO response = fotoService.salvarFoto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
