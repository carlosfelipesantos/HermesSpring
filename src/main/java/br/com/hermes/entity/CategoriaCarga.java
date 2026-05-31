package br.com.hermes.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias_carga")
public class CategoriaCarga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome; // Ex: "Refrigerado", "Perigoso", "Frágil"

    private String descricao;

    // Lado N:N
    @ManyToMany(mappedBy = "categoriasCarga")
    private List<Transportador> transportadores = new ArrayList<>();

    // getters e setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public List<Transportador> getTransportadores() { return transportadores; }
    public void setTransportadores(List<Transportador> transportadores) { this.transportadores = transportadores; }
}