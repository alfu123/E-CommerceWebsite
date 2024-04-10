import { Options } from '@angular-slider/ngx-slider';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ApiResponseModel } from 'src/app/shared/models/api-response';
import { CheckboxModel } from 'src/app/shared/models/checkbox';
import { ProductModel } from 'src/app/shared/models/product';
import { DataService } from 'src/app/shared/services/api/data.service';
import { ProductService } from 'src/app/shared/services/api/product.service';
import { UserApiService } from 'src/app/shared/services/api/user-api.service';

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {
    // Price Range Slider Vars
    minPrice: number = 0;
    maxPrice: number = 0;
    options: Options = {
        floor: 0,
        ceil: 0,
        noSwitching: true,
        translate: (value: number): string => {
            return '$' + value;
        },
        getSelectionBarColor(value) {
            return '#ff4081';
        },
        getPointerColor(value) {
            return '#ff4081';
        }
    };

    // Price & Brand Panel State
    pricePanelOpen = true;
    brandPanelOpen = false;

    pageNumber:number=0;
    subscriptions: Subscription[] = [];
    results: ApiResponseModel;
    products: ProductModel[] = [];
    filteredProducts: ProductModel[] = [];
    brands: CheckboxModel[] = [];
    showDrawer: boolean = false;
    showSpinner: boolean = false;
    productName: string = '';
    showLoadButton:boolean=false;
    resp: any;

    constructor(private productService: ProductService, private dataService: DataService, private router: Router,private userService:UserApiService) {
        this.results = { status: '', status_code: 0, data: [] };
    }

    ngOnInit(): void {
        this.fetchAllProducts();
        this.subscriptions.push(this.dataService.currentState.subscribe((open) => this.showDrawer = open))
    }

    ngOnDestroy(): void {
        this.subscriptions.forEach((subscription) => { subscription.unsubscribe });
    }
    // Get All Products
    fetchAllProducts() {
        this.showSpinner = true;
        this.subscriptions.push(this.productService.getAllProducts(this.pageNumber).subscribe({
            next: (resp) => {
                if (resp) {
                    this.results = resp;
                    console.log(this.results.data.length);
                    if (resp.data.length == 12) {
                        
                        // this.products = resp.data;
                        this.showLoadButton=true;
                        // this.filteredProducts = this.products;
                        // this.updateFilters();
                        
                    } 
                    else {
                        
                        // alert("No Product Found!");
                        this.showLoadButton=false;
                    } 
                    this.products = resp.data;
                    this.filteredProducts = this.products;
                    this.updateFilters();
                    this.resp.data.forEach((p: ProductModel) =>this.products.push(p)); 
                    
                } else {
                    alert("Some Error Occurred!");
                }
                
            },
            error: (err) => {
                console.error(err);
                alert("Internal Server Error!");
            },
            complete: () => {
                this.showSpinner = false;
            }
        }));
    }

    // Set Filters For Products
    updateFilters() {
        let min = this.products[0].price;
        let max = this.products[0].price;
        this.products.forEach((x) => {
            // Update Price Max Value
            if (x.price > max) {
                max = x.price;
            }
            // Update Price Min Value
            if (x.price < min) {
                min = x.price;
            }
            // Update Brands Checkbox
            if (this.brands.filter((brand) => { return brand.label == x.brand }).length == 0) {
                this.brands.push({
                    label: x.brand,
                    selected: true
                });
            }
        });
        // Sort Brands with name
        this.brands.sort((b1, b2) => { return b1.label.localeCompare(b2.label) });
        // Set options for ng-slider
        this.options = {
            floor: min,
            ceil: max,
            noSwitching: true,
            translate: (value: number): string => {
                return '$' + value;
            },
        }
        // Set Minimum & maximum price for ng-slider
        this.minPrice = min;
        this.maxPrice = max;
    }

    // Filter Products By Brand
    filterProductsByBrand(brand: CheckboxModel) {
        brand.selected = !brand.selected;
        this.filterProduct();
    }

    // Filter Products By Name
    filterProductsByName(name: string) {
        this.productName = name;
        this.filterProduct();
    }

    // Filter Products with Name, Brand, & price
    filterProduct() {
        this.filteredProducts = this.products.filter((x) => {
            if ((this.brands.find((brand) => {
                return (x.brand == brand.label && brand.selected);
            })) && (x.price >= this.minPrice && x.price <= this.maxPrice)
            ) {
                if (this.productName) {
                    return (x.pname.toLowerCase().startsWith(this.productName.toLowerCase()))
                }
                return x;
            }
            return;
        });
    }
    // Load other next Item
    public loadMoreProduct() {
        this.pageNumber = this.pageNumber + 1;
        this.fetchAllProducts();
      }

    // Navigate to view product details
    viewProduct(pid: number) {
        console.log(pid);
        this.router.navigateByUrl(`products?pid=${pid}`);
    }
}