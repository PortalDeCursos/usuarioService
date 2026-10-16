package PortalTADS.usuarioService.controller;

import PortalTADS.usuarioService.model.dto.login.loginRequestDTO;
import PortalTADS.usuarioService.model.dto.login.loginResponseDTO;
import PortalTADS.usuarioService.service.autenticacaoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class autenticacaoController {

    private final autenticacaoService autenticacaoService;

    @PostMapping("/login")
    public ResponseEntity<loginResponseDTO> login(@RequestBody @Valid loginRequestDTO loginRequestDTO) throws Exception {
        loginResponseDTO response = autenticacaoService.login(loginRequestDTO);
        return ResponseEntity.ok(response);
    }

}
