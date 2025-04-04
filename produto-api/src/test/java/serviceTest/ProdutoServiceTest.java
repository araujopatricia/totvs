package serviceTest;

import com.totvs.produto_api.dto.ProdutoDTO;
import com.totvs.produto_api.exception.ProdutoException;
import com.totvs.produto_api.modelo.Categoria;
import com.totvs.produto_api.modelo.Produto;
import com.totvs.produto_api.repository.CategoriaRepository;
import com.totvs.produto_api.repository.ProdutoRepository;
import com.totvs.produto_api.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private ProdutoDTO produtoDTO;
    private Produto produto;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Dados de teste
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Eletrônicos");

        produtoDTO = new ProdutoDTO();
        produtoDTO.setNome("Smartphone");
        produtoDTO.setDescricao("Smartphone XYZ");
        produtoDTO.setPreco(new BigDecimal("1500.00"));
        produtoDTO.setQuantidadeEstoque(10);
        produtoDTO.setCategoriaId(1L);

        produto = new Produto();
        produto.setId(1L);
        produto.setNome("Smartphone");
        produto.setDescricao("Smartphone XYZ");
        produto.setPreco(new BigDecimal("1500.00"));
        produto.setQuantidadeEstoque(10);
        produto.setCategoria(categoria);
    }

    @Test
    void testCriarProduto_Sucesso() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(produtoRepository.existsByNomeAndCategoriaId("Smartphone", 1L)).thenReturn(false);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ProdutoDTO resultado = produtoService.criarProduto(produtoDTO);

        assertNotNull(resultado);
        assertEquals("Smartphone", resultado.getNome());
        assertEquals(1L, resultado.getCategoriaId());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void testCriarProduto_ProdutoJaExiste() {
        when(produtoRepository.existsByNomeAndCategoriaId("Smartphone", 1L)).thenReturn(true);

        ProdutoException exception = assertThrows(ProdutoException.class, () -> {
            produtoService.criarProduto(produtoDTO);
        });

        assertEquals("Produto já existe nesta categoria.", exception.getMessage());
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void testCriarProduto_CategoriaNaoEncontrada() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        ProdutoException exception = assertThrows(ProdutoException.class, () -> {
            produtoService.criarProduto(produtoDTO);
        });

        assertEquals("Categoria não encontrada.", exception.getMessage());
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void testListarProdutos_Sucesso() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Produto> page = new PageImpl<>(Arrays.asList(produto));
        when(produtoRepository.findByNomeContainingAndCategoriaNomeContaining("Smart", "Eletr", pageable))
                .thenReturn(page);

        Page<ProdutoDTO> resultado = produtoService.listarProdutos("Smart", "Eletr", pageable);

        assertEquals(1, resultado.getTotalElements());
        assertEquals("Smartphone", resultado.getContent().get(0).getNome());
        verify(produtoRepository, times(1)).findByNomeContainingAndCategoriaNomeContaining("Smart", "Eletr", pageable);
    }

    @Test
    void testBuscarPorId_Sucesso() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        ProdutoDTO resultado = produtoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Smartphone", resultado.getNome());
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorId_NaoEncontrado() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        ProdutoException exception = assertThrows(ProdutoException.class, () -> {
            produtoService.buscarPorId(1L);
        });

        assertEquals("Produto não encontrado.", exception.getMessage());
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    void testEditarProduto_Sucesso() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ProdutoDTO resultado = produtoService.editarProduto(1L, produtoDTO);

        assertNotNull(resultado);
        assertEquals("Smartphone", resultado.getNome());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void testEditarProduto_NaoEncontrado() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        ProdutoException exception = assertThrows(ProdutoException.class, () -> {
            produtoService.editarProduto(1L, produtoDTO);
        });

        assertEquals("Produto não encontrado.", exception.getMessage());
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void testExcluirProduto_Sucesso() {
        produto.setQuantidadeEstoque(0); // Estoque zero permite exclusão
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        produtoService.excluirProduto(1L);

        verify(produtoRepository, times(1)).delete(produto);
    }

    @Test
    void testExcluirProduto_ComEstoque() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        ProdutoException exception = assertThrows(ProdutoException.class, () -> {
            produtoService.excluirProduto(1L);
        });

        assertEquals("Não é possível excluir produto com estoque maior que zero.", exception.getMessage());
        verify(produtoRepository, never()).delete(any(Produto.class));
    }

    @Test
    void testValidarProduto_NomeInvalido() {
        produtoDTO.setNome("");

        ProdutoException exception = assertThrows(ProdutoException.class, () -> {
            produtoService.criarProduto(produtoDTO);
        });

        assertEquals("Nome do produto é obrigatório.", exception.getMessage());
    }

    @Test
    void testValidarProduto_PrecoInvalido() {
        produtoDTO.setPreco(BigDecimal.ZERO);

        ProdutoException exception = assertThrows(ProdutoException.class, () -> {
            produtoService.criarProduto(produtoDTO);
        });

        assertEquals("Preço deve ser maior que zero.", exception.getMessage());
    }

    @Test
    void testValidarProduto_QuantidadeInvalida() {
        produtoDTO.setQuantidadeEstoque(-1);

        ProdutoException exception = assertThrows(ProdutoException.class, () -> {
            produtoService.criarProduto(produtoDTO);
        });

        assertEquals("Quantidade em estoque não pode ser negativa.", exception.getMessage());
    }

    @Test
    void testValidarProduto_CategoriaNula() {
        produtoDTO.setCategoriaId(null);

        ProdutoException exception = assertThrows(ProdutoException.class, () -> {
            produtoService.criarProduto(produtoDTO);
        });

        assertEquals("Categoria é obrigatória.", exception.getMessage());
    }
}
