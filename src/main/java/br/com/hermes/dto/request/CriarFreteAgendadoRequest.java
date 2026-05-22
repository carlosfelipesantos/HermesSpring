package br.com.hermes.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class CriarFreteAgendadoRequest extends CriarFreteImediatoRequest {

    @NotNull(message = "ID do transportador é obrigatório")
    private Long transportadorId;

    @NotNull(message = "Data e hora de início são obrigatórias")
    private LocalDateTime dataHoraInicio;

    public @NotNull(message = "ID do transportador é obrigatório") Long getTransportadorId() {
        return transportadorId;
    }

    public void setTransportadorId(@NotNull(message = "ID do transportador é obrigatório") Long transportadorId) {
        this.transportadorId = transportadorId;
    }

    public @NotNull(message = "Data e hora de início são obrigatórias") LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(@NotNull(message = "Data e hora de início são obrigatórias") LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }
}