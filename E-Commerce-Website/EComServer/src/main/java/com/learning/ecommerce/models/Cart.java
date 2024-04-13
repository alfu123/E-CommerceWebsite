package com.learning.ecommerce.models;

import javax.persistence.*;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer cartId;
    @OneToOne(cascade = CascadeType.MERGE)
    private Product product;
    @OneToOne(cascade = CascadeType.MERGE)
    private User user;

    public Cart(Integer cartId, Product product, User user) {
        this.cartId = cartId;
        this.product = product;
        this.user = user;
    }
    public Cart() {
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
