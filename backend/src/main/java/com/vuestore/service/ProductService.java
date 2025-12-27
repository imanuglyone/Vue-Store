package com.vuestore.service;

import com.vuestore.dto.product.ProductResponse;
import com.vuestore.entity.Product;
import com.vuestore.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<ProductResponse> getAll() {
    return productRepository.findAll().stream()
      .map(this::toResponse)
      .toList();
  }

  public ProductResponse getById(Long id) {
    Product product = productRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    return toResponse(product);
  }

  private ProductResponse toResponse(Product product) {
    return new ProductResponse(
      product.getId(),
      product.getName(),
      product.getDescription(),
      product.getPrice(),
      product.getImageUrl(),
      product.getCategory(),
      product.getStock(),
      product.getCreatedAt()
    );
  }
}
