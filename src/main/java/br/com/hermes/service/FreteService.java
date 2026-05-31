package br.com.hermes.service;

import br.com.hermes.dto.request.CriarFreteAgendadoRequest;
import br.com.hermes.dto.request.CriarFreteImediatoRequest;
import br.com.hermes.dto.response.FreteResponse;
import br.com.hermes.entity.*;
import br.com.hermes.exception.BusinessException;
import br.com.hermes.exception.ConflictException;
import br.com.hermes.exception.NotFoundException;
import br.com.hermes.repository.FreteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FreteService {

    private final FreteRepository freteRepository;
    private final ClienteService clienteService;
    private final TransportadorService transportadorService;

    // ✅ Construtor explícito (SEM Lombok, SEM ModelMapper)
    public FreteService(FreteRepository freteRepository,
                        ClienteService clienteService,
                        TransportadorService transportadorService) {
        this.freteRepository = freteRepository;
        this.clienteService = clienteService;
        this.transportadorService = transportadorService;
    }

    // ========== FLUXOS NORMAIS ==========

    @Transactional
    public Frete criarFreteImediato(CriarFreteImediatoRequest request, Long clienteId) {
        Cliente cliente = clienteService.buscarPorIdEntity(clienteId);

        Frete frete = new Frete();
        frete.setOrigem(request.getOrigem());
        frete.setDestino(request.getDestino());
        frete.setDescricaoCarga(request.getDescricaoCarga());
        frete.setValor(request.getValor());
        frete.setStatus(StatusFrete.PENDENTE);
        frete.setDataSolicitacao(LocalDateTime.now());
        frete.setCliente(cliente);

        return freteRepository.save(frete);
    }

    @Transactional
    public Frete criarFreteAgendado(CriarFreteAgendadoRequest request, Long clienteId) {
        Cliente cliente = clienteService.buscarPorIdEntity(clienteId);
        Transportador transportador = transportadorService.buscarPorIdEntity(request.getTransportadorId());

        if (request.getDataHoraInicio().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Data e hora de início devem ser futuras");
        }

        Frete frete = new Frete();
        frete.setOrigem(request.getOrigem());
        frete.setDestino(request.getDestino());
        frete.setDescricaoCarga(request.getDescricaoCarga());
        frete.setValor(request.getValor());
        frete.setStatus(StatusFrete.PENDENTE);
        frete.setDataSolicitacao(LocalDateTime.now());
        frete.setDataHoraInicio(request.getDataHoraInicio());
        frete.setCliente(cliente);
        frete.setTransportador(transportador);

        return freteRepository.save(frete);
    }

    @Transactional
    public void aceitarFrete(Long freteId, Long transportadorId) {
        Frete frete = buscarFreteEntity(freteId);
        Transportador transportador = transportadorService.buscarPorIdEntity(transportadorId);

        if (frete.getTransportador() != null) {
            throw new BusinessException("Frete já foi aceito por outro transportador");
        }
        if (frete.getStatus() != StatusFrete.PENDENTE) {
            throw new BusinessException("Frete não está disponível para aceitação");
        }

        LocalDateTime inicio = LocalDateTime.now().plusMinutes(30);
        LocalDateTime fimPrevisto = inicio.plusHours(1);

        frete.setTransportador(transportador);
        frete.setStatus(StatusFrete.ACEITO);
        frete.setDataHoraInicio(inicio);
        frete.setDataHoraFimPrevisto(fimPrevisto);

        freteRepository.save(frete);
    }

    @Transactional
    public void confirmarFreteAgendado(Long freteId, Long transportadorId) {
        Frete frete = buscarFreteEntity(freteId);

        if (frete.getTransportador() == null || !frete.getTransportador().getId().equals(transportadorId)) {
            throw new BusinessException("Apenas o transportador designado pode confirmar");
        }
        if (frete.getStatus() != StatusFrete.PENDENTE) {
            throw new BusinessException("Frete não está pendente de confirmação");
        }
        if (frete.getDataHoraInicio().isBefore(LocalDateTime.now())) {
            throw new ConflictException("O horário agendado já passou. Não é possível confirmar.");
        }

        frete.setStatus(StatusFrete.AGENDADO);
        freteRepository.save(frete);
    }

    @Transactional
    public void finalizarFrete(Long freteId, Long transportadorId) {
        Frete frete = buscarFreteEntity(freteId);

        if (frete.getTransportador() == null || !frete.getTransportador().getId().equals(transportadorId)) {
            throw new BusinessException("Apenas o transportador responsável pode finalizar");
        }
        if (frete.getStatus() != StatusFrete.ACEITO && frete.getStatus() != StatusFrete.AGENDADO && frete.getStatus() != StatusFrete.EM_TRANSITO) {
            throw new BusinessException("Frete não está em estado para ser finalizado");
        }

        frete.setStatus(StatusFrete.CONCLUIDO);
        frete.setDataConclusao(LocalDateTime.now());
        frete.setDataHoraFimReal(LocalDateTime.now());

        freteRepository.save(frete);
    }

    // ========== FLUXOS ALTERNATIVOS ==========

    @Transactional
    public void cancelarFrete(Long freteId, Long usuarioId, String role) {
        Frete frete = buscarFreteEntity(freteId);

        if (role.equals("CLIENTE") && !frete.getCliente().getId().equals(usuarioId)) {
            throw new BusinessException("Você não é o cliente dono deste frete");
        }
        if (role.equals("TRANSPORTADOR") && (frete.getTransportador() == null || !frete.getTransportador().getId().equals(usuarioId))) {
            throw new BusinessException("Você não é o transportador deste frete");
        }
        if (frete.getStatus() == StatusFrete.CONCLUIDO) {
            throw new BusinessException("Frete já concluído, não pode ser cancelado");
        }

        frete.setStatus(StatusFrete.CANCELADO);
        freteRepository.save(frete);
    }

    @Transactional
    public void rejeitarFreteAgendado(Long freteId, Long transportadorId) {
        Frete frete = buscarFreteEntity(freteId);

        if (frete.getTransportador() == null || !frete.getTransportador().getId().equals(transportadorId)) {
            throw new BusinessException("Apenas o transportador designado pode rejeitar");
        }
        if (frete.getStatus() != StatusFrete.PENDENTE) {
            throw new BusinessException("Frete não está pendente de confirmação");
        }

        frete.setStatus(StatusFrete.CANCELADO);
        freteRepository.save(frete);
    }

    // ========== MÉTODOS DE CONSULTA ==========

    @Transactional(readOnly = true)
    public List<FreteResponse> listarTodos() {
        return freteRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FreteResponse buscarPorId(Long id) {
        Frete frete = buscarFreteEntity(id);
        return toResponse(frete);
    }

    @Transactional(readOnly = true)
    public List<FreteResponse> listarPorCliente(Long clienteId) {
        clienteService.buscarPorId(clienteId);
        return freteRepository.findByClienteId(clienteId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FreteResponse> listarPorTransportador(Long transportadorId) {
        transportadorService.buscarPorId(transportadorId);
        return freteRepository.findByTransportadorId(transportadorId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FreteResponse> listarDisponiveis() {
        return freteRepository.findByTransportadorIsNullAndStatus(StatusFrete.PENDENTE).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Frete buscarFreteEntityPublic(Long id) {
        return freteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Frete não encontrado"));
    }

    // ========== MÉTODOS PRIVADOS ==========

    private Frete buscarFreteEntity(Long id) {
        return freteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Frete não encontrado"));
    }

    // ✅ Conversão manual (SEM ModelMapper)
    private FreteResponse toResponse(Frete frete) {
        FreteResponse response = new FreteResponse();
        response.setId(frete.getId());
        response.setOrigem(frete.getOrigem());
        response.setDestino(frete.getDestino());
        response.setDescricaoCarga(frete.getDescricaoCarga());
        response.setValor(frete.getValor());
        response.setStatus(frete.getStatus());
        response.setDataSolicitacao(frete.getDataSolicitacao());
        response.setDataConclusao(frete.getDataConclusao());
        response.setDataHoraInicio(frete.getDataHoraInicio());
        response.setDataHoraFimPrevisto(frete.getDataHoraFimPrevisto());
        response.setDataHoraFimReal(frete.getDataHoraFimReal());

        if (frete.getCliente() != null) {
            response.setClienteId(frete.getCliente().getId());
            response.setClienteNome(frete.getCliente().getNome());
        }

        if (frete.getTransportador() != null) {
            response.setTransportadorId(frete.getTransportador().getId());
            response.setTransportadorNome(frete.getTransportador().getNome());
        }

        if (frete.getAvaliacao() != null) {
            response.setAvaliacaoId(frete.getAvaliacao().getId());
            response.setAvaliacaoNota(frete.getAvaliacao().getNota());
            response.setAvaliacaoComentario(frete.getAvaliacao().getComentario());
        }

        return response;
    }
}