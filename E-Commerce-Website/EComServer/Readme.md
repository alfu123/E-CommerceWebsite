## Example API URLs

Request Type    | URL                                                                             | Descreption
--------------- |---------------------------------------------------------------------------------|-----------------------------------------
POST            | `http://localhost:8282/api/auth/login`                                          | Login URL and generate Token
POST            | `http://localhost:8282/api/auth/signup`                                         | User Registration URL
POST            | `http://localhost:8282/api/products`                                            | Add Product to Database URL
GET             | `http://localhost:8282/api/products`                                            | Get All Product URL
GET             | `http://localhost:8282/api/products?pid=2`                                      | Get Product with id
GET             | `http://localhost:8282/api/products?pname=Noise+ColorFit+Pro+2`                 | Get Product with product name
GET             | `http://localhost:8282/api/products?brand=Nike`                                 | Get Product with product brand
GET             | `http://localhost:8282/api/products?pid=2&pname=Noise+ColorFit+Pro+2`           | Get Product with id & product name
GET             | `http://localhost:8282/api/products?pid=2&brand=Nike`                           | Get Product with id & product brand
GET             | `http://localhost:8282/api/products?pname=Noise+ColorFit+Pro+2&brand=Nike`      | Get Product with product brand & product name
GET             | `http://localhost:8282/api/products?pid=2&pname=Noise+ColorFit+Pro+2&brand=Nike` | Get Product with id & product name & product brand
GET             | `http://localhost:8282/api/products/product?pid=1&pincode=222004`               | Check Availability & Expected Delivery Date from product id & pincode
GET             | `http://localhost:8282/api/products/getProductDetails/true/1`                   | Buy Single Product Or buy your all products in Cart where client must have role USER
POST            | `http://localhost:8282/api/order/placeOrder`                                    |Place order by User
## Login JSON Request 

```
{
    "username" : "admin",
    "password" : "admin"
}
```

## Register User JSON Request 

```
{
    "username" : "admin",
    "password" : "admin",
    "uname" : "Admin Admin"
}
```

## Add Product JSON Request 

```
{
    "pid": 3,
    "pname": "TITAN Men Smart Pro",
    "description": "Enter Description",
    "brand": "Titan",
    "price": 11995.00,
    "imageUrl": "https://staticimg.titan.co.in/Titan/Catalog/90149AP01_1.jpg",

    "serviceability": [
        {
            "pincode": 226021,
            "expectedDelivery" : 29 `Days For Delivery`
        },
        {
            "pincode": 226019,
            "expectedDelivery" : 30 `Days For Delivery`
        }
    ]
}
```