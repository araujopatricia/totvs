import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';

import { Product } from '../../models/product.model';
import { ProductService } from '../../services/product.service';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category.model';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatFormFieldModule,
    MatCardModule
  ],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  displayedColumns: string[] = ['id', 'nome', 'descricao', 'preco', 'quantidadeEstoque', 'categoria', 'acoes'];

  // Filtros
  searchName: string = '';
  searchCategory: string = '';

  // Paginação
  totalItems: number = 0;
  pageSize: number = 10;
  pageIndex: number = 0;

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadProducts();
    this.loadCategories();
  }

  loadProducts(): void {
    if (this.searchName || this.searchCategory) {
      this.searchProducts();
    } else {
      this.productService.getProducts(this.pageIndex, this.pageSize).subscribe({
        next: (response) => {
          this.products = response.content;
          this.totalItems = response.totalElements;
        },
        error: (error) => {
          console.error('Error fetching products', error);
          this.snackBar.open('Erro ao carregar produtos', 'Fechar', { duration: 3000 });
        }
      });
    }
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (error) => {
        console.error('Error fetching categories', error);
      }
    });
  }

  searchProducts(): void {
    this.productService.searchProducts(
      this.searchName,
      this.searchCategory,
      this.pageIndex,
      this.pageSize
    ).subscribe({
      next: (response) => {
        this.products = response.content;
        this.totalItems = response.totalElements;
      },
      error: (error) => {
        console.error('Error searching products', error);
        this.snackBar.open('Erro na busca de produtos', 'Fechar', { duration: 3000 });
      }
    });
  }

  deleteProduct(id: number): void {
    if (confirm('Tem certeza que deseja excluir este produto?')) {
      this.productService.deleteProduct(id).subscribe({
        next: () => {
          this.snackBar.open('Produto excluído com sucesso', 'Fechar', { duration: 3000 });
          this.loadProducts();
        },
        error: (error) => {
          console.error('Error deleting product', error);
          this.snackBar.open(
            error.error?.message || 'Não é possível excluir produto com estoque',
            'Fechar',
            { duration: 5000 }
          );
        }
      });
    }
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadProducts();
  }

  resetFilters(): void {
    this.searchName = '';
    this.searchCategory = '';
    this.pageIndex = 0;
    this.loadProducts();
  }
}
