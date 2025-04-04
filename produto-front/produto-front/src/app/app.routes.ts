import { Routes as NgRoutes } from '@angular/router';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductFormComponent } from './components/product-form/product-form.component';

export const routes: NgRoutes = [
  { path: '', redirectTo: '/produtos', pathMatch: 'full' },
  { path: 'produtos', component: ProductListComponent },
  { path: 'produtos/new', component: ProductFormComponent },
  { path: 'produtos/edit/:id', component: ProductFormComponent }
];
