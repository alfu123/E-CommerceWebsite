import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { ApiResponseModel } from '../../models/api-response';
import { AvailabilityResponseModel } from '../../models/availability-response';
import { ProductModel } from '../../models/product';
import { OrderDetails } from '../../models/order-details';
import { MyOrderComponent } from 'src/app/my-order/my-order.component';
import { OrderApiResponseModel } from '../../models/order-api-response';

@Injectable({
    providedIn: 'root'
})
export class ProductService {
    baseApiUrl: string = environment.baseApiUrl;
    requestHeader=new HttpHeaders({
        "No-Auth":"True"
    });


    constructor(private httpClient: HttpClient) { }

    // Get All Products from API
    getAllProducts(pageNumber: number) {
        return this.httpClient.get<ApiResponseModel>(`${this.baseApiUrl}/products?pageNumber=${pageNumber}`,{headers:this.requestHeader});
    }

    // Get A single product ffrom product id
    getProductById(pid: number) {
        return this.httpClient.get<ApiResponseModel>(`${this.baseApiUrl}/products?pid=${pid}`,{headers:this.requestHeader});
    }

    // Get availability of product with pincode 
    getAvailability(pid: number, pincode: number) {
        return this.httpClient.get<AvailabilityResponseModel>(`${this.baseApiUrl}/products/product?pid=${pid}&pincode=${pincode}`,{headers:this.requestHeader});
    }

    getProductDetails(isSingleProductCheckout: boolean,productId: number){
        return this.httpClient.get<ProductModel[]>(`${this.baseApiUrl}/products/getProductDetails/`+isSingleProductCheckout+`/`+productId)
    }

    placeOrder(orderDetails:OrderDetails,isCartCheckout: String | null){
        return this.httpClient.post(`${this.baseApiUrl}/order/placeOrder/`+isCartCheckout,orderDetails);
    }
    addToCart(productId:any){
        return this.httpClient.get(`${this.baseApiUrl}/cart/addToCart/`+productId);
    }
    getCartDetails(){
        return this.httpClient.get(`${this.baseApiUrl}/cart/getCartDetails`);
    }
    deleteCartItems(cartId:any){
        return this.httpClient.delete(`${this.baseApiUrl}/cart/deleteCartItem/`+cartId);
    }
    getMyOrder(){
        return this.httpClient.get<OrderApiResponseModel>(`${this.baseApiUrl}/order/getOrderDetails`);
    }
    getAllOrder(status:string){
        return this.httpClient.get<OrderApiResponseModel>(`${this.baseApiUrl}/order/getAllOrderDetails/`+status);
    }
    markAsDeliver(orderId:string){
        return this.httpClient.get<OrderApiResponseModel>(`${this.baseApiUrl}/order/markOrderAsDeliver/`+orderId);
    }
}
