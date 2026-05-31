package br.com.hermes.service;

import br.com.hermes.dto.request.LoginRequest;
import br.com.hermes.dto.response.LoginResponse;
import br.com.hermes.exception.BusinessException;
import br.com.hermes.repository.ClienteRepository;
import br.com.hermes.repository.TransportadorRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final ClienteRepository clienteRepository;
    private final TransportadorRepository transportadorRepository;

    public AuthService(ClienteRepository clienteRepository,
                       TransportadorRepository transportadorRepository) {
        this.clienteRepository = clienteRepository;
        this.transportadorRepository = transportadorRepository;
    }

    public LoginResponse loginCliente(LoginRequest request) {
        return clienteRepository
                .findByEmailAndSenha(request.getEmail(), request.getSenha())
                .map(c -> new LoginResponse(c.getId(), c.getNome(), c.getEmail(), "CLIENTE"))
                .orElseThrow(() -> new BusinessException("Email ou senha inválidos"));
    }

    public LoginResponse loginTransportador(LoginRequest request) {
        return transportadorRepository
                .findByEmailAndSenha(request.getEmail(), request.getSenha())
                .map(t -> new LoginResponse(t.getId(), t.getNome(), t.getEmail(), "TRANSPORTADOR"))
                .orElseThrow(() -> new BusinessException("Email ou senha inválidos"));
    }
}