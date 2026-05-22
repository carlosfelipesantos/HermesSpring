package br.com.hermes.service;

import br.com.hermes.dto.response.TransportadorResponse;
import br.com.hermes.entity.Transportador;
import br.com.hermes.exception.BusinessException;
import br.com.hermes.exception.NotFoundException;
import br.com.hermes.repository.TransportadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransportadorService {

    private final TransportadorRepository transportadorRepository;

    // ✅ Construtor explícito (SEM Lombok, SEM ModelMapper)
    public TransportadorService(TransportadorRepository transportadorRepository) {
        this.transportadorRepository = transportadorRepository;
    }

    @Transactional(readOnly = true)
    public List<TransportadorResponse> listarTodos() {
        return transportadorRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TransportadorResponse buscarPorId(Long id) {
        Transportador transportador = transportadorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transportador não encontrado"));
        return toResponse(transportador);
    }

    @Transactional(readOnly = true)
    public Transportador buscarPorIdEntity(Long id) {
        return transportadorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transportador não encontrado"));
    }

    @Transactional
    public TransportadorResponse criar(Transportador transportador) {
        if (transportadorRepository.existsByEmail(transportador.getEmail())) {
            throw new BusinessException("Email já está em uso");
        }
        transportador = transportadorRepository.save(transportador);
        return toResponse(transportador);
    }

    @Transactional
    public void deletar(Long id) {
        if (!transportadorRepository.existsById(id)) {
            throw new NotFoundException("Transportador não encontrado");
        }
        transportadorRepository.deleteById(id);
    }

    // ✅ Método manual de conversão (SEM ModelMapper)
    private TransportadorResponse toResponse(Transportador transportador) {
        TransportadorResponse response = new TransportadorResponse();
        response.setId(transportador.getId());
        response.setNome(transportador.getNome());
        response.setEmail(transportador.getEmail());
        response.setTelefone(transportador.getTelefone());
        response.setAvaliacaoMedia(transportador.getAvaliacaoMedia());
        response.setTotalAvaliacoes(transportador.getTotalAvaliacoes());
        return response;
    }
}