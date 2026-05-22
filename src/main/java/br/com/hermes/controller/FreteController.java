package br.com.hermes.controller;

import br.com.hermes.dto.request.CriarFreteAgendadoRequest;
import br.com.hermes.dto.request.CriarFreteImediatoRequest;
import br.com.hermes.dto.response.FreteResponse;
import br.com.hermes.entity.Frete;
import br.com.hermes.service.FreteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fretes")
public class FreteController {

    private final FreteService freteService;

    public FreteController(FreteService freteService) {
        this.freteService = freteService;
    }

    // ========== FLUXOS NORMAIS ==========

    // Criar frete imediato (qualquer transportador pode aceitar)
    @PostMapping("/imediato")
    public ResponseEntity<FreteResponse> criarFreteImediato(
            @Valid @RequestBody CriarFreteImediatoRequest request,
            @RequestHeader("usuarioId") Long clienteId) {
        Frete frete = freteService.criarFreteImediato(request, clienteId);
        FreteResponse response = freteService.buscarPorId(frete.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Criar frete agendado (contratação direta)
    @PostMapping("/agendado")
    public ResponseEntity<FreteResponse> criarFreteAgendado(
            @Valid @RequestBody CriarFreteAgendadoRequest request,
            @RequestHeader("usuarioId") Long clienteId) {
        Frete frete = freteService.criarFreteAgendado(request, clienteId);
        FreteResponse response = freteService.buscarPorId(frete.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Aceitar frete imediato (transportador)
    @PostMapping("/{id}/aceitar")
    public ResponseEntity<Void> aceitarFrete(
            @PathVariable Long id,
            @RequestHeader("usuarioId") Long transportadorId) {
        freteService.aceitarFrete(id, transportadorId);
        return ResponseEntity.ok().build();
    }

    // Confirmar frete agendado (transportador)
    @PostMapping("/{id}/confirmar")
    public ResponseEntity<Void> confirmarFreteAgendado(
            @PathVariable Long id,
            @RequestHeader("usuarioId") Long transportadorId) {
        freteService.confirmarFreteAgendado(id, transportadorId);
        return ResponseEntity.ok().build();
    }

    // Finalizar frete (transportador)
    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizarFrete(
            @PathVariable Long id,
            @RequestHeader("usuarioId") Long transportadorId) {
        freteService.finalizarFrete(id, transportadorId);
        return ResponseEntity.ok().build();
    }

    // ========== FLUXOS ALTERNATIVOS ==========

    // Cancelar frete (cliente ou transportador)
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarFrete(
            @PathVariable Long id,
            @RequestHeader("usuarioId") Long usuarioId,
            @RequestHeader("role") String role) {
        freteService.cancelarFrete(id, usuarioId, role);
        return ResponseEntity.ok().build();
    }

    // Rejeitar frete agendado (transportador)
    @PostMapping("/{id}/rejeitar")
    public ResponseEntity<Void> rejeitarFreteAgendado(
            @PathVariable Long id,
            @RequestHeader("usuarioId") Long transportadorId) {
        freteService.rejeitarFreteAgendado(id, transportadorId);
        return ResponseEntity.ok().build();
    }

    // ========== CONSULTAS ==========

    @GetMapping
    public ResponseEntity<List<FreteResponse>> listarTodos() {
        return ResponseEntity.ok(freteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FreteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(freteService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<FreteResponse>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(freteService.listarPorCliente(clienteId));
    }

    @GetMapping("/transportador/{transportadorId}")
    public ResponseEntity<List<FreteResponse>> listarPorTransportador(@PathVariable Long transportadorId) {
        return ResponseEntity.ok(freteService.listarPorTransportador(transportadorId));
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<FreteResponse>> listarDisponiveis() {
        return ResponseEntity.ok(freteService.listarDisponiveis());
    }
}