package com.totvs.produto_api.dto;

public class CategoriaDTO {
    private String categoria;
    private Integer quantidadeTotalEstoque;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getQuantidadeTotalEstoque() {
        return quantidadeTotalEstoque;
    }

    public void setQuantidadeTotalEstoque(Integer quantidadeTotalEstoque) {
        this.quantidadeTotalEstoque = quantidadeTotalEstoque;
    }
}