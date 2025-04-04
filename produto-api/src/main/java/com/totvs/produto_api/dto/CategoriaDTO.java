package com.totvs.produto_api.dto;

public class CategoriaDTO {
    private String nome;
    private Long estoqueTotal;

    public CategoriaDTO(String nome, Long estoqueTotal) {
        this.nome = nome;
        this.estoqueTotal = estoqueTotal;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Long getEstoqueTotal() { return estoqueTotal; }
    public void setEstoqueTotal(Long estoqueTotal) { this.estoqueTotal = estoqueTotal; }
}