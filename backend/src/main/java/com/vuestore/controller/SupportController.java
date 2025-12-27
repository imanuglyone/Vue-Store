package com.vuestore.controller;

import com.vuestore.dto.support.ContactRequest;
import com.vuestore.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/support")
public class SupportController {
  private final ContactService contactService;

  public SupportController(ContactService contactService) {
    this.contactService = contactService;
  }

  @PostMapping("/contact")
  public ResponseEntity<Void> contact(@Valid @RequestBody ContactRequest request) {
    contactService.save(request);
    return ResponseEntity.noContent().build();
  }
}
