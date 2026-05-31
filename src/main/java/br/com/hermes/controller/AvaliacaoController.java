package br.com.hermes.controller;

import br.com.hermes.dto.request.AvaliacaoRequest;
import br.com.hermes.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @PostMapping
    public ResponseEntity<Void> avaliar(
            @RequestBody AvaliacaoRequest request,
            @RequestHeader("usuarioId") Long clienteId) {
        avaliacaoService.avaliarFrete(request.getFreteId(), clienteId, request);
        return ResponseEntity.ok().build();
    }
}