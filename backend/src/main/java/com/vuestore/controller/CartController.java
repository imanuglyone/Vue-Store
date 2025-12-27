package com.vuestore.controller;

import com.vuestore.dto.cart.CartItemRequest;
import com.vuestore.dto.cart.CartResponse;
import com.vuestore.dto.cart.UpdateCartItemRequest;
import com.vuestore.entity.User;
import com.vuestore.service.CartService;
import com.vuestore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {
  private final CartService cartService;
  private final UserService userService;

  public CartController(CartService cartService, UserService userService) {
    this.cartService = cartService;
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<CartResponse> getCart() {
    User user = userService.getCurrentUser();
    return ResponseEntity.ok(cartService.getCart(user));
  }

  @PostMapping("/items")
  public ResponseEntity<CartResponse> addItem(@Valid @RequestBody CartItemRequest request) {
    User user = userService.getCurrentUser();
    return ResponseEntity.ok(cartService.addItem(user, request));
  }

  @PatchMapping("/items/{productId}")
  public ResponseEntity<CartResponse> updateItem(@PathVariable Long productId,
                                                 @Valid @RequestBody UpdateCartItemRequest request) {
    User user = userService.getCurrentUser();
    return ResponseEntity.ok(cartService.updateItem(user, productId, request));
  }

  @DeleteMapping("/items/{productId}")
  public ResponseEntity<CartResponse> removeItem(@PathVariable Long productId) {
    User user = userService.getCurrentUser();
    return ResponseEntity.ok(cartService.removeItem(user, productId));
  }
}
