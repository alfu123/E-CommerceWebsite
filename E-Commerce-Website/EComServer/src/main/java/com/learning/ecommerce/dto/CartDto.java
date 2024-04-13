package com.learning.ecommerce.dto;

import com.learning.ecommerce.models.Product;
import com.learning.ecommerce.models.User;



public class CartDto {

    private Integer cartId;

    private ProductDto productDto;

    private UserDto userDto;

    public CartDto(Integer cartId, ProductDto productDto, UserDto userDto) {
        this.cartId = cartId;
        this.productDto = productDto;
        this.userDto = userDto;
    }

    public CartDto() {
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}
