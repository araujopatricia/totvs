package com.totvs.produto_api.repository;

import com.totvs.produto_api.dto.CategoriaDTO;
import com.totvs.produto_api.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    @Query("SELECT new com.totvs.produto_api.dto.CategoriaDTO(c.nome, SUM(p.quantidadeEstoque)) " +
            "FROM Categoria c JOIN c.produtos p GROUP BY c.nome")
    List<CategoriaDTO> findEstoqueTotalPorCategoria();
}