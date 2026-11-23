package PortalTADS.usuarioService.service;

import PortalTADS.usuarioService.exception.FotoNaoEncontradaException;
import PortalTADS.usuarioService.exception.UsuarioJaTemFotoException;
import PortalTADS.usuarioService.exception.UsuarioNaoEncontradoException;
import PortalTADS.usuarioService.model.dto.foto.fotoRequestDTO;
import PortalTADS.usuarioService.model.dto.foto.fotoResponseDTO;
import PortalTADS.usuarioService.model.foto;
import PortalTADS.usuarioService.model.mapper.fotoMapper;
import PortalTADS.usuarioService.repository.entity.fotoEntity;
import PortalTADS.usuarioService.repository.fotoRepository;
import PortalTADS.usuarioService.repository.usuarioRepository;
import PortalTADS.usuarioService.service.storage.armazenarArquivos;
import PortalTADS.usuarioService.service.storage.processarImagem;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Service
@AllArgsConstructor
public class fotoService {

    private static final long MAX_BYTES = 2 * 1024 * 1024;
    private static final int TAMANHO = 256;

    private final processarImagem processarImagem;
    private final armazenarArquivos armazenarArquivos;

    private final usuarioRepository usuarioRepository;

    private final fotoMapper fotoMapper;
    private final fotoRepository fotoRepository;



    @Transactional
    public fotoResponseDTO salvarFoto(fotoRequestDTO request) {

        if(!usuarioRepository.existsById(request.getUsuarioId())){
            throw new UsuarioNaoEncontradoException(request.getUsuarioId());
        }

        if(fotoRepository.existsByUsuarioId(request.getUsuarioId())){
            throw new UsuarioJaTemFotoException(request.getUsuarioId());
        }


        byte[] png = processarImagem.paraPng(request.getFoto(), TAMANHO, MAX_BYTES);
        String nomeArquivo = armazenarArquivos.salvar("foto", "png", png);

        foto fotoModel = fotoMapper.toModel(request);
        fotoModel.setNomeArquivo(nomeArquivo);
        fotoModel.setNomeArquivo(nomeArquivo);
        fotoEntity entity = fotoMapper.toEntity(fotoModel);
        fotoRepository.save(entity);
        return fotoMapper.toResponseDTO(entity);

    }

    public Resource buscarFoto(Long usuarioId) {

        fotoEntity entity = fotoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));

        return armazenarArquivos.buscarArquivo(entity.getNomeArquivo())
                .orElseThrow(() -> new FotoNaoEncontradaException(usuarioId));
    }

    public fotoResponseDTO atualizarFoto(fotoRequestDTO request) {
        fotoEntity foto = fotoRepository.findByUsuarioId(request.getUsuarioId())
                .orElseThrow(() -> new FotoNaoEncontradaException(request.getUsuarioId()));

        byte[] bytes = processarImagem.paraPng(request.getFoto(), TAMANHO, MAX_BYTES);
        String novoNome = armazenarArquivos.atualizar(foto.getNomeArquivo(), "foto", "png", bytes);

        foto.setNomeArquivo(novoNome);
        fotoRepository.save(foto);
        return fotoMapper.toResponseDTO(foto);

    }
}
