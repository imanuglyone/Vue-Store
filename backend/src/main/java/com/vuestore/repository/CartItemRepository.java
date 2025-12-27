package com.vuestore.repository;

import com.vuestore.entity.CartItem;
import com.vuestore.entity.Product;
import com.vuestore.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  List<CartItem> findByUser(User user);
  Optional<CartItem> findByUserAndProduct(User user, Product product);
  void deleteByUser(User user);
}
