package br.com.hermes.repository;

import br.com.hermes.entity.Frete;
import br.com.hermes.entity.StatusFrete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FreteRepository extends JpaRepository<Frete, Long> {

    // Buscar fretes por cliente
    List<Frete> findByClienteId(Long clienteId);

    // Buscar fretes por transportador
    List<Frete> findByTransportadorId(Long transportadorId);

    // Buscar fretes disponíveis (sem transportador e status PENDENTE)
    List<Frete> findByTransportadorIsNullAndStatus(StatusFrete status);
}