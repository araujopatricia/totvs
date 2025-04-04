export interface Category {
  id?: number;
  nome: string;
}

export interface CategoryStock {
  categoria: string;
  quantidadeTotalEstoque: number;
}
