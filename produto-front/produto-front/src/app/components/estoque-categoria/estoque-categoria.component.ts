export interface Categoria {
  id?: number;
  nome: string;
}

export interface CategoriaEstoqueDTO {
  categoria: string;
  quantidadeTotalEstoque: number;
}
