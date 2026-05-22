package br.com.hermes.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AvaliacaoRequest {

    @NotNull(message = "Nota é obrigatória")
    @DecimalMin(value = "0.0", message = "Nota mínima é 0")
    @DecimalMax(value = "5.0", message = "Nota máxima é 5")
    private Double nota;

    @NotBlank(message = "Comentário é obrigatório")
    private String comentario;

    public @NotNull(message = "Nota é obrigatória") @DecimalMin(value = "0.0", message = "Nota mínima é 0") @DecimalMax(value = "5.0", message = "Nota máxima é 5") Double getNota() {
        return nota;
    }

    public void setNota(@NotNull(message = "Nota é obrigatória") @DecimalMin(value = "0.0", message = "Nota mínima é 0") @DecimalMax(value = "5.0", message = "Nota máxima é 5") Double nota) {
        this.nota = nota;
    }

    public @NotBlank(message = "Comentário é obrigatório") String getComentario() {
        return comentario;
    }

    public void setComentario(@NotBlank(message = "Comentário é obrigatório") String comentario) {
        this.comentario = comentario;
    }
}