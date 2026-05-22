package br.com.hermes.dto.response;

import br.com.hermes.entity.StatusFrete;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FreteResponse {

    private Long id;
    private String origem;
    private String destino;
    private String descricaoCarga;
    private BigDecimal valor;
    private StatusFrete status;

    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFimPrevisto;
    private LocalDateTime dataHoraFimReal;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataConclusao;

    // Dados do cliente que solicitou
    private Long clienteId;
    private String clienteNome;

    // Dados do transportador (se houver)
    private Long transportadorId;
    private String transportadorNome;

    // Dados da avaliação (se houver)
    private Long avaliacaoId;
    private Double avaliacaoNota;
    private String avaliacaoComentario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDescricaoCarga() {
        return descricaoCarga;
    }

    public void setDescricaoCarga(String descricaoCarga) {
        this.descricaoCarga = descricaoCarga;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public StatusFrete getStatus() {
        return status;
    }

    public void setStatus(StatusFrete status) {
        this.status = status;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFimPrevisto() {
        return dataHoraFimPrevisto;
    }

    public void setDataHoraFimPrevisto(LocalDateTime dataHoraFimPrevisto) {
        this.dataHoraFimPrevisto = dataHoraFimPrevisto;
    }

    public LocalDateTime getDataHoraFimReal() {
        return dataHoraFimReal;
    }

    public void setDataHoraFimReal(LocalDateTime dataHoraFimReal) {
        this.dataHoraFimReal = dataHoraFimReal;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public Long getTransportadorId() {
        return transportadorId;
    }

    public void setTransportadorId(Long transportadorId) {
        this.transportadorId = transportadorId;
    }

    public String getTransportadorNome() {
        return transportadorNome;
    }

    public void setTransportadorNome(String transportadorNome) {
        this.transportadorNome = transportadorNome;
    }

    public Long getAvaliacaoId() {
        return avaliacaoId;
    }

    public void setAvaliacaoId(Long avaliacaoId) {
        this.avaliacaoId = avaliacaoId;
    }

    public Double getAvaliacaoNota() {
        return avaliacaoNota;
    }

    public void setAvaliacaoNota(Double avaliacaoNota) {
        this.avaliacaoNota = avaliacaoNota;
    }

    public String getAvaliacaoComentario() {
        return avaliacaoComentario;
    }

    public void setAvaliacaoComentario(String avaliacaoComentario) {
        this.avaliacaoComentario = avaliacaoComentario;
    }
}