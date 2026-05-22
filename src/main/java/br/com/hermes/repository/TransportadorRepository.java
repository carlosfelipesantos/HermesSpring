package br.com.hermes.repository;

import br.com.hermes.entity.Transportador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportadorRepository extends JpaRepository<Transportador, Long> {
    boolean existsByEmail(String email);
}