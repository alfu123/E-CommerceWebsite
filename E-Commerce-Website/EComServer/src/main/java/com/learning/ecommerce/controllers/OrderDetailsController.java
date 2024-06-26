package com.learning.ecommerce.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learning.ecommerce.dto.OrderInputDto;
import com.learning.ecommerce.dto.TransactionDetailsDto;
import com.learning.ecommerce.services.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderDetailsController {
    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping({"/createTransaction/{amount}"})
    public TransactionDetailsDto createTransaction(@PathVariable(name = "amount") Double amount) {
        return orderDetailService.createTransaction(amount);
    }
    @PreAuthorize("hasRole('User')")
    @PostMapping({"/placeOrder/{isCartCheckout}"})
    public ResponseEntity<String> createOrder(@PathVariable boolean isCartCheckout,@RequestBody OrderInputDto orderInputDto) throws JsonProcessingException {
        return orderDetailService.placedOrder(orderInputDto,isCartCheckout);
    }
    @PreAuthorize("hasRole('User')")
    @GetMapping({"/getOrderDetails"})
    public ResponseEntity<String> getMyOrder(){

        return orderDetailService.getMyOrder();
    }
    @PreAuthorize("hasRole('Admin')")
    @GetMapping({"/getAllOrderDetails/{status}"})
    public ResponseEntity<String> getAllOrder(@PathVariable String status){
        return orderDetailService.getAllOrderDetails(status);
    }
    @PreAuthorize("hasRole('Admin')")
    @GetMapping({"/markOrderAsDeliver/{orderId}"})
    public ResponseEntity<String> markOrderAsDeliver(@PathVariable Integer orderId){
        return orderDetailService.markOrderAsDeliver(orderId);

    }
}
