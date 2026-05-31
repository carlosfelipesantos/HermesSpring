package br.com.hermes.service;

import br.com.hermes.dto.request.AvaliacaoRequest;
import br.com.hermes.entity.Avaliacao;
import br.com.hermes.entity.Frete;
import br.com.hermes.entity.StatusFrete;
import br.com.hermes.entity.Transportador;
import br.com.hermes.exception.BusinessException;
import br.com.hermes.repository.AvaliacaoRepository;
import br.com.hermes.repository.TransportadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final FreteService freteService;
    private final TransportadorRepository transportadorRepository;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository,
                            FreteService freteService,
                            TransportadorRepository transportadorRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.freteService = freteService;
        this.transportadorRepository = transportadorRepository;
    }

    @Transactional
    public void avaliarFrete(Long freteId, Long clienteId, AvaliacaoRequest request) {
        Frete frete = freteService.buscarFreteEntityPublic(freteId);

        if (!frete.getCliente().getId().equals(clienteId)) {
            throw new BusinessException("Apenas o cliente dono do frete pode avaliar");
        }
        if (frete.getStatus() != StatusFrete.CONCLUIDO) {
            throw new BusinessException("Apenas fretes concluídos podem ser avaliados");
        }
        if (avaliacaoRepository.existsByFreteId(freteId)) {
            throw new BusinessException("Este frete já foi avaliado");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNota(request.getNota());
        avaliacao.setComentario(request.getComentario());
        avaliacao.setDataAvaliacao(LocalDateTime.now());
        avaliacao.setFrete(frete);

        avaliacaoRepository.save(avaliacao);

        // Atualiza média do transportador
        Transportador transportador = frete.getTransportador();
        int total = transportador.getTotalAvaliacoes() == null ? 0 : transportador.getTotalAvaliacoes();
        double mediaAtual = transportador.getAvaliacaoMedia() == null ? 0.0 : transportador.getAvaliacaoMedia();

        double novaMedia = ((mediaAtual * total) + request.getNota()) / (total + 1);
        transportador.setTotalAvaliacoes(total + 1);
        transportador.setAvaliacaoMedia(novaMedia);
        transportadorRepository.save(transportador);
    }
}