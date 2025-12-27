package com.vuestore.repository;

import com.vuestore.entity.Order;
import com.vuestore.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByUserOrderByCreatedAtDesc(User user);
}
