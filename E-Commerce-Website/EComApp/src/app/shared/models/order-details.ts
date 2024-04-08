import { OrderQuantity } from "./order-quantity";
import { ServiceabilityModel } from "./serviceability";

export interface OrderDetails {
    fullName: string;
    fullAddress: string;
    contactNumber: string;
    alternateContactNumber: string;
    pincode:'';
//    serviceability:ServiceabilityModel[];
    // transactionId: string,
    orderProductQuantityList: OrderQuantity[];
}