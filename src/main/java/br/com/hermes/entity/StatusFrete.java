package br.com.hermes.entity;

public enum StatusFrete {
    PENDENTE,       // Aguardando aceitação/confirmação
    ACEITO,         // Frete imediato aceito pelo transportador
    AGENDADO,       // Frete agendado confirmado pelo transportador
    EM_TRANSITO,    // Em andamento
    CONCLUIDO,      // Finalizado
    CANCELADO       // Cancelado
}