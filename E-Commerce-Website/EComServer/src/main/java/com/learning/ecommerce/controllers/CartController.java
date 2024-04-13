package com.learning.ecommerce.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learning.ecommerce.models.Cart;
import com.learning.ecommerce.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PreAuthorize("hasRole('User')")
    @GetMapping({"/addToCart/{productId}"})
    public String addToCart(@PathVariable(name = "productId") Integer productId) throws JsonProcessingException {
        return cartService.addToCart(productId);
    }

}
