package com.vuestore.controller.admin;

import com.vuestore.dto.order.OrderResponse;
import com.vuestore.dto.order.UpdateOrderStatusRequest;
import com.vuestore.entity.OrderStatus;
import com.vuestore.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/orders")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminOrderController {
  private final OrderService orderService;

  public AdminOrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  public ResponseEntity<List<OrderResponse>> getAll() {
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<OrderResponse> updateStatus(@PathVariable("id") Long id,
                                                    @Valid @RequestBody UpdateOrderStatusRequest request) {
    OrderStatus status = OrderStatus.valueOf(request.status());
    return ResponseEntity.ok(orderService.updateStatus(id, status));
  }
}
