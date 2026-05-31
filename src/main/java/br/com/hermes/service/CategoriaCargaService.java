package br.com.hermes.service;

import br.com.hermes.entity.CategoriaCarga;
import br.com.hermes.entity.Transportador;
import br.com.hermes.exception.NotFoundException;
import br.com.hermes.repository.CategoriaCargaRepository;
import br.com.hermes.repository.TransportadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoriaCargaService {

    private final CategoriaCargaRepository categoriaCargaRepository;
    private final TransportadorRepository transportadorRepository;

    public CategoriaCargaService(CategoriaCargaRepository categoriaCargaRepository,
                                 TransportadorRepository transportadorRepository) {
        this.categoriaCargaRepository = categoriaCargaRepository;
        this.transportadorRepository = transportadorRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaCarga> listarTodas() {
        return categoriaCargaRepository.findAll();
    }

    @Transactional
    public CategoriaCarga criar(CategoriaCarga categoria) {
        return categoriaCargaRepository.save(categoria);
    }

    @Transactional
    public void associarTransportador(Long categoriaId, Long transportadorId) {
        CategoriaCarga categoria = categoriaCargaRepository.findById(categoriaId)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
        Transportador transportador = transportadorRepository.findById(transportadorId)
                .orElseThrow(() -> new NotFoundException("Transportador não encontrado"));

        if (!transportador.getCategoriasCarga().contains(categoria)) {
            transportador.getCategoriasCarga().add(categoria);
            transportadorRepository.save(transportador);
        }
    }
}