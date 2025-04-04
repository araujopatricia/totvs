import { Category } from './category.model';

export interface Product {
  id?: number;
  nome: string;
  descricao?: string;
  preco: number;
  quantidadeEstoque: number;
  categoria: Category;
}
