package PortalTADS.usuarioService.controller;

import PortalTADS.usuarioService.model.dto.foto.fotoRequestDTO;
import PortalTADS.usuarioService.model.dto.foto.fotoResponseDTO;
import PortalTADS.usuarioService.service.fotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/fotos")
@AllArgsConstructor
@Tag(name = "Fotos", description = "Endpoints para gerenciamento de fotos de perfil dos usuários (upload, consulta e atualização)")
public class fotoController {

        private final fotoService fotoService;

    @Operation(
            summary = "Cadastrar foto de perfil",
            description = """
                    Realiza o upload da foto de perfil de um usuário.
                    
                    A imagem enviada é validada, convertida e redimensionada para formato PNG (256x256).
                    O tamanho máximo suportado é de 2 MB.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Foto cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = fotoResponseDTO.class)),
                    headers = @Header(name = "Location", description = "URI do recurso criado", schema = @Schema(type = "string", example = "/fotos/1"))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos — arquivo de imagem inválido ou campos obrigatórios ausentes",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado para o ID informado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito — o usuário já possui uma foto cadastrada",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "413",
                    description = "Arquivo excede o tamanho máximo permitido (2 MB)",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<fotoResponseDTO> salvarFoto(@Valid @ModelAttribute fotoRequestDTO request) {
        fotoResponseDTO response = fotoService.salvarFoto(request);
        URI location = URI.create("/fotos/" + response.getUsuarioId());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(response);
    }

    @Operation(
            summary = "Buscar foto de perfil",
            description = """
                    Recupera o arquivo binário da imagem de perfil de um usuário específico a partir do seu ID.
                    Retorna a imagem diretamente no formato `image/png`.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Foto encontrada com sucesso",
                    content = @Content(
                            mediaType = MediaType.IMAGE_PNG_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário ou foto não encontrada para o ID informado",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    @GetMapping(value = "/{usuarioId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> buscarFoto(
            @Parameter(description = "Identificador único do usuário cuja foto será consultada", example = "1", required = true)
            @PathVariable Long usuarioId) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(fotoService.buscarFoto(usuarioId));
    }

        @Operation(summary = "Atualizar foto de perfil", description = """
                        Atualiza a foto de perfil existente de um usuário, substituindo o arquivo anterior pela nova imagem.

                        A imagem enviada é validada, convertida e redimensionada para formato PNG (256x256).
                        O tamanho máximo suportado é de 2 MB.
                        """)
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Foto atualizada com sucesso", content = @Content(schema = @Schema(implementation = fotoResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos — arquivo de imagem inválido ou campos obrigatórios ausentes", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "404", description = "Usuário ou foto não encontrada para o ID informado", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "413", description = "Arquivo excede o tamanho máximo permitido (2 MB)", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<fotoResponseDTO> atualizarFoto(@Valid @ModelAttribute fotoRequestDTO request) {
                fotoResponseDTO response = fotoService.atualizarFoto(request);
                return ResponseEntity.ok(response);
        }
}
