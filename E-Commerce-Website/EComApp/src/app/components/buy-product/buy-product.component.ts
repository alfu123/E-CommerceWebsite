import { Component, Injectable, Injector, NgZone, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ApiResponseModel } from 'src/app/shared/models/api-response';
import { OrderDetails } from 'src/app/shared/models/order-details';
import { ProductModel } from 'src/app/shared/models/product';
import { ServiceabilityModel } from 'src/app/shared/models/serviceability';
import { ProductService } from 'src/app/shared/services/api/product.service';

@Injectable({
  providedIn: 'root'
})

@Component({
  selector: 'app-buy-product',
  templateUrl: './buy-product.component.html',
  styleUrls: ['./buy-product.component.css']
})
export class BuyProductComponent implements OnInit {

  expectedDel:any | undefined;
  pid: number | undefined;
  productDetails: ProductModel[] = [];
  subscriptions: Subscription[] = [];
  // serviceabilityRes:ServiceabilityModel[]=[];
  productResponse: ApiResponseModel;

  orderDetails: OrderDetails = {
    fullName: '',
    fullAddress: '',
    contactNumber: '',
    alternateContactNumber: '',
    pincode:'',
    // transactionId: '',
    orderProductQuantityList: []
  }
  isSingleProductCheckout: string |null = '';
  showProductSpinner: boolean = false;

  constructor(private activatedRoute: ActivatedRoute,
    private productService: ProductService,
    private router: Router,
    private injector: Injector) {
    this.productResponse = { status: '', status_code: 0, data: [] };
  }

  ngOnInit(): void {
    this.showProductSpinner = true;
    this.productResponse = this.activatedRoute.snapshot.data['productDetails'];
    this.isSingleProductCheckout=this.activatedRoute.snapshot.paramMap.get("isSingleProductCheckout");
    
    if (this.productResponse ) {
      this.productDetails = this.productResponse.data;
      
      this.expectedDel=this.productResponse.data[0].serviceability[0].expectedDelivery;
      this.productResponse.data.forEach(product => {
        // Iterate over each serviceability model of the product
        product.serviceability.forEach(serviceability => {
          // Compare expected delivery with current maximum
          if (serviceability.expectedDelivery > this.expectedDel) {
            this.expectedDel = serviceability.expectedDelivery;
          }
        });
      });
      


      // this.serviceabilityRes=this.productResponse.data[0].serviceability;
      console.log(this.productDetails[0].pname);
      this.productDetails.forEach(

        x => this.orderDetails.orderProductQuantityList.push(
          { productId: x.pid, quantity: 1 }
        ),
      );
    }
    else{
      error: (err: any) => {
        console.error(err);
        alert("Internal Server Error!");
      }
    }
    this.showProductSpinner = false;

    console.log(this.orderDetails);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => { subscription.unsubscribe });
  }

  public placeOrder(orderForm: NgForm) {
    this.productService.placeOrder(this.orderDetails,this.isSingleProductCheckout).subscribe(
      (resp: any) => {
        console.log(resp);
        orderForm.reset();

        const ngZone = this.injector.get(NgZone);
        ngZone.run(
          () => {
            this.router.navigate(["/orderConfirm",{expectedDel:this.expectedDel

            }]);
          }
        );
      },
      (err: any) => {
        console.log(err);
      }
    );
  }

  getQuantityForProduct(productId: number) {
    const filteredProduct = this.orderDetails.orderProductQuantityList.filter(
      (productQuantity) => productQuantity.productId === productId
    );

    return filteredProduct[0].quantity;
  }

  getCalculatedTotal(productId: number, price: number) {
    const filteredProduct = this.orderDetails.orderProductQuantityList.filter(
      (productQuantity) => productQuantity.productId === productId
    );

    return filteredProduct[0].quantity * price;
  }

  onQuantityChanged(q: number, productId: number) {
    this.orderDetails.orderProductQuantityList.filter(
      (orderProduct) => orderProduct.productId === productId
    )[0].quantity = q;
  }

  getCalculatedGrandTotal() {
    let grandTotal = 0;

    this.orderDetails.orderProductQuantityList.forEach(
      (productQuantity) => {
        const price = this.productDetails.filter(product => product.pid === productQuantity.productId)[0].price;
        grandTotal = grandTotal + price * productQuantity.quantity;
      }
    );

    return grandTotal;
  }

  // createTransactionAndPlaceOrder(orderForm: NgForm) {
  //   let amount = this.getCalculatedGrandTotal();
  //   this.productService.createTransaction(amount).subscribe(
  //     (response) => {
  //       console.log(response);
  //       this.openTransactioModal(response, orderForm);
  //     },
  //     (error) => {
  //       console.log(error);
  //     }
  //   );




}
