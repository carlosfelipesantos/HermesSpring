package br.com.hermes.repository;

import br.com.hermes.entity.CategoriaCarga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaCargaRepository extends JpaRepository<CategoriaCarga, Long> {
}