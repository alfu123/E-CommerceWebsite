package com.learning.ecommerce.dto;

public class OrderProductQuantityDto {
    private Integer productId;
    private Integer quantity;

    public OrderProductQuantityDto(Integer productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderProductQuantityDto() {
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
