package com.vuestore.service;

import com.vuestore.dto.cart.CartItemRequest;
import com.vuestore.dto.cart.CartItemResponse;
import com.vuestore.dto.cart.CartResponse;
import com.vuestore.dto.cart.UpdateCartItemRequest;
import com.vuestore.entity.CartItem;
import com.vuestore.entity.Product;
import com.vuestore.entity.User;
import com.vuestore.repository.CartItemRepository;
import com.vuestore.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {
  private final CartItemRepository cartItemRepository;
  private final ProductRepository productRepository;

  public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
    this.cartItemRepository = cartItemRepository;
    this.productRepository = productRepository;
  }

  public CartResponse getCart(User user) {
    List<CartItem> items = cartItemRepository.findByUser(user);
    return toCartResponse(items);
  }

  @Transactional
  public CartResponse addItem(User user, CartItemRequest request) {
    Product product = productRepository.findById(request.productId())
      .orElseThrow(() -> new IllegalArgumentException("Product not found"));

    CartItem item = cartItemRepository.findByUserAndProduct(user, product)
      .orElseGet(() -> {
        CartItem newItem = new CartItem();
        newItem.setUser(user);
        newItem.setProduct(product);
        return newItem;
      });

    item.setQuantity(item.getQuantity() + request.quantity());
    cartItemRepository.save(item);
    return getCart(user);
  }

  @Transactional
  public CartResponse updateItem(User user, Long productId, UpdateCartItemRequest request) {
    Product product = productRepository.findById(productId)
      .orElseThrow(() -> new IllegalArgumentException("Product not found"));

    CartItem item = cartItemRepository.findByUserAndProduct(user, product)
      .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

    item.setQuantity(request.quantity());
    cartItemRepository.save(item);
    return getCart(user);
  }

  @Transactional
  public CartResponse removeItem(User user, Long productId) {
    Product product = productRepository.findById(productId)
      .orElseThrow(() -> new IllegalArgumentException("Product not found"));

    CartItem item = cartItemRepository.findByUserAndProduct(user, product)
      .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

    cartItemRepository.delete(item);
    return getCart(user);
  }

  @Transactional
  public void clear(User user) {
    cartItemRepository.deleteByUser(user);
  }

  private CartResponse toCartResponse(List<CartItem> items) {
    List<CartItemResponse> responses = items.stream()
      .map(this::toItemResponse)
      .toList();

    int totalItems = items.stream()
      .mapToInt(CartItem::getQuantity)
      .sum();

    BigDecimal totalPrice = items.stream()
      .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    return new CartResponse(responses, totalItems, totalPrice);
  }

  private CartItemResponse toItemResponse(CartItem item) {
    BigDecimal lineTotal = item.getProduct().getPrice()
      .multiply(BigDecimal.valueOf(item.getQuantity()));

    return new CartItemResponse(
      item.getProduct().getId(),
      item.getProduct().getName(),
      item.getProduct().getImageUrl(),
      item.getProduct().getPrice(),
      item.getQuantity(),
      lineTotal
    );
  }
}
