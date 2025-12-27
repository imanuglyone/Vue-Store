package com.vuestore.repository;

import com.vuestore.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  boolean existsByName(String name);
  List<Product> findAllByName(String name);
}
