package br.com.hermes.controller;

import br.com.hermes.dto.response.TransportadorResponse;
import br.com.hermes.entity.Transportador;
import br.com.hermes.service.TransportadorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transportadores")
public class TransportadorController {

    private final TransportadorService transportadorService;

    public TransportadorController(TransportadorService transportadorService) {
        this.transportadorService = transportadorService;
    }

    @GetMapping
    public ResponseEntity<List<TransportadorResponse>> listarTodos() {
        return ResponseEntity.ok(transportadorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportadorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transportadorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TransportadorResponse> criar(@Valid @RequestBody Transportador transportador) {
        TransportadorResponse response = transportadorService.criar(transportador);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        transportadorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}