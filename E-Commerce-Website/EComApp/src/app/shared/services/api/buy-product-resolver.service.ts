import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { ProductModel } from '../../models/product';
import { Observable, catchError, of } from 'rxjs';
import { ProductService } from './product.service';

@Injectable({
  providedIn: 'root'
})
export class BuyProductResolverService implements Resolve<ProductModel[]>{

  constructor(private productService:ProductService,private router:Router) { }
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): ProductModel[] | Observable<ProductModel[]> | Promise<ProductModel[]> {
    const pid = route.paramMap.get("id");
    const isSingleProductCheckout = route.paramMap.get("isSingleProductCheckout")==="true";
    
      return this.productService.getProductDetails(isSingleProductCheckout,pid? parseInt(pid, 10) : 0);
   
    
  }
}
