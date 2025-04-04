package com.totvs.produto_api.controller;

import com.totvs.produto_api.dto.ProdutoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProdutoControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testListarProdutos() {
        ResponseEntity<ProdutoDTO[]> response = restTemplate.getForEntity("/produtos", ProdutoDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}