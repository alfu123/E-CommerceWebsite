package com.learning.ecommerce.dao;

import com.learning.ecommerce.models.Cart;
import com.learning.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDao extends JpaRepository<Cart,Integer> {

    List<Cart> findByUser(User user);

    void deleteById(Integer cartId);
}
