import { ServiceabilityModel } from "./serviceability";

export interface ProductModel {
    // productId: number;
    pid: number,
    pname: string,
    brand: string,
    description: string,
    price: number,
    imageUrl: string,
    serviceability: ServiceabilityModel[]
}