package br.com.hermes.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transportadores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transportador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    private String telefone;

    @Column(name = "avaliacao_media")
    private Double avaliacaoMedia;

    @Column(nullable = false)
    private String senha;

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    @Column(name = "total_avaliacoes")
    private Integer totalAvaliacoes = 0;

    @OneToMany(mappedBy = "transportador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Frete> fretesAceitos = new ArrayList<>();

    // Dentro da classe Transportador, adicionar:

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "transportador_categoria",
            joinColumns = @JoinColumn(name = "transportador_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<CategoriaCarga> categoriasCarga = new ArrayList<>();

    // getter e setter
    public List<CategoriaCarga> getCategoriasCarga() { return categoriasCarga; }
    public void setCategoriasCarga(List<CategoriaCarga> categoriasCarga) { this.categoriasCarga = categoriasCarga; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Double getAvaliacaoMedia() {
        return avaliacaoMedia;
    }

    public void setAvaliacaoMedia(Double avaliacaoMedia) {
        this.avaliacaoMedia = avaliacaoMedia;
    }

    public Integer getTotalAvaliacoes() {
        return totalAvaliacoes;
    }

    public void setTotalAvaliacoes(Integer totalAvaliacoes) {
        this.totalAvaliacoes = totalAvaliacoes;
    }

    public List<Frete> getFretesAceitos() {
        return fretesAceitos;
    }

    public void setFretesAceitos(List<Frete> fretesAceitos) {
        this.fretesAceitos = fretesAceitos;
    }
}