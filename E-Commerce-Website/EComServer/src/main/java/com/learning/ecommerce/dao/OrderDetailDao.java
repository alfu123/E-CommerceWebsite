package com.learning.ecommerce.dao;

import com.learning.ecommerce.models.OrderDetail;
import com.learning.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailDao extends JpaRepository<OrderDetail,Integer> {
    List<OrderDetail> findByOrderStatus(String status);
    List<OrderDetail> findByUser(User user);

}
