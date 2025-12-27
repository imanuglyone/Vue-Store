package com.vuestore.controller;

import com.vuestore.dto.order.OrderCreateRequest;
import com.vuestore.dto.order.OrderResponse;
import com.vuestore.entity.User;
import com.vuestore.service.OrderService;
import com.vuestore.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
  private final OrderService orderService;
  private final UserService userService;

  public OrderController(OrderService orderService, UserService userService) {
    this.orderService = orderService;
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
    User user = userService.getCurrentUser();
    return ResponseEntity.ok(orderService.createOrder(user, request));
  }

  @GetMapping
  public ResponseEntity<List<OrderResponse>> getOrders() {
    User user = userService.getCurrentUser();
    return ResponseEntity.ok(orderService.getOrders(user));
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderResponse> getOrder(@PathVariable("id") Long id) {
    User user = userService.getCurrentUser();
    return ResponseEntity.ok(orderService.getOrderById(user, id));
  }

  @PatchMapping("/{id}/cancel")
  public ResponseEntity<OrderResponse> cancelOrder(@PathVariable("id") Long id) {
    User user = userService.getCurrentUser();
    return ResponseEntity.ok(orderService.cancelOrder(user, id));
  }
}
