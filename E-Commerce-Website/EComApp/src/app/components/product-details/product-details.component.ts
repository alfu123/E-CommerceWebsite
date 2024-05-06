import { Component, Injectable, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { Subscription } from 'rxjs';
import { ApiResponseModel } from 'src/app/shared/models/api-response';
import { AvailabilityResponseModel } from 'src/app/shared/models/availability-response';
import { ProductModel } from 'src/app/shared/models/product';
import { ProductService } from 'src/app/shared/services/api/product.service';
import { UserAuthService } from 'src/app/shared/services/api/user-auth.service';

@Injectable({
    providedIn:'root'
})

@Component({
    selector: 'app-product-details',
    templateUrl: './product-details.component.html',
    styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit, OnDestroy {

    pid: number | undefined;
    subscriptions: Subscription[] = [];
    productResponse: ApiResponseModel;
    availabilityResponse: AvailabilityResponseModel;
    availabilityForm: FormGroup;
    showAvailabilitySpinner: boolean = false;
    showProductSpinner: boolean = false;
    isProductServiceable: boolean | undefined;

   


    constructor(private route: ActivatedRoute, private productService: ProductService, private fb: FormBuilder, private router: Router,
        private userAuthService: UserAuthService) {
        // Initialize Product Response
        this.productResponse = { status: '', status_code: 0, data: [] };
        // Initialize Availabilty Response
        this.availabilityResponse = { status: '', status_code: 0 };
        // Create Availability Form With Pincode Field
        this.availabilityForm = this.fb.group({
            pincode: ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    ngOnInit(): void {
        // Display the Product Loading Spinner
        this.showProductSpinner = true;
        this.subscriptions.push(this.route.queryParams.subscribe(p => {
            this.pid = p['pid'];
            if (this.pid) {
                this.subscriptions.push(this.productService.getProductById(this.pid).subscribe({
                    next: (resp) => {
                        // Set Product from Response
                        this.productResponse = resp;
                        console.log(this.productResponse);

                        // this.router.navigate(['/buyProduct']);
                    },
                    error: (err) => {
                        console.error(err);
                        alert("Internal Server Error!");
                    },
                    complete: () => {
                        // Hide the Product Spinner
                        this.showProductSpinner = false;
                    }
                }));
            }
        }));
        
        

    }

    ngOnDestroy(): void {
        this.subscriptions.forEach((subscription) => subscription.unsubscribe());
    }

    // Set Input Limit to 6 numbers for Pincode
    handleKeyboardInput(value: string, event: KeyboardEvent) {
        // Clear Previous Availability Response Data
        if ((this.availabilityResponse.data || this.availabilityResponse.error) && (event.key === "Backspace" || event.key === "Delete")) {
            delete this.availabilityResponse.data;
            delete this.availabilityResponse.error;
        }
        // disable User input if the pincode field already have 6 digits
        if (value.length >= 6 && !(event.key === "Backspace" || event.key === "Delete")) {
            return false;
        }
        return true;
    }

    // Method to check availability of Product with pincode
    checkAvailability() {
        if (this.availabilityForm.valid && this.pid) {
            // Show Availability Spinner
            this.showAvailabilitySpinner = true;
            this.subscriptions.push(
                this.productService.getAvailability(this.pid, this.availabilityForm.controls['pincode'].value).subscribe({
                    next: (resp) => {
                        if (resp) {
                            // Set availability Repsonse
                            this.availabilityResponse = resp;
                            this.isProductServiceable = resp.data?.availability;

                        } else {
                            alert("Some Error Occurred!");
                        }
                    },
                    error: (err) => {
                        console.error(err);
                        alert("Internal Server Error!");
                    },
                    complete: () => {
                        // Hide Availability Repsonse
                        this.showAvailabilitySpinner = false;
                    }
                })
            )
        }
    }

    addToCart(productId:any){
        this.productService.addToCart(productId).subscribe(
            (response)=>{
                console.log(response);
                this.router.navigate(['/cart']);
            },
            (error)=>{
                console.log(error);
            }
        );
    }

    buyProduct(productId: any) {
        console.log(productId);
        if(this.userAuthService.isUser()){
            
            this.router.navigate(['/buyProduct', {
                isSingleProductCheckout: true, id: productId
            }]);
        }
        else{
            this.router.navigate(['/forbidden']);
        }
    }
}
