package br.com.hermes.controller;

import br.com.hermes.entity.CategoriaCarga;
import br.com.hermes.service.CategoriaCargaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaCargaController {

    private final CategoriaCargaService categoriaCargaService;

    public CategoriaCargaController(CategoriaCargaService categoriaCargaService) {
        this.categoriaCargaService = categoriaCargaService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaCarga>> listarTodas() {
        return ResponseEntity.ok(categoriaCargaService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<CategoriaCarga> criar(@RequestBody CategoriaCarga categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaCargaService.criar(categoria));
    }

    // Associar categoria a transportador (N:N)
    @PostMapping("/{categoriaId}/transportadores/{transportadorId}")
    public ResponseEntity<Void> associar(@PathVariable Long categoriaId,
                                         @PathVariable Long transportadorId) {
        categoriaCargaService.associarTransportador(categoriaId, transportadorId);
        return ResponseEntity.ok().build();
    }
}