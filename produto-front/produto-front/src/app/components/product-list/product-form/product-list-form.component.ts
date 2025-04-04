import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';

import { Product } from '../../models/product.model';
import { Category } from '../../models/category.model';
import { ProductService } from '../../services/product.service';
import { CategoryService } from '../../services/category.service';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatSnackBarModule,
    MatCardModule,
    MatFormFieldModule
  ],
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.scss']
})
export class ProductFormComponent implements OnInit {
  productForm!: FormGroup;
  categories: Category[] = [];
  isEditMode: boolean = false;
  productId?: number;

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private categoryService: CategoryService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadCategories();

    // Verificar se é edição ou criação
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.productId = +params['id'];
        this.loadProduct(this.productId);
      }
    });
  }

  initForm(): void {
    this.productForm = this.fb.group({
      nome: ['', [Validators.required]],
      descricao: [''],
      preco: ['', [Validators.required, Validators.min(0.01)]],
      quantidadeEstoque: ['', [Validators.required, Validators.min(0)]],
      categoria: ['', [Validators.required]]
    });
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (error) => {
        console.error('Error fetching categories', error);
        this.snackBar.open('Erro ao carregar categorias', 'Fechar', { duration: 3000 });
      }
    });
  }

  loadProduct(id: number): void {
    this.productService.getProductById(id).subscribe({
      next: (product) => {
        this.productForm.patchValue({
          nome: product.nome,
          descricao: product.descricao,
          preco: product.preco,
          quantidadeEstoque: product.quantidadeEstoque,
          categoria: product.categoria.id
        });
      },
      error: (error) => {
        console.error('Error fetching product', error);
        this.snackBar.open('Erro ao carregar produto', 'Fechar', { duration: 3000 });
      }
    });
  }

  onSubmit(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }

    const productData = this.prepareProductData();

    if (this.isEditMode && this.productId) {
      this.updateProduct(productData);
    } else {
      this.createProduct(productData);
    }
  }

  prepareProductData(): Product {
    const formValue = this.productForm.value;
    const categoryId = formValue.categoria;

    const product: Product = {
      nome: formValue.nome,
      descricao: formValue.descricao,
      preco: formValue.preco,
      quantidadeEstoque: formValue.quantidadeEstoque,
      categoria: { id: categoryId }
    };

    if (this.isEditMode && this.productId) {
      product.id = this.productId;
    }

    return product;
  }

  createProduct(product: Product): void {
    this.productService.createProduct(product).subscribe({
      next: () => {
        this.snackBar.open('Produto criado com sucesso', 'Fechar', { duration: 3000 });
        this.router.navigate(['/products']);
      },
      error: (error) => {
        console.error('Error creating product', error);
        this.snackBar.open(
          error.error?.message || 'Erro ao criar produto',
          'Fechar',
          { duration: 5000 }
        );
      }
    });
  }

  updateProduct(product: Product): void {
    this.productService.updateProduct(product).subscribe({
      next: () => {
        this.snackBar.open('Produto atualizado com sucesso', 'Fechar', { duration: 3000 });
        this.router.navigate(['/products']);
      },
      error: (error) => {
        console.error('Error updating product', error);
        this.snackBar.open(
          error.error?.message || 'Erro ao atualizar produto',
          'Fechar',
          { duration: 5000 }
        );
      }
    });
  }

  hasError(controlName: string, errorName: string): boolean {
    const control = this.productForm.get(controlName);
    return !!control && control.touched && control.hasError(errorName);
  }

  cancel(): void {
    this.router.navigate(['/products']);
  }
}
