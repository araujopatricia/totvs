import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

interface ProdutoDTO {
  id: number;
  nome: string;
  preco: number;
  quantidadeEstoque: number;
  categoriaId: number;
}

interface CategoriaDTO {
  nome: string;
  estoqueTotal: number;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080'; // URL do back-end

  constructor(private http: HttpClient) { }

  getProdutos(): Observable<ProdutoDTO[]> {
    return this.http.get<ProdutoDTO[]>(`${this.apiUrl}/produtos`);
  }

  getEstoquePorCategoria(): Observable<CategoriaDTO[]> {
    return this.http.get<CategoriaDTO[]>(`${this.apiUrl}/categorias/estoque-total`);
  }
}
