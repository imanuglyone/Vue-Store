package com.vuestore.service;

import com.vuestore.dto.product.ProductRequest;
import com.vuestore.dto.product.ProductResponse;
import com.vuestore.entity.Product;
import com.vuestore.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminProductService {
  private final ProductRepository productRepository;

  public AdminProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<ProductResponse> getAll() {
    return productRepository.findAll().stream()
      .map(this::toResponse)
      .toList();
  }

  public ProductResponse create(ProductRequest request) {
    Product product = new Product();
    apply(request, product);
    product.setCreatedAt(LocalDateTime.now());
    return toResponse(productRepository.save(product));
  }

  public ProductResponse update(Long id, ProductRequest request) {
    Product product = productRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    apply(request, product);
    return toResponse(productRepository.save(product));
  }

  public void delete(Long id) {
    Product product = productRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    productRepository.delete(product);
  }

  private void apply(ProductRequest request, Product product) {
    product.setName(request.name());
    product.setDescription(request.description());
    product.setPrice(request.price());
    product.setImageUrl(request.imageUrl());
    product.setCategory(request.category());
    product.setStock(request.stock());
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
