import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LoginComponent } from './components/login/login.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { SignupComponent } from './components/signup/signup.component';
import { UserGuard } from './shared/gaurds/user.guard';
import { AdminComponent } from './components/admin/admin.component';
import { UserComponent } from './components/user/user.component';
import { ForbiddenComponent } from './components/forbidden/forbidden.component';
import { BuyProductComponent } from './components/buy-product/buy-product.component';
import { BuyProductResolverService } from './shared/services/api/buy-product-resolver.service';
import { OrderConfirmationComponent } from './components/order-confirmation/order-confirmation.component';
import { CartComponent } from './components/cart/cart.component';
import { MyOrderComponent } from './my-order/my-order.component';
import { AllOrderComponent } from './components/all-order/all-order.component';


const routes: Routes = [
    { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    { path: "forbidden", component: ForbiddenComponent },
    {
        path: "admin",
        component: AdminComponent,
        canActivate: [UserGuard],
        data: { roles: ["Admin"] },
      },
      {
        path: "user",
        component: UserComponent,
        canActivate: [UserGuard],
        data: { roles: ["User"] },
      },
      {
        path: "cart",
        component: CartComponent,
        canActivate: [UserGuard],
        data: { roles: ["User"] },
      },
      {
        path: "myOrder",
        component: MyOrderComponent,
        canActivate: [UserGuard],
        data: { roles: ["User"] },
      },
      {
        path: "allOrder",
        component: AllOrderComponent,
        canActivate: [UserGuard],
        data: { roles: ["Admin"] },
      },
      {
        path: 'buyProduct', component:BuyProductComponent, canActivate: [UserGuard], data: {roles:["User"]},
        resolve: {
            productDetails: BuyProductResolverService,
          }
      },
    { path: 'dashboard', component: DashboardComponent },
    { path: 'login', component: LoginComponent },
    { path: 'signup', component: SignupComponent },
    // { path: 'products', component: ProductDetailsComponent, canActivate: [UserGuard], data: {roles:["User"]}}
    {path: 'products',component:ProductDetailsComponent},
    {path: 'orderConfirm', component: OrderConfirmationComponent, canActivate: [UserGuard], data: {roles:["User"]}}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
