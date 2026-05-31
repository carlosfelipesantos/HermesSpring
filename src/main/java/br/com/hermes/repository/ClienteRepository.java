package br.com.hermes.repository;

import br.com.hermes.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmailAndSenha(String email, String senha);

    boolean existsByEmail(String email);
}