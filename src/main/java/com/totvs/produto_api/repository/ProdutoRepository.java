package com.totvs.produto_api.repository;

import com.totvs.produto_api.modelo.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsByNomeAndCategoriaId(String nome, Long categoriaId);
    Page<Produto> findByNomeContainingAndCategoriaNomeContaining(String nome, String categoriaNome, Pageable pageable);
}