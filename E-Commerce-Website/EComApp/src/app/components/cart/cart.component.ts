import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ProductService } from 'src/app/shared/services/api/product.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  displayedColumns: string[] = ['Name', 'Description', 'Price', 'Action'];
  subscriptions: Subscription[] = [];
  cartDetails= [];
  totalPrice: number=0;
  productCount: number=0;
  showProductSpinner: boolean = false;

  panelOpenState = false;

  constructor(private productService:ProductService, private router:Router) { }

  ngOnInit(): void {
    this.getCartDetails();
  }

  delete(cartId:any) {
    console.log(cartId);
    this.productService.deleteCartItems(cartId).subscribe(
      (resp) => {
        console.log(resp);
        this.getCartDetails();
      }, (err) => {
        console.log(err);
      }
    );
  }

  getCartDetails() {
    this.showProductSpinner=true;
    this.subscriptions.push(this.productService.getCartDetails().subscribe(
      (response:any) => {
        console.log(response);
        this.cartDetails = response;

        response.forEach((cartItem: any) => {
          
          // Access the product object within the cart item
          const product = cartItem.product;
  
          // Access the price property from the product object
          const productPrice = product.price;
          this.totalPrice+=productPrice;
          this.productCount++;
          // console.log("Product Price:", productPrice);
  
          // Do something with the product price if needed
        });
        console.log(this.totalPrice);
        console.log(this.productCount);
        
      },
      (error) => {
        console.log(error);
      },
      ()=>{
        this.showProductSpinner=false;
      }
      
    ));
  }

  checkout() {
    
    this.router.navigate(['/buyProduct', {
      isSingleProductCheckout: false, id: 0
    }]);
  }
  
  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => { subscription.unsubscribe });
  }

}
