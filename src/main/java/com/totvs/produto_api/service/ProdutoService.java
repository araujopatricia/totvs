package com.totvs.produto_api.service;

import com.totvs.produto_api.dto.ProdutoDTO;
import com.totvs.produto_api.exception.ProdutoException;
import com.totvs.produto_api.modelo.Categoria;
import com.totvs.produto_api.modelo.Produto;
import com.totvs.produto_api.repository.CategoriaRepository;
import com.totvs.produto_api.repository.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProdutoService {
    private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public ProdutoDTO criarProduto(ProdutoDTO dto) {
        logger.info("Criando produto: {}", dto.getNome());
        validarProduto(dto);
        if (produtoRepository.existsByNomeAndCategoriaId(dto.getNome(), dto.getCategoriaId())) {
            throw new ProdutoException("Produto já existe nesta categoria.");
        }
        Produto produto = toEntity(dto);
        produto = produtoRepository.save(produto);
        return toDTO(produto);
    }

    public Page<ProdutoDTO> listarProdutos(String nome, String categoriaNome, Pageable pageable) {
        logger.info("Listando produtos com filtro - nome: {}, categoria: {}", nome, categoriaNome);
        return produtoRepository.findByNomeContainingAndCategoriaNomeContaining(nome, categoriaNome, pageable)
                .map(this::toDTO);
    }

    public ProdutoDTO buscarPorId(Long id) {
        logger.info("Buscando produto por ID: {}", id);
        return produtoRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ProdutoException("Produto não encontrado."));
    }

    public ProdutoDTO editarProduto(Long id, ProdutoDTO dto) {
        logger.info("Editando produto ID: {}", id);
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoException("Produto não encontrado."));
        validarProduto(dto);
        updateProduto(produto, dto);
        produto = produtoRepository.save(produto);
        return toDTO(produto);
    }

    public void excluirProduto(Long id) {
        logger.info("Excluindo produto ID: {}", id);
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoException("Produto não encontrado."));
        if (produto.getQuantidadeEstoque() > 0) {
            throw new ProdutoException("Não é possível excluir produto com estoque maior que zero.");
        }
        produtoRepository.delete(produto);
    }

    private void validarProduto(ProdutoDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new ProdutoException("Nome do produto é obrigatório.");
        }
        if (dto.getPreco() == null || dto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProdutoException("Preço deve ser maior que zero.");
        }
        if (dto.getQuantidadeEstoque() == null || dto.getQuantidadeEstoque() < 0) {
            throw new ProdutoException("Quantidade em estoque não pode ser negativa.");
        }
        if (dto.getCategoriaId() == null) {
            throw new ProdutoException("Categoria é obrigatória.");
        }
    }

    private Produto toEntity(ProdutoDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ProdutoException("Categoria não encontrada."));
        produto.setCategoria(categoria);
        return produto;
    }

    private ProdutoDTO toDTO(Produto produto) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setPreco(produto.getPreco());
        dto.setQuantidadeEstoque(produto.getQuantidadeEstoque());
        dto.setCategoriaId(produto.getCategoria().getId());
        return dto;
    }

    private void updateProduto(Produto produto, ProdutoDTO dto) {
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPreco(dto.getPreco());
        produto.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ProdutoException("Categoria não encontrada."));
        produto.setCategoria(categoria);
    }
}