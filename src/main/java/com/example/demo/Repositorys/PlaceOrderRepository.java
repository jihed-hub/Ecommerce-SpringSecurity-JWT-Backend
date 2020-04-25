package com.example.demo.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.PlaceOrder;

@Repository
public interface PlaceOrderRepository extends JpaRepository<PlaceOrder,Long> {

	PlaceOrder findByOrderId(int orderId);
}
