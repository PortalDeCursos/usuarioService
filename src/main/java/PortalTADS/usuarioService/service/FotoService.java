package PortalTADS.usuarioService.service;

import PortalTADS.usuarioService.exception.FotoNaoEncontradaException;
import PortalTADS.usuarioService.exception.UsuarioJaTemFotoException;
import PortalTADS.usuarioService.exception.UsuarioNaoEncontradoException;
import PortalTADS.usuarioService.model.Foto;
import PortalTADS.usuarioService.model.dto.foto.FotoRequestDTO;
import PortalTADS.usuarioService.model.dto.foto.FotoResponseDTO;
import PortalTADS.usuarioService.model.mapper.FotoMapper;
import PortalTADS.usuarioService.repository.entity.FotoEntity;
import PortalTADS.usuarioService.repository.FotoRepository;
import PortalTADS.usuarioService.repository.UsuarioRepository;
import PortalTADS.usuarioService.service.storage.ArmazenarArquivos;
import PortalTADS.usuarioService.service.storage.ProcessarImagem;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class FotoService {

    private static final long MAX_BYTES = 2 * 1024 * 1024;
    private static final int TAMANHO = 256;

    private final ProcessarImagem processarImagem;
    private final ArmazenarArquivos armazenarArquivos;

    private final UsuarioRepository usuarioRepository;

    private final FotoMapper fotoMapper;
    private final FotoRepository fotoRepository;



    @Transactional
    public FotoResponseDTO salvarFoto(FotoRequestDTO request) {

        if(!usuarioRepository.existsById(request.getUsuarioId())){
            throw new UsuarioNaoEncontradoException(request.getUsuarioId());
        }

        if(fotoRepository.existsByUsuarioId(request.getUsuarioId())){
            throw new UsuarioJaTemFotoException(request.getUsuarioId());
        }


        byte[] png = processarImagem.paraPng(request.getFoto(), TAMANHO, MAX_BYTES);
        String nomeArquivo = armazenarArquivos.salvar("foto", "png", png);

        Foto fotoModel = fotoMapper.toModel(request);
        fotoModel.setNomeArquivo(nomeArquivo);
        fotoModel.setNomeArquivo(nomeArquivo);
        FotoEntity entity = fotoMapper.toEntity(fotoModel);
        fotoRepository.save(entity);
        return fotoMapper.toResponseDTO(entity);

    }

    public Resource buscarFoto(Long usuarioId) {

        FotoEntity entity = fotoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));

        return armazenarArquivos.buscarArquivo(entity.getNomeArquivo())
                .orElseThrow(() -> new FotoNaoEncontradaException(usuarioId));
    }

    public FotoResponseDTO atualizarFoto(FotoRequestDTO request) {
        FotoEntity foto = fotoRepository.findByUsuarioId(request.getUsuarioId())
                .orElseThrow(() -> new FotoNaoEncontradaException(request.getUsuarioId()));

        byte[] bytes = processarImagem.paraPng(request.getFoto(), TAMANHO, MAX_BYTES);
        String novoNome = armazenarArquivos.atualizar(foto.getNomeArquivo(), "foto", "png", bytes);

        foto.setNomeArquivo(novoNome);
        fotoRepository.save(foto);
        return fotoMapper.toResponseDTO(foto);

    }
}
