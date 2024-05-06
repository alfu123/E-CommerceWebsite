
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './shared/components/header/header.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatTabsModule } from '@angular/material/tabs';
import { ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { MatGridListModule } from '@angular/material/grid-list'
import { MatSidenavModule } from '@angular/material/sidenav';
import { NgxSliderModule } from '@angular-slider/ngx-slider';
import { NgModule } from '@angular/core';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { DataService } from './shared/services/api/data.service';
import { AdminComponent } from './components/admin/admin.component';
import { UserComponent } from './components/user/user.component';
import { ForbiddenComponent } from './components/forbidden/forbidden.component';
import { BuyProductComponent } from './components/buy-product/buy-product.component';
import { FormsModule } from '@angular/forms';
import { UserGuard } from './shared/gaurds/user.guard';
import { AuthInterceptor } from './shared/gaurds/auth.interceptor';
import { UserApiService } from './shared/services/api/user-api.service';
import { OrderConfirmationComponent } from './components/order-confirmation/order-confirmation.component';
import { CartComponent } from './components/cart/cart.component';
import {MatTableModule} from '@angular/material/table';
import { MyOrderComponent } from './my-order/my-order.component';
import { AllOrderComponent } from './components/all-order/all-order.component';
import {MatButtonToggleModule} from '@angular/material/button-toggle';

@NgModule({
    declarations: [
        AppComponent,
        HeaderComponent,
        LoginComponent,
        SignupComponent,
        DashboardComponent,
        ProductDetailsComponent,
        AdminComponent,
        UserComponent,
        ForbiddenComponent,
        BuyProductComponent,
        OrderConfirmationComponent,
        CartComponent,
        MyOrderComponent,
        AllOrderComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        MatButtonModule,
        MatToolbarModule,
        MatIconModule,
        FlexLayoutModule,
        MatCardModule,
        MatInputModule,
        ReactiveFormsModule,
        MatTabsModule,
        HttpClientModule,
        MatGridListModule,
        MatSidenavModule,
        NgxSliderModule,
        MatDividerModule,
        MatExpansionModule,
        MatCheckboxModule,
        MatProgressSpinnerModule,
        FormsModule,
        MatTableModule,
        MatButtonToggleModule
    ],
    providers: [UserGuard,
        {
          provide: HTTP_INTERCEPTORS,
          useClass:AuthInterceptor,
          multi:true
        },
        DataService],
    bootstrap: [AppComponent]
})
export class AppModule { }
