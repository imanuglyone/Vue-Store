package com.vuestore.service;

import com.vuestore.dto.auth.AuthResponse;
import com.vuestore.dto.auth.LoginRequest;
import com.vuestore.dto.auth.RegisterRequest;
import com.vuestore.dto.user.UserResponse;
import com.vuestore.entity.Role;
import com.vuestore.entity.User;
import com.vuestore.repository.UserRepository;
import com.vuestore.security.CustomUserDetails;
import com.vuestore.security.CustomUserDetailsService;
import com.vuestore.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final CustomUserDetailsService userDetailsService;

  public AuthService(UserRepository userRepository,
                     PasswordEncoder passwordEncoder,
                     AuthenticationManager authenticationManager,
                     JwtService jwtService,
                     CustomUserDetailsService userDetailsService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  public AuthResponse register(RegisterRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new IllegalArgumentException("Email already registered");
    }

    User user = new User();
    user.setEmail(request.email());
    user.setName(request.name());
    user.setPassword(passwordEncoder.encode(request.password()));
    user.setRole(Role.ROLE_USER);

    User saved = userRepository.save(user);
    CustomUserDetails details = new CustomUserDetails(saved);
    String token = jwtService.generateToken(details);

    return new AuthResponse(token, toUserResponse(saved));
  }

  public AuthResponse login(LoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(request.email(), request.password())
    );

    if (!authentication.isAuthenticated()) {
      throw new IllegalArgumentException("Invalid credentials");
    }

    CustomUserDetails details = (CustomUserDetails) userDetailsService.loadUserByUsername(request.email());
    String token = jwtService.generateToken(details);

    User user = userRepository.findByEmail(request.email())
      .orElseThrow(() -> new IllegalArgumentException("User not found"));

    return new AuthResponse(token, toUserResponse(user));
  }

  private UserResponse toUserResponse(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
  }
}
