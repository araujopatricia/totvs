package com.totvs.produto_api.controller;

import com.totvs.produto_api.dto.CategoriaDTO;
import com.totvs.produto_api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/estoque-total")
    public ResponseEntity<List<CategoriaDTO>> listarEstoqueTotal() {
        return ResponseEntity.ok(categoriaRepository.findEstoqueTotalPorCategoria());
    }
}