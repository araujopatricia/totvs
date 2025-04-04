package com.totvs.produto_api.controller;

import com.totvs.produto_api.dto.CategoriaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoriaControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testListarEstoqueTotalPorCategoria() {
        // Faz a requisição ao endpoint
        ResponseEntity<CategoriaDTO[]> response = restTemplate.getForEntity("/categorias/estoque-total", CategoriaDTO[].class);

        // Verifica o status
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}