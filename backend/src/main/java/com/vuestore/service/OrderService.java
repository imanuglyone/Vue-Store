package com.vuestore.service;

import com.vuestore.dto.order.OrderCreateRequest;
import com.vuestore.dto.order.OrderItemResponse;
import com.vuestore.dto.order.OrderResponse;
import com.vuestore.entity.CartItem;
import com.vuestore.entity.Order;
import com.vuestore.entity.OrderItem;
import com.vuestore.entity.OrderStatus;
import com.vuestore.entity.User;
import com.vuestore.repository.CartItemRepository;
import com.vuestore.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
  private final OrderRepository orderRepository;
  private final CartItemRepository cartItemRepository;

  public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository) {
    this.orderRepository = orderRepository;
    this.cartItemRepository = cartItemRepository;
  }

  @Transactional
  public OrderResponse createOrder(User user, OrderCreateRequest request) {
    List<CartItem> items = cartItemRepository.findByUser(user);
    if (items.isEmpty()) {
      throw new IllegalArgumentException("Cart is empty");
    }

    if ("CARD".equalsIgnoreCase(request.paymentMethod())
      && (request.paymentLast4() == null || request.paymentLast4().isBlank())) {
      throw new IllegalArgumentException("Payment last4 is required for card");
    }

    Order order = new Order();
    order.setUser(user);
    order.setStatus(OrderStatus.CREATED);
    order.setCreatedAt(LocalDateTime.now());
    order.setFullName(request.fullName());
    order.setPhone(request.phone());
    order.setAddress(request.address());
    order.setPaymentMethod(request.paymentMethod());
    order.setPaymentLast4(request.paymentLast4());
    order.setPaymentStatus("PENDING");

    BigDecimal total = BigDecimal.ZERO;
    for (CartItem item : items) {
      OrderItem orderItem = new OrderItem();
      orderItem.setOrder(order);
      orderItem.setProductName(item.getProduct().getName());
      orderItem.setProductPrice(item.getProduct().getPrice());
      orderItem.setQuantity(item.getQuantity());
      orderItem.setLineTotal(item.getProduct().getPrice()
        .multiply(BigDecimal.valueOf(item.getQuantity())));
      order.getItems().add(orderItem);
      total = total.add(orderItem.getLineTotal());
    }

    order.setTotalAmount(total);

    Order saved = orderRepository.save(order);
    cartItemRepository.deleteByUser(user);
    return toResponse(saved);
  }

  @Transactional(readOnly = true)
  public List<OrderResponse> getOrders(User user) {
    return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
      .map(this::toResponse)
      .toList();
  }

  @Transactional(readOnly = true)
  public OrderResponse getOrderById(User user, Long orderId) {
    Order order = orderRepository.findById(orderId)
      .orElseThrow(() -> new IllegalArgumentException("Order not found"));

    if (!order.getUser().getId().equals(user.getId())) {
      throw new IllegalArgumentException("Order not found");
    }

    return toResponse(order);
  }

  @Transactional
  public OrderResponse cancelOrder(User user, Long orderId) {
    Order order = orderRepository.findById(orderId)
      .orElseThrow(() -> new IllegalArgumentException("Order not found"));

    if (!order.getUser().getId().equals(user.getId())) {
      throw new IllegalArgumentException("Order not found");
    }

    if (order.getStatus() != OrderStatus.CREATED) {
      throw new IllegalArgumentException("Order cannot be canceled");
    }

    order.setStatus(OrderStatus.CANCELED);
    order.setPaymentStatus("CANCELED");
    return toResponse(orderRepository.save(order));
  }

  @Transactional(readOnly = true)
  public List<OrderResponse> getAllOrders() {
    return orderRepository.findAll().stream()
      .map(this::toResponse)
      .toList();
  }

  @Transactional
  public OrderResponse updateStatus(Long orderId, OrderStatus status) {
    Order order = orderRepository.findById(orderId)
      .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    order.setStatus(status);
    if (status == OrderStatus.PAID) {
      order.setPaymentStatus("PAID");
    }
    return toResponse(orderRepository.save(order));
  }

  private OrderResponse toResponse(Order order) {
    List<OrderItemResponse> itemResponses = order.getItems().stream()
      .map(item -> new OrderItemResponse(
        item.getProductName(),
        item.getProductPrice(),
        item.getQuantity(),
        item.getLineTotal()
      ))
      .toList();

    return new OrderResponse(
      order.getId(),
      order.getStatus().name(),
      order.getTotalAmount(),
      order.getCreatedAt(),
      itemResponses,
      order.getFullName(),
      order.getPhone(),
      order.getAddress(),
      order.getPaymentMethod(),
      order.getPaymentLast4(),
      order.getPaymentStatus()
    );
  }
}
