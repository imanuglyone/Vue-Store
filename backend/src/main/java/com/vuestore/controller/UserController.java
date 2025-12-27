package com.vuestore.controller;

import com.vuestore.dto.user.UserResponse;
import com.vuestore.entity.User;
import com.vuestore.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponse> me() {
    User user = userService.getCurrentUser();
    return ResponseEntity.ok(new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name()));
  }
}
