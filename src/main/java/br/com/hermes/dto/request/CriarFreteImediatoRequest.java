package br.com.hermes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CriarFreteImediatoRequest {

    @NotBlank(message = "Origem é obrigatória")
    private String origem;

    @NotBlank(message = "Destino é obrigatório")
    private String destino;

    @NotBlank(message = "Descrição da carga é obrigatória")
    private String descricaoCarga;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private BigDecimal valor;

    public @NotBlank(message = "Origem é obrigatória") String getOrigem() {
        return origem;
    }

    public void setOrigem(@NotBlank(message = "Origem é obrigatória") String origem) {
        this.origem = origem;
    }

    public @NotBlank(message = "Destino é obrigatório") String getDestino() {
        return destino;
    }

    public void setDestino(@NotBlank(message = "Destino é obrigatório") String destino) {
        this.destino = destino;
    }

    public @NotBlank(message = "Descrição da carga é obrigatória") String getDescricaoCarga() {
        return descricaoCarga;
    }

    public void setDescricaoCarga(@NotBlank(message = "Descrição da carga é obrigatória") String descricaoCarga) {
        this.descricaoCarga = descricaoCarga;
    }

    public @NotNull(message = "Valor é obrigatório") @Positive(message = "Valor deve ser positivo") BigDecimal getValor() {
        return valor;
    }

    public void setValor(@NotNull(message = "Valor é obrigatório") @Positive(message = "Valor deve ser positivo") BigDecimal valor) {
        this.valor = valor;
    }
}